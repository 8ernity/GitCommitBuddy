package com.gitcommitbuddy.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.gitcommitbuddy.R
import com.gitcommitbuddy.data.PreferencesManager
import com.gitcommitbuddy.databinding.ActivityMainBinding
import com.gitcommitbuddy.service.FloatingWidgetService
import com.gitcommitbuddy.service.ReminderWorker
import com.gitcommitbuddy.ui.settings.SettingsActivity
import com.gitcommitbuddy.util.MotivationalMessages
import com.gitcommitbuddy.util.NotificationHelper
import com.gitcommitbuddy.util.PermissionHelper
import com.gitcommitbuddy.util.TimeFormatter
import com.gitcommitbuddy.viewmodel.CommitUiState
import com.gitcommitbuddy.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    @Inject lateinit var notifHelper: NotificationHelper
    @Inject lateinit var prefs: PreferencesManager

    // ── Permission launchers ──────────────────────────────────────────────────

    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (PermissionHelper.canDrawOverlays(this)) {
            startFloatingWidget()
            showSnackbar("Floating widget enabled ✅")
        } else {
            // Uncheck the switch — permission was denied
            binding.switchWidget.isChecked = false
            viewModel.setWidgetEnabled(false)
            showSnackbar("Overlay permission required for the floating widget")
        }
    }

    private val notifPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) scheduleReminders()
        else showSnackbar("Notification permission denied — reminders won't fire")
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        notifHelper.createNotificationChannels()
        setupUI()
        observeViewModel()
        requestPermissionsIfNeeded()
        viewModel.refresh()
        viewModel.fetchProfile()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    // ── UI Setup ──────────────────────────────────────────────────────────────

    private fun setupUI() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }

        binding.btnOpenGitHub.setOnClickListener {
            val username = viewModel.githubUsername.value
            val url = if (username.isNotBlank()) "https://github.com/$username"
                      else "https://github.com"
            openUrl(url)
        }

        binding.btnGoToSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Guard against the switch firing during programmatic updates
        var programmaticChange = false
        binding.switchWidget.setOnCheckedChangeListener { _, isChecked ->
            if (programmaticChange) return@setOnCheckedChangeListener
            viewModel.setWidgetEnabled(isChecked)
            if (isChecked) checkOverlayAndStartWidget()
            else stopFloatingWidget()
        }

        lifecycleScope.launch {
            viewModel.widgetEnabled.collectLatest { enabled ->
                programmaticChange = true
                binding.switchWidget.isChecked = enabled
                programmaticChange = false
            }
        }

        binding.cardCommitStatus.setOnClickListener { viewModel.refresh() }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.swipeRefresh.isRefreshing = state is CommitUiState.Loading

                when (state) {
                    is CommitUiState.Loading -> {
                        binding.layoutContent.isVisible       = false
                        binding.layoutNotConfigured.isVisible = false
                        binding.layoutError.isVisible         = false
                    }
                    is CommitUiState.NotConfigured -> {
                        binding.layoutContent.isVisible       = false
                        binding.layoutNotConfigured.isVisible = true
                        binding.layoutError.isVisible         = false
                    }
                    is CommitUiState.Loaded -> {
                        binding.layoutContent.isVisible       = true
                        binding.layoutNotConfigured.isVisible = false
                        binding.layoutError.isVisible         = false

                        lifecycleScope.launch {
                            val status = state.status
                            val limit = prefs.getSnapshot().commitLimit
                            val count = status.todayCommitCount

                            binding.tvCommitStatus.text = if (status.committedToday)
                                "✅ Goal Reached! ($count/$limit)" else "❌ $count/$limit commits today"

                            binding.tvMotivation.text = if (status.committedToday)
                                MotivationalMessages.getCommittedMessage()
                            else MotivationalMessages.getPendingMessage()

                            binding.tvLastCommit.text = status.lastCommitTime
                                ?.let { TimeFormatter.formatRelative(it) }
                                ?: "No recent commits"

                            binding.tvLastRepo.text    = status.lastCommitRepo ?: ""
                            binding.tvLastMessage.text = status.lastCommitMessage ?: ""
                            binding.tvStreak.text      =
                                MotivationalMessages.getStreakDescription(status.currentStreak)

                            MotivationalMessages.getStreakMilestoneMessage(status.currentStreak)
                                ?.let { showSnackbar(it) }

                            binding.ivStatusIcon.apply {
                                setImageResource(
                                    if (status.committedToday) R.drawable.ic_check_circle
                                    else R.drawable.ic_warning
                                )
                                animate().scaleX(1.2f).scaleY(1.2f).setDuration(150)
                                    .withEndAction {
                                        animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                                    }.start()
                            }
                        }
                    }
                    is CommitUiState.Error -> {
                        binding.layoutContent.isVisible       = false
                        binding.layoutNotConfigured.isVisible = false
                        binding.layoutError.isVisible         = true
                        binding.tvError.text                  = state.message
                    }
                    CommitUiState.Idle -> { /* wait for first refresh */ }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.userProfile.collectLatest { user ->
                if (user != null) {
                    binding.tvUsername.text    = "@${user.login}"
                    binding.tvDisplayName.text = user.name ?: user.login
                }
            }
        }
    }

    // ── Permissions ───────────────────────────────────────────────────────────

    private fun requestPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                scheduleReminders()
            }
        } else {
            scheduleReminders()
        }

        //if (!PermissionHelper.isBatteryOptimizationIgnored(this)) {
        //    showBatteryOptimizationDialog()
        //}
    }

    private fun scheduleReminders() {
        lifecycleScope.launch {
            val snap = prefs.getSnapshot()
            if (snap.notificationsOn) {
                ReminderWorker.scheduleDailyReminder(
                    this@MainActivity,
                    snap.reminderHour,
                    snap.reminderMinute
                )
            }
        }
    }

    private fun checkOverlayAndStartWidget() {
        if (PermissionHelper.canDrawOverlays(this)) {
            startFloatingWidget()
        } else {
            showOverlayPermissionDialog()
        }
    }

    private fun showOverlayPermissionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Overlay Permission Required")
            .setMessage(
                "GitCommit Buddy needs permission to display the floating bubble " +
                "over other apps.\n\nTap 'Allow', then enable 'Allow display over other apps'."
            )
            .setPositiveButton("Allow") { _, _ ->
                PermissionHelper.requestOverlayPermission(this, overlayPermissionLauncher)
            }
            .setNegativeButton("Not Now") { _, _ ->
                binding.switchWidget.isChecked = false
                viewModel.setWidgetEnabled(false)
            }
            .show()
    }

    private fun showBatteryOptimizationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Improve Reliability")
            .setMessage(
                "To ensure daily reminders fire reliably, please exclude GitCommit Buddy " +
                "from battery optimization."
            )
            .setPositiveButton("Optimize") { _, _ ->
                PermissionHelper.requestBatteryOptimizationExemption(this)
            }
            .setNegativeButton("Skip", null)
            .show()
    }

    private fun startFloatingWidget() {
        val intent = Intent(this, FloatingWidgetService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopFloatingWidget() {
        stopService(Intent(this, FloatingWidgetService::class.java))
    }

    // ── Menu ──────────────────────────────────────────────────────────────────

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> {
            startActivity(Intent(this, SettingsActivity::class.java)); true
        }
        R.id.action_refresh -> { viewModel.refresh(); true }
        else -> super.onOptionsItemSelected(item)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun openUrl(url: String) {
        startActivity(
            Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    private fun showSnackbar(msg: String) =
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
}

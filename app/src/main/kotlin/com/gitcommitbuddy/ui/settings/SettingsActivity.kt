package com.gitcommitbuddy.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gitcommitbuddy.R
import com.gitcommitbuddy.databinding.ActivitySettingsBinding
import com.gitcommitbuddy.service.FloatingWidgetService
import com.gitcommitbuddy.service.ReminderWorker
import com.gitcommitbuddy.util.NotificationHelper
import com.gitcommitbuddy.util.PermissionHelper
import com.gitcommitbuddy.util.TimeFormatter
import com.gitcommitbuddy.viewmodel.SettingsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    @Inject lateinit var notifHelper: NotificationHelper

    private var tokenVisible = false

    // Local copies so we never call .value on a StateFlow inside a listener
    private var currentReminderHour   = 21
    private var currentReminderMinute = 0

    // Prevent the switch listener from firing on programmatic isChecked changes
    private var suppressWidgetSwitch = false

    // ── Overlay permission launcher (used when widget switch is toggled ON) ───
    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (PermissionHelper.canDrawOverlays(this)) {
            startFloatingService()
            showSnackbar("Floating widget enabled ✅")
        } else {
            // Permission denied — revert switch without triggering the listener
            suppressWidgetSwitch = true
            binding.switchWidget.isChecked = false
            suppressWidgetSwitch = false
            viewModel.setWidgetEnabled(false)
            showSnackbar("Overlay permission is required for the floating widget")
        }
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupUI()
        observeViewModel()
    }

    // ── UI wiring ─────────────────────────────────────────────────────────────

    private fun setupUI() {

        // ── GitHub credentials ────────────────────────────────────────────────
        binding.btnSaveCredentials.setOnClickListener {
            val username = binding.etUsername.text?.toString()?.trim() ?: ""
            val token    = binding.etToken.text?.toString()?.trim() ?: ""
            if (username.isBlank()) {
                binding.tilUsername.error = "Username required"
                return@setOnClickListener
            }
            binding.tilUsername.error = null
            viewModel.saveCredentials(username, token)
            showSnackbar("Credentials saved ✅")
        }

        // ── Token visibility toggle ───────────────────────────────────────────
        binding.btnToggleToken.setOnClickListener {
            tokenVisible = !tokenVisible
            binding.etToken.inputType = if (tokenVisible)
                InputType.TYPE_CLASS_TEXT
            else
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.etToken.setSelection(binding.etToken.text?.length ?: 0)
            binding.btnToggleToken.setImageResource(
                if (tokenVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
            )
        }

        // ── Reminder time picker ──────────────────────────────────────────────
        binding.cardReminderTime.setOnClickListener { showTimePicker() }

        // ── Floating widget toggle ────────────────────────────────────────────
        // This switch both saves the preference AND starts/stops the actual service.
        binding.switchWidget.setOnCheckedChangeListener { _, checked ->
            if (suppressWidgetSwitch) return@setOnCheckedChangeListener
            viewModel.setWidgetEnabled(checked)
            if (checked) {
                checkOverlayAndStartWidget()
            } else {
                stopFloatingService()
                showSnackbar("Floating widget stopped")
            }
        }

        // ── Notifications toggle ──────────────────────────────────────────────
        binding.switchNotifications.setOnCheckedChangeListener { _, checked ->
            viewModel.setNotificationsEnabled(checked)
            if (checked) {
                notifHelper.createNotificationChannels()
                ReminderWorker.scheduleDailyReminder(this, currentReminderHour, currentReminderMinute)
                showSnackbar("Daily reminders enabled 🔔")
            } else {
                ReminderWorker.cancelAll(this)
                showSnackbar("Reminders disabled")
            }
        }

        // ── Dark mode toggle ──────────────────────────────────────────────────
        binding.switchDarkMode.setOnCheckedChangeListener { _, checked ->
            viewModel.setDarkMode(checked)
            applyDarkMode(checked)
        }

        // ── Bubble colour swatches ────────────────────────────────────────────
        binding.colorGreen.setOnClickListener  { selectColor("#1F8348") }
        binding.colorBlue.setOnClickListener   { selectColor("#1565C0") }
        binding.colorPurple.setOnClickListener { selectColor("#6A1B9A") }
        binding.colorRed.setOnClickListener    { selectColor("#C62828") }
        binding.colorOrange.setOnClickListener { selectColor("#E65100") }

        // ── Battery optimisation ──────────────────────────────────────────────
        binding.btnBatteryOptimization.setOnClickListener {
            if (PermissionHelper.isBatteryOptimizationIgnored(this)) {
                showSnackbar("Already excluded from battery optimization ✅")
            } else {
                PermissionHelper.requestBatteryOptimizationExemption(this)
            }
        }

        // ── PAT help dialog ───────────────────────────────────────────────────
        binding.btnHelpToken.setOnClickListener { showTokenHelp() }
    }

    // ── ViewModel observers ───────────────────────────────────────────────────

    private fun observeViewModel() {
        var firstUsername = true
        var firstToken    = true

        lifecycleScope.launch {
            viewModel.username.collectLatest { u ->
                if (firstUsername) { binding.etUsername.setText(u); firstUsername = false }
            }
        }
        lifecycleScope.launch {
            viewModel.token.collectLatest { t ->
                if (firstToken) { binding.etToken.setText(t); firstToken = false }
            }
        }
        lifecycleScope.launch {
            viewModel.reminderHour.collectLatest { h ->
                currentReminderHour = h
                updateReminderLabel(h, currentReminderMinute)
            }
        }
        lifecycleScope.launch {
            viewModel.reminderMinute.collectLatest { m ->
                currentReminderMinute = m
                updateReminderLabel(currentReminderHour, m)
            }
        }
        lifecycleScope.launch {
            viewModel.widgetEnabled.collectLatest { enabled ->
                suppressWidgetSwitch = true
                binding.switchWidget.isChecked = enabled
                suppressWidgetSwitch = false
            }
        }
        lifecycleScope.launch {
            viewModel.notifEnabled.collectLatest { binding.switchNotifications.isChecked = it }
        }
        lifecycleScope.launch {
            viewModel.darkMode.collectLatest { binding.switchDarkMode.isChecked = it }
        }
    }

    // ── Widget start / stop ───────────────────────────────────────────────────

    /**
     * Checks overlay permission before starting the service.
     * If not granted, shows a rationale dialog and launches the system settings page.
     */
    private fun checkOverlayAndStartWidget() {
        if (PermissionHelper.canDrawOverlays(this)) {
            startFloatingService()
        } else {
            // Revert switch immediately — only re-check on return from settings
            suppressWidgetSwitch = true
            binding.switchWidget.isChecked = false
            suppressWidgetSwitch = false
            viewModel.setWidgetEnabled(false)

            MaterialAlertDialogBuilder(this)
                .setTitle("Overlay Permission Required")
                .setMessage(
                    "GitCommit Buddy needs permission to display the floating bubble " +
                    "over other apps.\n\nTap 'Allow', then enable " +
                    "'Allow display over other apps' for GitCommit Buddy."
                )
                .setPositiveButton("Allow") { _, _ ->
                    PermissionHelper.requestOverlayPermission(this, overlayPermissionLauncher)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun startFloatingService() {
        val intent = Intent(this, FloatingWidgetService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopFloatingService() {
        stopService(Intent(this, FloatingWidgetService::class.java))
    }

    // ── Time picker ───────────────────────────────────────────────────────────

    private fun showTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(currentReminderHour)
            .setMinute(currentReminderMinute)
            .setTitleText("Set Daily Reminder Time")
            .build()

        picker.addOnPositiveButtonClickListener {
            val h = picker.hour
            val m = picker.minute
            viewModel.saveReminderTime(h, m)
            currentReminderHour   = h
            currentReminderMinute = m
            updateReminderLabel(h, m)
            // Re-schedule WorkManager with the updated time
            ReminderWorker.scheduleDailyReminder(this, h, m)
            showSnackbar("Reminder set for ${TimeFormatter.formatTime(h, m)}")
        }

        picker.show(supportFragmentManager, "time_picker")
    }

    private fun updateReminderLabel(hour: Int, minute: Int) {
        binding.tvReminderTime.text = TimeFormatter.formatTime(hour, minute)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun selectColor(hex: String) {
        viewModel.setBubbleColor(hex)
        showSnackbar("Bubble colour updated — restart widget to apply")
    }

    private fun applyDarkMode(enabled: Boolean) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
            if (enabled) androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
            else         androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun showTokenHelp() {
        MaterialAlertDialogBuilder(this)
            .setTitle("How to get a GitHub Token")
            .setMessage(
                "1. Go to github.com → Settings\n" +
                "2. Developer settings → Personal access tokens\n" +
                "   → Fine-grained tokens → Generate new token\n" +
                "3. Set expiry (e.g. 1 year)\n" +
                "4. Repository access: Public Repositories (read-only)\n" +
                "5. Permissions → Events: Read-only\n" +
                "6. Generate & copy the token here\n\n" +
                "⚠️  Your token is stored only on this device and is " +
                "never sent anywhere except api.github.com."
            )
            .setPositiveButton("Got it", null)
            .setNeutralButton("Open GitHub") { _, _ ->
                startActivity(
                    Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/settings/tokens"))
                )
            }
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackbar(msg: String) =
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
}

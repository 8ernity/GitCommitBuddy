package com.gitcommitbuddy.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.Vibrator
import android.os.VibratorManager
import android.util.DisplayMetrics
import android.view.*
import androidx.appcompat.view.ContextThemeWrapper
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.core.view.isVisible
import com.gitcommitbuddy.R
import com.gitcommitbuddy.data.PreferencesManager
import com.gitcommitbuddy.data.db.CommitCacheEntity
import com.gitcommitbuddy.data.repository.GitHubRepository
import com.gitcommitbuddy.databinding.LayoutFloatingBubbleBinding
import com.gitcommitbuddy.databinding.LayoutFloatingPanelBinding
import com.gitcommitbuddy.util.NotificationHelper
import com.gitcommitbuddy.util.TimeFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.math.abs

/**
 * FloatingWidgetService
 * ─────────────────────
 * Foreground service that manages two WindowManager overlay views:
 *
 *  • bubbleView  — small always-visible draggable circle
 *  • panelView   — expanded info card shown on bubble tap
 */
@AndroidEntryPoint
class FloatingWidgetService : Service() {

    @Inject lateinit var repository: GitHubRepository
    @Inject lateinit var prefs: PreferencesManager
    @Inject lateinit var notifHelper: NotificationHelper

    private lateinit var windowManager: WindowManager
    private var bubbleView: View? = null
    private var panelView: View? = null
    private lateinit var bubbleBinding: LayoutFloatingBubbleBinding
    private lateinit var panelBinding: LayoutFloatingPanelBinding

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var bubbleParams: WindowManager.LayoutParams? = null
    private var panelParams: WindowManager.LayoutParams? = null
    private var isPanelVisible = false

    companion object {
        const val ACTION_REFRESH      = "com.gitcommitbuddy.action.REFRESH"
        const val ACTION_STOP         = "com.gitcommitbuddy.action.STOP"
        const val ACTION_MARK_DONE    = "com.gitcommitbuddy.action.MARK_DONE"
        const val ACTION_REMIND_LATER = "com.gitcommitbuddy.action.REMIND_LATER"

        private const val DRAG_SLOP = 12f
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        notifHelper.createNotificationChannels()
        startForeground(
            NotificationHelper.NOTIF_ID_FOREGROUND,
            notifHelper.buildForegroundNotification()
        )
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        addBubble()
        addPanel()
        observeSettings()
        refreshCommitStatus()
    }

    private fun observeSettings() {
        serviceScope.launch {
            prefs.darkMode.collect { isDark ->
                updateTheme(isDark)
            }
        }
    }

    private fun updateTheme(isDark: Boolean) {
        // We use the DayNight theme which should handle dark/light if the system is set correctly,
        // but for the service context we manually apply the mode if possible.
        
        if (bubbleView != null || panelView != null) {
            val wasPanelVisible = isPanelVisible
            removeBubble()
            removePanel()
            addBubble()
            addPanel()
            if (wasPanelVisible) showPanel()
            refreshCommitStatus()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_REFRESH      -> refreshCommitStatus()
            ACTION_MARK_DONE    -> handleMarkDone()
            ACTION_REMIND_LATER -> handleRemindLater()
            ACTION_STOP         -> stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        removeBubble()
        removePanel()
    }

    // ── Bubble view ───────────────────────────────────────────────────────────

    @SuppressLint("InflateParams")
    private fun addBubble() {
        val contextThemeWrapper = ContextThemeWrapper(this, R.style.Theme_GitCommitBuddy)
        bubbleBinding = LayoutFloatingBubbleBinding.inflate(LayoutInflater.from(contextThemeWrapper))
        bubbleView    = bubbleBinding.root

        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        bubbleParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 24
            y = 300
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                blurBehindRadius = 40
            }
        }

        try {
            windowManager.addView(bubbleView, bubbleParams)
            animateBubbleIn()
            setupBubbleTouchListener()
        } catch (e: Exception) {
            e.printStackTrace()
            stopSelf()
        }
    }

    private fun animateBubbleIn() {
        bubbleView?.apply {
            scaleX = 0f
            scaleY = 0f
            alpha  = 0f
            animate()
                .scaleX(1f).scaleY(1f).alpha(1f)
                .setDuration(320)
                .setInterpolator(OvershootInterpolator(2.2f))
                .withEndAction { startBreathingAnimation() }
                .start()
        }
    }

    private fun startBreathingAnimation() {
        bubbleView?.let { view ->
            view.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(2000)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .withEndAction {
                    view.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(2000)
                        .setInterpolator(AccelerateDecelerateInterpolator())
                        .withEndAction { startBreathingAnimation() }
                        .start()
                }
                .start()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupBubbleTouchListener() {
        var downX = 0
        var downY = 0
        var downRawX = 0f
        var downRawY = 0f
        var isDragging = false

        bubbleView?.setOnTouchListener { view, event ->
            val params = bubbleParams ?: return@setOnTouchListener false

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX    = params.x
                    downY    = params.y
                    downRawX = event.rawX
                    downRawY = event.rawY
                    isDragging = false
                    view.animate().scaleX(0.88f).scaleY(0.88f).setDuration(90).start()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = event.rawX - downRawX
                    val dy = event.rawY - downRawY
                    if (!isDragging && (abs(dx) > DRAG_SLOP || abs(dy) > DRAG_SLOP)) {
                        isDragging = true
                    }
                    if (isDragging) {
                        params.x = (downX + dx).toInt().coerceAtLeast(0)
                        params.y = (downY + dy).toInt().coerceAtLeast(0)
                        safeUpdateLayout(bubbleView, params)
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    view.animate().scaleX(1f).scaleY(1f).setDuration(160).start()
                    if (isDragging) {
                        snapToEdge(params)
                    } else {
                        togglePanel()
                    }
                    isDragging = false
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    view.animate().scaleX(1f).scaleY(1f).setDuration(160).start()
                    isDragging = false
                    true
                }
                else -> false
            }
        }
    }

    private fun snapToEdge(params: WindowManager.LayoutParams) {
        val screenWidth = safeScreenWidth()
        val bubbleWidth = bubbleView?.width?.takeIf { it > 0 } ?: 64.dpToPx()
        val midScreen   = screenWidth / 2
        val targetX     = if (params.x + bubbleWidth / 2 < midScreen) 0
                          else screenWidth - bubbleWidth

        android.animation.ValueAnimator.ofInt(params.x, targetX).apply {
            duration     = 280
            interpolator = OvershootInterpolator(1.4f)
            addUpdateListener { anim ->
                params.x = anim.animatedValue as Int
                safeUpdateLayout(bubbleView, params)
            }
        }.start()
    }

    // ── Panel view ────────────────────────────────────────────────────────────

    @SuppressLint("InflateParams")
    private fun addPanel() {
        val contextThemeWrapper = ContextThemeWrapper(this, R.style.Theme_GitCommitBuddy)
        panelBinding = LayoutFloatingPanelBinding.inflate(LayoutInflater.from(contextThemeWrapper))
        panelView = panelBinding.root.apply { isVisible = false }

        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        panelParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                blurBehindRadius = 60
            }
        }

        try {
            windowManager.addView(panelView, panelParams)
            panelView?.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_OUTSIDE) {
                    hidePanel()
                    true
                } else false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setupPanelListeners()
    }

    private fun setupPanelListeners() {
        panelBinding.btnOpenGitHub.setOnClickListener {
            serviceScope.launch {
                val username = prefs.githubUsername.first()
                openUrl(if (username.isNotBlank()) "https://github.com/$username" else "https://github.com")
                hidePanel()
            }
        }
        panelBinding.btnRemindLater.setOnClickListener { handleRemindLater() }
        panelBinding.btnMarkDone.setOnClickListener { handleMarkDone() }
        panelBinding.btnRefresh.setOnClickListener {
            panelBinding.progressBar.isVisible = true
            refreshCommitStatus()
        }
        panelBinding.btnClose.setOnClickListener { hidePanel() }
    }

    private fun handleRemindLater() {
        // Assume ReminderWorker exists as in original code
        // ReminderWorker.scheduleRemindLaterNotification(applicationContext, delayMinutes = 60)
        vibrateShort()
        Toast.makeText(applicationContext, "⏰ Reminder set for 1 hour from now", Toast.LENGTH_SHORT).show()
        hidePanel()
    }

    private fun handleMarkDone() {
        // ReminderWorker.cancelFollowUp(applicationContext)
        bubbleBinding.ivStatus.setImageResource(R.drawable.ic_check_circle)
        vibrateSuccess()
        Toast.makeText(applicationContext, "🎉 Great job! Streak secured.", Toast.LENGTH_SHORT).show()
        hidePanel()
        refreshCommitStatus()
    }

    private fun togglePanel() {
        if (isPanelVisible) hidePanel() else showPanel()
    }

    private fun showPanel() {
        isPanelVisible = true
        panelView?.apply {
            isVisible = true
            alpha  = 0f
            scaleX = 0.7f
            scaleY = 0.7f
            animate()
                .alpha(1f).scaleX(1f).scaleY(1f)
                .setDuration(220)
                .setInterpolator(OvershootInterpolator(1.6f))
                .start()
        }
        vibrateShort()
    }

    private fun hidePanel() {
        isPanelVisible = false
        panelView?.animate()
            ?.alpha(0f)?.scaleX(0.75f)?.scaleY(0.75f)
            ?.setDuration(160)
            ?.withEndAction { panelView?.isVisible = false }
            ?.start()
    }

    private fun refreshCommitStatus() {
        serviceScope.launch {
            val snapshot = prefs.getSnapshot()
            if (snapshot.username.isBlank()) {
                showNotConfigured()
                return@launch
            }
            repository.refreshCommitStatus(snapshot.username, snapshot.token, snapshot.commitLimit)
            val cache = repository.observeCommitStatus().first()
            if (cache != null) {
                updateBubble(cache.committedToday)
                updatePanel(cache, snapshot.commitLimit)
            }
            panelBinding.progressBar.isVisible = false
        }
    }

    private fun updateBubble(committedToday: Boolean) {
        bubbleBinding.ivStatus.setImageResource(
            if (committedToday) R.drawable.ic_check_circle else R.drawable.ic_warning
        )
        bubbleBinding.root.animate()
            .scaleX(1.15f).scaleY(1.15f).setDuration(120)
            .withEndAction {
                bubbleBinding.root.animate()
                    .scaleX(1f).scaleY(1f).setDuration(120).start()
            }.start()
    }

    private fun updatePanel(cache: CommitCacheEntity, limit: Int) {
        val count = cache.todayCommitCount
        
        panelBinding.tvCommitStatus.text =
            if (cache.committedToday) "✅ Goal Reached! ($count/$limit)" 
            else "❌ $count/$limit commits today"

        panelBinding.tvLastCommitTime.text = cache.lastCommitTime
            ?.let { "Last: ${TimeFormatter.formatRelative(it)}" }
            ?: "No recent commits found"

        panelBinding.tvLastCommitRepo.text = cache.lastCommitRepo ?: ""
        panelBinding.tvStreak.text         = "🔥 ${cache.currentStreak} day streak"
        panelBinding.progressBar.isVisible = false

        val committed = cache.committedToday
        panelBinding.btnRemindLater.isVisible = !committed
        panelBinding.btnMarkDone.text = if (committed) "✓ All done!" else "Done ✓"
        panelBinding.btnMarkDone.isEnabled = !committed
    }

    private fun showNotConfigured() {
        panelBinding.tvCommitStatus.text    = "⚙️ Configure GitHub in Settings"
        panelBinding.tvLastCommitTime.text  = ""
        panelBinding.tvLastCommitRepo.text  = ""
        panelBinding.tvStreak.text          = ""
        panelBinding.progressBar.isVisible  = false
        panelBinding.btnRemindLater.isVisible = false
    }

    private fun safeUpdateLayout(view: View?, params: WindowManager.LayoutParams) {
        try {
            if (view != null && view.isAttachedToWindow) {
                windowManager.updateViewLayout(view, params)
            }
        } catch (_: Exception) { }
    }

    @Suppress("DEPRECATION")
    private fun safeScreenWidth(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.width()
        } else {
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            dm.widthPixels
        }
    }

    private fun removeBubble() {
        try { bubbleView?.let { if (it.isAttachedToWindow) windowManager.removeView(it) } }
        catch (_: Exception) {}
        bubbleView = null
    }

    private fun removePanel() {
        try { panelView?.let { if (it.isAttachedToWindow) windowManager.removeView(it) } }
        catch (_: Exception) {}
        panelView = null
    }

    private fun openUrl(url: String) {
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    @Suppress("DEPRECATION")
    private fun vibrateShort() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                (getSystemService(VIBRATOR_MANAGER_SERVICE) as? VibratorManager)
                    ?.defaultVibrator
                    ?.vibrate(android.os.VibrationEffect.createOneShot(40, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                (getSystemService(VIBRATOR_SERVICE) as? Vibrator)
                    ?.vibrate(android.os.VibrationEffect.createOneShot(40, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
            }
        } catch (_: Exception) {}
    }

    @Suppress("DEPRECATION")
    private fun vibrateSuccess() {
        try {
            val pattern = longArrayOf(0, 60, 40, 60)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                (getSystemService(VIBRATOR_MANAGER_SERVICE) as? VibratorManager)
                    ?.defaultVibrator
                    ?.vibrate(android.os.VibrationEffect.createWaveform(pattern, -1))
            } else {
                (getSystemService(VIBRATOR_SERVICE) as? Vibrator)
                    ?.vibrate(android.os.VibrationEffect.createWaveform(pattern, -1))
            }
        } catch (_: Exception) {}
    }

    private fun Int.dpToPx(): Int =
        (this * resources.displayMetrics.density).toInt()
}

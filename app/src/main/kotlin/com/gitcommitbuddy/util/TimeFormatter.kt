package com.gitcommitbuddy.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Utility for formatting ISO-8601 timestamps into human-readable strings.
 */
object TimeFormatter {

    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val displayFormat = SimpleDateFormat("MMM d, h:mm a", Locale.US)

    /**
     * Converts an ISO-8601 UTC string to a relative description.
     * e.g., "2 hours ago", "Yesterday", "3 days ago"
     */
    fun formatRelative(isoString: String): String {
        return try {
            val date = isoFormat.parse(isoString) ?: return isoString
            val now = System.currentTimeMillis()
            val diffMs = now - date.time

            val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMs)
            val hours   = TimeUnit.MILLISECONDS.toHours(diffMs)
            val days    = TimeUnit.MILLISECONDS.toDays(diffMs)

            when {
                minutes < 1  -> "Just now"
                minutes < 60 -> "$minutes minute${if (minutes == 1L) "" else "s"} ago"
                hours < 24   -> "$hours hour${if (hours == 1L) "" else "s"} ago"
                days == 1L   -> "Yesterday"
                days < 7     -> "$days days ago"
                else         -> displayFormat.format(date)
            }
        } catch (e: Exception) {
            isoString
        }
    }

    /**
     * Formats an ISO-8601 string to "Jan 15, 3:45 PM" style.
     */
    fun formatAbsolute(isoString: String): String {
        return try {
            val date = isoFormat.parse(isoString) ?: return isoString
            displayFormat.format(date)
        } catch (e: Exception) {
            isoString
        }
    }

    /**
     * Returns today's date as "yyyy-MM-dd" in UTC.
     */
    fun todayUtc(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US)
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(Date())

    /**
     * Format hour/minute as "9:00 PM" style.
     */
    fun formatTime(hour: Int, minute: Int): String {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return SimpleDateFormat("h:mm a", Locale.US).format(cal.time)
    }
}

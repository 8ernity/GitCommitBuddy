package com.gitcommitbuddy.util

/**
 * Provides randomised motivational messages for the UI.
 * Keeps the app feeling fresh rather than showing the same text every day.
 */
object MotivationalMessages {

    private val committedMessages = listOf(
        "🎉 Crushed it! Streak secured.",
        "💪 Another day, another commit. You're on fire!",
        "🔥 Consistency is your superpower. Keep going!",
        "✅ Today's goal: achieved. Tomorrow's goal: repeat.",
        "🚀 Ship it! The best code is pushed code.",
        "🌟 Great work! Every commit counts.",
        "⚡ You showed up. That's what matters.",
        "🏆 Streak alive! Your future self is proud."
    )

    private val pendingMessages = listOf(
        "💻 Time to write some code!",
        "🎯 One commit away from keeping the streak alive.",
        "⏳ The day isn't over yet — go ship something!",
        "🌱 Small commits grow into great projects.",
        "🔑 Even a README fix counts. Just push!",
        "📝 A comment, a fix, a feature — anything works.",
        "⚡ 10 minutes of code is all it takes.",
        "🏃 Don't break the chain — commit something today!"
    )

    private val streakMilestoneMessages = mapOf(
        7  to "🔥 7-day streak! One whole week of consistency!",
        14 to "💥 Two weeks strong! You're building a habit!",
        30 to "🏅 30-day streak! You're a coding machine!",
        60 to "🚀 60 days! Legendary consistency!",
        100 to "👑 100 day streak! You are unstoppable!"
    )

    fun getCommittedMessage(): String =
        committedMessages.random()

    fun getPendingMessage(): String =
        pendingMessages.random()

    fun getStreakMilestoneMessage(streak: Int): String? =
        streakMilestoneMessages[streak]

    fun getStreakDescription(streak: Int): String = when {
        streak == 0   -> "No streak yet — start today!"
        streak < 3    -> "🌱 $streak day streak — just getting started!"
        streak < 7    -> "🔥 $streak day streak — building momentum!"
        streak < 14   -> "💪 $streak day streak — solid consistency!"
        streak < 30   -> "⚡ $streak day streak — you're on a roll!"
        streak < 60   -> "🏆 $streak day streak — incredible!"
        else          -> "👑 $streak day streak — LEGENDARY!"
    }
}

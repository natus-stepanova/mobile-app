package org.hyperskill.app.notification.domain.repository

import org.hyperskill.app.notification.data.model.NotificationDescription

interface NotificationRepository {
    fun isNotificationsPermissionGranted(): Boolean
    fun setNotificationsPermissionGranted(isGranted: Boolean)

    fun isDailyStudyRemindersEnabled(): Boolean
    fun setDailyStudyRemindersEnabled(enabled: Boolean)

    fun getNotificationTimestamp(key: String): Long
    fun setNotificationTimestamp(key: String, timestamp: Long)

    fun getDailyStudyRemindersIntervalStartHour(): Int
    fun setDailyStudyRemindersIntervalStartHour(hour: Int)

    fun getRandomDailyStudyRemindersNotificationDescription(): NotificationDescription
    fun getShuffledDailyStudyRemindersNotificationDescriptions(): List<NotificationDescription>

    fun getLastTimeUserAskedToEnableDailyReminders(): Long?
    fun setLastTimeUserAskedToEnableDailyReminders(timestamp: Long)
    fun getUserAskedToEnableDailyRemindersCount(): Long
}
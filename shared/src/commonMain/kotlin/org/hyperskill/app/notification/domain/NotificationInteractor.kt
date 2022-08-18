package org.hyperskill.app.notification.domain

import org.hyperskill.app.notification.data.model.NotificationDescription
import org.hyperskill.app.notification.domain.repository.NotificationRepository

class NotificationInteractor(
    private val notificationRepository: NotificationRepository
) {
    fun isNotificationsEnabled(): Boolean =
        notificationRepository.isNotificationsEnabled()

    fun setNotificationsEnabled(enabled: Boolean) {
        notificationRepository.setNotificationsEnabled(enabled)
    }

    fun getNotificationTimestamp(key: String): Long =
        notificationRepository.getNotificationTimestamp(key)

    fun setNotificationTimestamp(key: String, timestamp: Long) {
        notificationRepository.setNotificationTimestamp(key, timestamp)
    }

    fun getDailyStudyRemindersIntervalStartHour(): Int =
        notificationRepository.getDailyStudyRemindersIntervalStartHour()

    fun setDailyStudyRemindersIntervalStartHour(hour: Int) {
        notificationRepository.setDailyStudyRemindersIntervalStartHour(hour)
    }

    fun getRandomNotificationDescription(): NotificationDescription =
        notificationRepository.getRandomNotificationDescription()

    fun getShuffledNotificationDescriptions(): List<NotificationDescription> =
        notificationRepository.getShuffledNotificationDescriptions()
}
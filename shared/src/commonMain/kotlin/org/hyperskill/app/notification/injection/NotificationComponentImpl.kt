package org.hyperskill.app.notification.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.notification.cache.NotificationCacheDataSourceImpl
import org.hyperskill.app.notification.data.repository.NotificationRepositoryImpl
import org.hyperskill.app.notification.data.source.NotificationCacheDataSource
import org.hyperskill.app.notification.domain.NotificationInteractor
import org.hyperskill.app.notification.domain.repository.NotificationRepository

class NotificationComponentImpl(appGraph: AppGraph) : NotificationComponent {
    private val notificationCacheDataSource: NotificationCacheDataSource =
        NotificationCacheDataSourceImpl(appGraph.commonComponent.settings)

    private val notificationRepository: NotificationRepository =
        NotificationRepositoryImpl(notificationCacheDataSource)

    override val notificationInteractor: NotificationInteractor =
        NotificationInteractor(notificationRepository)
}
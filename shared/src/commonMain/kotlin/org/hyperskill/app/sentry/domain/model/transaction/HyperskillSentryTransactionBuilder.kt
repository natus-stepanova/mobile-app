package org.hyperskill.app.sentry.domain.model.transaction

object HyperskillSentryTransactionBuilder {
    /**
     * AppFeature
     */
    fun buildAppFeatureRemoteLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "app-feature-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * HomeFeature
     */
    fun buildHomeFeatureRemoteLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "home-feature-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )
}
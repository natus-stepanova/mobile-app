package org.hyperskill.app.track.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.track.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress

interface TrackFeature {
    sealed interface State {
        /**
         * Represents initial state.
         */
        object Idle : State

        /**
         * Represents a state when loading track screen data.
         */
        object Loading : State

        /**
         * Represents a state when track screen data successfully loaded.
         *
         * @property track Current user profile selected track.
         * @property trackProgress Current user profile selected track progress.
         * @property studyPlan Current user profile study plan.
         * @property isRefreshing A boolean flag that indicates about is pull-to-refresh is ongoing.
         * @property isLinkLoadingShown A boolean flag that indicates about magic link loading.
         * @see Track
         * @see TrackProgress
         * @see StudyPlan
         */
        data class Content(
            val track: Track,
            val trackProgress: TrackProgress,
            val studyPlan: StudyPlan? = null,
            val isRefreshing: Boolean = false,
            val isLinkLoadingShown: Boolean = false
        ) : State

        /**
         * Represents a state when track screen data failed to load.
         */
        object NetworkError : State
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean = false) : Message

        data class TrackSuccess(
            val track: Track,
            val trackProgress: TrackProgress,
            val studyPlan: StudyPlan? = null
        ) : Message

        object TrackFailure : Message

        object PullToRefresh : Message

        object ClickedContinueInWeb : Message

        data class LinkReceived(val url: String) : Message
        object LinkReceiveFailed : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        object ClickedContinueInWebEventMessage : Message
    }

    sealed interface Action {
        object FetchTrack : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        data class GetLink(val path: HyperskillUrlPath) : Action

        sealed interface ViewAction : Action {
            data class FollowLink(val url: String) : ViewAction
        }
    }
}
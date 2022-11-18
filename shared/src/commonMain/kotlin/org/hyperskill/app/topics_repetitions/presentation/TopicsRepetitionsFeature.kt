package org.hyperskill.app.topics_repetitions.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.topics_repetitions.domain.model.TopicToRepeat
import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitions

interface TopicsRepetitionsFeature {
    sealed interface State {
        object Idle : State
        object Loading : State

        data class Content(
            val topicsRepetitions: TopicsRepetitions,
            val topicsToRepeat: List<TopicToRepeat>,
            val recommendedTopicsToRepeatCount: Int,
            val trackTitle: String,
            val nextTopicsLoading: Boolean = false
        ) : State

        object NetworkError : State
    }

    sealed interface Message {

        data class Initialize(
            val recommendedTopicsToRepeatCount: Int,
            val forceUpdate: Boolean
        ) : Message

        sealed interface TopicsRepetitionsLoaded : Message {
            data class Success(
                val topicsRepetitions: TopicsRepetitions,
                val topicsToRepeat: List<TopicToRepeat>,
                val recommendedTopicsToRepeatCount: Int,
                val trackTitle: String,
            ) : TopicsRepetitionsLoaded

            object Error : TopicsRepetitionsLoaded
        }


        object ShowMoreButtonClicked : Message

        sealed interface NextTopicsLoaded : Message {
            data class Success(
                val remainingTopicsRepetitions: TopicsRepetitions,
                val nextTopicsToRepeat: List<TopicToRepeat>
            ) : NextTopicsLoaded

            object Error : NextTopicsLoaded
        }

        data class TopicRepeated(val topicId: Long) : Message


        /**
         * Analytic
         */
    }

    sealed interface Action {

        data class Initialize(val recommendedTopicsToRepeatCount: Int) : Action

        data class FetchNextTopics(val topicsRepetitions: TopicsRepetitions) : Action

        /**
         * Logging analytic event action
         *
         * @property analyticEvent event to be logged
         */
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            /**
             * Shows snackbar with error message
             */
            object ShowNetworkError : ViewAction
        }
    }
}
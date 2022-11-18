package org.hyperskill.app.topics_repetitions.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Action
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Message
import org.hyperskill.app.topics.domain.interactor.TopicsInteractor
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import org.hyperskill.app.topics_repetitions.domain.model.Repetition
import org.hyperskill.app.topics_repetitions.domain.model.TopicToRepeat
import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitions
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TopicsRepetitionsActionDispatcher(
    config: ActionDispatcherOptions,
    private val topicsRepetitionsInteractor: TopicsRepetitionsInteractor,
    private val topicsInteractor: TopicsInteractor,
    private val profileInteractor: ProfileInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    companion object {
        const val TOPICS_PAGINATION_SIZE = 5
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.Initialize -> {
                val topicsRepetitions = topicsRepetitionsInteractor
                    .getCurrentTrackTopicsRepetitions()
                    .getOrElse {
                        onNewMessage(Message.TopicsRepetitionsLoaded.Error)
                        return
                    }

                val (remainingRepetitions, topicsToRepeat) = loadNextTopics(topicsRepetitions.repetitions)
                    .getOrElse {
                        onNewMessage(Message.TopicsRepetitionsLoaded.Error)
                        return
                    }

                val trackTitle = profileInteractor
                    .getCurrentProfile()
                    .getOrElse {
                        onNewMessage(Message.TopicsRepetitionsLoaded.Error)
                        return
                    }
                    .trackTitle ?: ""

                onNewMessage(
                    Message.TopicsRepetitionsLoaded.Success(
                        topicsRepetitions.copy(repetitions = remainingRepetitions),
                        topicsToRepeat,
                        action.recommendedTopicsToRepeatCount,
                        trackTitle
                    )
                )
            }
            is Action.FetchNextTopics -> {
                val (remainingRepetitions, topicsToRepeat) = loadNextTopics(action.topicsRepetitions.repetitions)
                    .getOrElse {
                        onNewMessage(Message.NextTopicsLoaded.Error)
                        return
                    }

                onNewMessage(
                    Message.NextTopicsLoaded.Success(
                        action.topicsRepetitions.copy(repetitions = remainingRepetitions),
                        topicsToRepeat
                    )
                )
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
        }
    }

    /**
     * Load next TOPICS_PAGINATION_SIZE topics and remove
     * TOPICS_PAGINATION_SIZE repetitions from passed parameter
     *
     * @param repetitions repetitions to load next topics
     * @return
     */
    private suspend fun loadNextTopics(repetitions: List<Repetition>): Result<Pair<List<Repetition>, List<TopicToRepeat>>> {
        val firstRepetitions = repetitions.take(TOPICS_PAGINATION_SIZE)
        val topicsToRepeat = topicsInteractor
            .getTopicsByIds(firstRepetitions.map { it.topicId })
            .getOrElse {
                return Result.failure(it)
            }
            .zip(firstRepetitions)
            .map { (topic, repetition) ->
                TopicToRepeat(topic.id, topic.title, repetition.steps.first(), topic.theory)
            }
        return Result.success(Pair(repetitions.drop(TOPICS_PAGINATION_SIZE), topicsToRepeat))
    }
}
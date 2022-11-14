package org.hyperskill.app.onboarding.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.onboarding.presentation.OnboardingActionDispatcher
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Action
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Message
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.State
import org.hyperskill.app.onboarding.presentation.OnboardingReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object OnboardingFeatureBuilder {
    fun build(
        onboardingInteractor: OnboardingInteractor,
        analyticInteractor: AnalyticInteractor
    ): Feature<State, Message, Action> {
        val onboardingReducer = OnboardingReducer()

        val onboardingActionDispatcher = OnboardingActionDispatcher(
            ActionDispatcherOptions(),
            onboardingInteractor,
            analyticInteractor
        )

        return ReduxFeature(State.Content, onboardingReducer)
            .wrapWithActionDispatcher(onboardingActionDispatcher)
    }
}
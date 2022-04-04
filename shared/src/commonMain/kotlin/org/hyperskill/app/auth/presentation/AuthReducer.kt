package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.presentation.AuthFeature.Action
import org.hyperskill.app.auth.presentation.AuthFeature.Message
import org.hyperskill.app.auth.presentation.AuthFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class AuthReducer : StateReducer<State, Message, Action> {
    override fun reduce(
        state: State,
        message: Message
    ): Pair<State, Set<Action>> =
        when (message) {
            is Message.AuthWithGoogle ->
                if (state is State.Idle || state is State.Error) {
                    State.Loading to setOf(Action.AuthWithGoogle(message.accessToken))
                } else {
                    null
                }
            is Message.AuthSuccess ->
                State.Authenticated(message.accessToken) to setOf(Action.ViewAction.NavigateToHomeScreen)
            is Message.AuthError ->
                State.Error to setOf(Action.ViewAction.ShowAuthError(message.errorMsg))
        } ?: state to emptySet()
}
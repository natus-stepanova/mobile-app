package org.hyperskill.app.auth.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Message

class AuthSocialWebViewActionDispatcher(
    config: ActionDispatcherOptions,
) : CoroutineActionDispatcher<Action, Message>(config.createConfig())
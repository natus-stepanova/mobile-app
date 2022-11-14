package org.hyperskill.app.android.placeholder_new_user.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.navigation.AuthScreen
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentPlaceholderNewUserScreenBinding
import org.hyperskill.app.core.domain.url.HyperskillUrlBuilder
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserViewModel
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class PlaceholderNewUserFragment :
    Fragment(R.layout.fragment_placeholder_new_user_screen),
    ReduxView<PlaceholderNewUserFeature.State, PlaceholderNewUserFeature.Action.ViewAction> {
    companion object {
        fun newInstance(): Fragment =
            PlaceholderNewUserFragment()
    }

    private val viewBinding: FragmentPlaceholderNewUserScreenBinding by viewBinding(
        FragmentPlaceholderNewUserScreenBinding::bind
    )

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val placeholderNewUserViewModel: PlaceholderNewUserViewModel by reduxViewModel(this) { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    private fun injectComponents() {
        val placeholderNewUserComponent = HyperskillApp.graph().buildPlaceholderNewUserComponent()
        val platformPlaceholderNewUserComponent =
            HyperskillApp.graph().buildPlatformPlaceholderNewUserComponent(placeholderNewUserComponent)

        viewModelFactory = platformPlaceholderNewUserComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.placeholderContinueToHyperskillButton.setOnClickListener {
            placeholderNewUserViewModel.onNewMessage(PlaceholderNewUserFeature.Message.ClickedContinueEventMessage)

            val intent = Intent(Intent.ACTION_VIEW)
            val url = HyperskillUrlBuilder.build(HyperskillUrlPath.Index())
            intent.data = Uri.parse(url.toString())

            startActivity(intent)
        }

        viewBinding.placeholderSignInButton.setOnClickListener {
            placeholderNewUserViewModel.onNewMessage(PlaceholderNewUserFeature.Message.PlaceholderSignInTappedMessage)
        }

        placeholderNewUserViewModel.onNewMessage(PlaceholderNewUserFeature.Message.ViewedEventMessage)
    }

    override fun onAction(action: PlaceholderNewUserFeature.Action.ViewAction) {
        when (action) {
            PlaceholderNewUserFeature.Action.ViewAction.NavigateTo.AuthScreen ->
                requireRouter().newRootScreen(AuthScreen)
        }
    }

    override fun render(state: PlaceholderNewUserFeature.State) {
        // no op
    }
}
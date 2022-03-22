package org.hyperskill.app.android.auth.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentAuthSocialBinding
import org.hyperskill.app.auth.presentation.AuthFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.app.presentation.redux.container.ReduxView

class AuthSocialFragment
    : Fragment(R.layout.fragment_auth_social),
    ReduxView<AuthFeature.State, AuthFeature.Action.ViewAction>
{

    private lateinit var viewBinding: FragmentAuthSocialBinding
    private lateinit var viewStateDelegate: ViewStateDelegate<AuthFeature.State>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewStateDelegate = ViewStateDelegate()
        viewStateDelegate.addState<AuthFeature.State.Idle>()
        viewStateDelegate.addState<AuthFeature.State.Loading>(viewBinding.signInSocialProgressIndicator)
    }

    override fun onAction(action: AuthFeature.Action.ViewAction) {}
    override fun render(state: AuthFeature.State) {
        if (state is AuthFeature.State.NetworkError) {
            // TODO: add text and action depending on error
            Snackbar.make(requireView(), "Login error", Snackbar.LENGTH_LONG)
                .setAction("Retry") {
                }
                .show()
        }
    }
}

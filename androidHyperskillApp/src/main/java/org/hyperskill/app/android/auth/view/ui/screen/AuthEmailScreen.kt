package org.hyperskill.app.android.auth.view.ui.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.auth.view.ui.fragment.AuthCredentialsFragment

object AuthEmailScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        AuthCredentialsFragment.newInstance()
}
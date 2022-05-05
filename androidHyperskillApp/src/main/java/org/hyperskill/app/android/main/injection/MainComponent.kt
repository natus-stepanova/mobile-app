package org.hyperskill.app.android.main.injection

import dagger.Subcomponent
import org.hyperskill.app.android.auth.injection.AuthDataModule
import org.hyperskill.app.android.main.view.ui.activity.MainActivity

@Subcomponent(
    modules = [
        MainModule::class,
        AuthDataModule::class
    ]
)
interface MainComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): MainComponent
    }

    fun inject(mainActivity: MainActivity)
}
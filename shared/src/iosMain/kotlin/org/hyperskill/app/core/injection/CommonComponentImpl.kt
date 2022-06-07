package org.hyperskill.app.core.injection

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.ResourceProviderImpl
import org.hyperskill.app.network.injection.NetworkModule
import platform.Foundation.NSUserDefaults

class CommonComponentImpl(
    override val userAgentInfo: UserAgentInfo
) : CommonComponent {

    override val json: Json =
        NetworkModule.provideJson()

    override val settings: Settings =
        AppleSettings(NSUserDefaults.standardUserDefaults)

    override val resourceProvider: ResourceProvider =
        ResourceProviderImpl()
}
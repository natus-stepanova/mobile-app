package org.hyperskill.app.core.injection

import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.Platform
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.DateFormatter

interface CommonComponent {
    val json: Json
    val userAgentInfo: UserAgentInfo
    val settings: Settings
    val resourceProvider: ResourceProvider
    val dateFormatter: DateFormatter
    val platform: Platform
    val buildKonfig: BuildKonfig
}
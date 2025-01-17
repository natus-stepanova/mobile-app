package org.hyperskill.app.core.domain

import platform.UIKit.UIDevice

actual class Platform actual constructor() {
    actual val platform: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    actual val isIos: Boolean = true
    actual val isAndroid: Boolean = false

    actual val analyticName: String = "ios"

    actual val feedbackName: String = "iOS"
}
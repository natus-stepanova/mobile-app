import org.jetbrains.kotlin.konan.properties.loadProperties
import org.jetbrains.kotlin.konan.properties.propertyString
import com.android.build.api.dsl.ApplicationBuildType

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
    alias(libs.plugins.sentry.androidGradle)
    alias(libs.plugins.firebase.appdistribution.gradle)
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.android.ui.material)
    implementation(libs.android.ui.appcompat)
    implementation(libs.android.ui.constraintlayout)
    implementation(libs.android.ui.core.ktx)
    implementation(libs.android.ui.fragment)
    implementation(libs.android.ui.fragment.ktx)
    implementation(libs.android.lifecycle.runtime)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    implementation(libs.kit.view.ui)
    implementation(libs.kit.view.injection)
    implementation(libs.kit.view.redux)
    implementation(libs.kit.view.navigation)

    implementation(libs.bundles.ktor.common)

    implementation(libs.gms.services)
    implementation(libs.gms.login)
    implementation(libs.viewbinding)
    implementation(libs.kit.ui.adapters)

    testImplementation(libs.bundles.android.test)

    ktlintRuleset(libs.ktlintRules)

    implementation(libs.android.material.progress.bar)
    implementation(libs.android.sentry)
    implementation(libs.android.sentry.fragment)
    implementation(libs.android.parcelable)

    debugImplementation(libs.android.flipper)
    debugImplementation(libs.android.soloader)
    releaseImplementation(libs.android.flipper.noop)

    implementation(libs.android.reactivex.rxandroid)
    implementation(libs.android.reactivex.rxjava)
    implementation(libs.android.reactivex.rxkotlin)
    implementation(libs.android.coil)
    implementation(libs.android.coil.svg)
    implementation(libs.android.splashscreen)
    implementation(libs.android.timepicker)
}

android {
    compileSdk = appVersions.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.hyperskill.app.android"
        minSdk = appVersions.versions.minSdk.get().toInt()
        targetSdk = appVersions.versions.targetSdk.get().toInt()
        versionCode = appVersions.versions.versionCode.get().toInt()
        versionName = appVersions.versions.versionName.get()
    }

    signingConfigs {
        getByName("debug") {
            if (SystemProperties.isCI()) return@getByName
            val properties = loadProperties("${project.rootDir}/androidHyperskillApp/keys/debug_keystore.properties")

            storeFile = file("../buildsystem/certs/debug.keystore")
            storePassword = properties.getProperty("HYPERSKILL_DEBUG_STORE_PASSWORD")
            keyAlias = properties.getProperty("HYPERSKILL_DEBUG_KEY_ALIAS")
            keyPassword = properties.getProperty("HYPERSKILL_DEBUG_KEY_PASSWORD")

            enableV3Signing = true
            enableV4Signing = true
        }

        create("release") {
            if (SystemProperties.isCI()) return@create

            val keystorePath = SystemProperties.get(project, "HYPERSKILL_KEYSTORE_PATH")
            if (keystorePath.isNullOrBlank()) return@create

            storeFile = file(keystorePath)
            storePassword = SystemProperties.get(project, "HYPERSKILL_RELEASE_STORE_PASSWORD")
            keyAlias = SystemProperties.get(project, "HYPERSKILL_RELEASE_KEY_ALIAS")
            keyPassword = SystemProperties.get(project, "HYPERSKILL_RELEASE_KEY_PASSWORD")

            enableV3Signing = true
            enableV4Signing = true
        }
    }

    buildTypes {
        fun applyFlavorConfigsFromFile(applicationBuildType: ApplicationBuildType) {
            if (SystemProperties.isCI()) return
            val properties = loadProperties("${project.rootDir}/androidHyperskillApp/keys/${applicationBuildType.name}.properties")
            properties.keys.forEach { name ->
                name as String
                applicationBuildType.buildConfigField(
                    type = "String",
                    name = name,
                    value = requireNotNull(System.getenv(name) ?: properties.propertyString(name))
                )
            }
        }

        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            applyFlavorConfigsFromFile(this)
            firebaseAppDistribution(
                configure = {
                    artifactType = "APK"
                    groups = "all-android-testers"
                    releaseNotesFile = "gradle/release-notes.txt"
                }
            )
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

ktlint {
    filter {
        exclude { element -> element.file.path.contains("build/") }
    }
}
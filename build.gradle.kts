buildscript {
    repositories {
        google()
    }
    dependencies {
        val navVersion = "2.7.7" // Match the version from your libs.versions.toml
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.navigation.safeargs.kotlin) apply false
}

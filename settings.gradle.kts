// settings.gradle.kts — FINAL FIX

pluginManagement {
    repositories {
<<<<<<< HEAD
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
=======
        gradlePluginPortal()   // MUST come first for KSP
        google()
        mavenCentral()
>>>>>>> f169118 (Save my local changes)
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
}

rootProject.name = "FutureScript"
include(":app")

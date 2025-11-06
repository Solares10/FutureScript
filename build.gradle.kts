plugins {
    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false

    // ✅ Safe Args plugin for Kotlin DSL (no classpath here)
    id("androidx.navigation.safeargs.kotlin") version "2.8.3" apply false
}

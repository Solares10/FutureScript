plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}



android {
    namespace = "com.example.futurescript"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.futurescript"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
<<<<<<< HEAD

=======
>>>>>>> f169118 (Save my local changes)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
<<<<<<< HEAD
        // ✅ Fix: allows using java.time.* APIs like Instant.now()
        isCoreLibraryDesugaringEnabled = true
=======
>>>>>>> f169118 (Save my local changes)
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

<<<<<<< HEAD
    buildFeatures {
        viewBinding = true
    }

    lint {
        abortOnError = false // optional: allows build to continue despite warnings
    }
=======
    buildFeatures { viewBinding = true }
>>>>>>> f169118 (Save my local changes)

    packaging {
        resources.excludes.add("META-INF/LICENSE*")
        resources.excludes.add("META-INF/NOTICE*")
    }
}

dependencies {
<<<<<<< HEAD
    // AndroidX core
=======
>>>>>>> f169118 (Save my local changes)
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
<<<<<<< HEAD
// WorkManager for background jobs and CoroutineWorker
implementation("androidx.work:work-runtime-ktx:2.9.1")

=======
>>>>>>> f169118 (Save my local changes)

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.3")

<<<<<<< HEAD
    // Room (KSP)
  // Room (with kapt)
implementation("androidx.room:room-runtime:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")


    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // ✅ Fix: desugaring library for java.time.* support
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")

    // Testing
=======
    // Room + KSP
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
   
    implementation("androidx.room:room-ktx:2.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

>>>>>>> f169118 (Save my local changes)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

java {
<<<<<<< HEAD
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
=======
    toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
>>>>>>> f169118 (Save my local changes)
}

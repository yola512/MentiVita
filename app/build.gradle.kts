import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // necessary for type-safe navigation
    alias(libs.plugins.kotlin.serialization)
    // ksp
    // "If you’re working on a Kotlin-first Android project,
    // it’s generally recommended to use KSP over KAPT.
    // Libraries like Room have added KSP support, making it a great choice for projects
    // that need faster builds and better annotation processing."
    // "Use KSP when you’re working with Kotlin-first libraries
    // like Room, Moshi, and AutoValue. KSP offers better performance and faster build times."
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.alzapp"
    compileSdk = 35

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    defaultConfig {
        applicationId = "com.example.alzapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // src: https://developer.android.com/topic/performance/app-optimization/enable-app-optimization#unused-alt-resources
            // enable those in the final (market) version of the app
            isMinifyEnabled = false // "true" removes unused code (code optimisation)
            isShrinkResources =
                false // = "true" automatically identifies  unused library resources and removes them (resource optimisation)
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            javaParameters.set(true)
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation("com.google.android.material:material:1.12.0")
    implementation(libs.androidx.compose.material.core)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)

    // Media3 for video
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    // type-safe navigation
    implementation(libs.androidx.navigation)
    implementation(libs.kotlinx.serialization)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.material3)
    implementation(libs.cronet.embedded)
    implementation(libs.androidx.ui)
    implementation(libs.ui)
    ksp(libs.androidx.room.compiler)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // coil
    implementation(libs.coil.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

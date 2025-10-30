import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.logickoder.newshub"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.logickoder.newshub"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.set(
                freeCompilerArgs.get() + listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
            )
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugarjdklibs)

    implementation(libs.core)
    implementation(libs.appcompat)
    implementation(libs.material)

    // Activity
    implementation(libs.activity)
    implementation(libs.activity.compose)

    // Coil
    implementation(libs.coil)
    implementation(libs.coil.network)

    // Compose
    implementation(platform(libs.compose))
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
    // Compose tooling support (Previews, etc.)
    debugImplementation(libs.compose.custom.view)
    debugImplementation(libs.compose.custom.view.pooling)

    // DataStore
    implementation(libs.datastore)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.compiler.android)

    // Kotlin
    implementation(libs.kotlin.immutable)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.serialization)

    // Lifecycle
    implementation(libs.lifecycle.compose)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.compose)

    // Napier
    implementation(libs.napier)

    // Navigation
    implementation(libs.navigation.runtime)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.viewmodel)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)

    // Room
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
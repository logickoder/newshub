import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    id("jacoco")
}

val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        localFile.inputStream().use { load(it) }
    }
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

        buildConfigField(
            "String",
            "NEWS_API_KEY",
            "\"${localProperties.getProperty("NEWS_API_KEY").orEmpty()}\""
        )
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = true
            enableUnitTestCoverage = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
            enableUnitTestCoverage = true
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

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testReleaseUnitTest")

    reports {
        xml.required.set(true)
        csv.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/data/remote/dto/*",
        "**/di/*"
    )

    val javaTree = fileTree(layout.buildDirectory.dir("intermediates/javac/release/classes")) {
        exclude(fileFilter)
    }
    val kotlinTree = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/release")) {
        exclude(fileFilter)
    }

    classDirectories.setFrom(files(javaTree, kotlinTree))
    sourceDirectories.setFrom(
        files(
            "$projectDir/src/main/java",
            "$projectDir/src/main/kotlin"
        )
    )
    executionData.setFrom(fileTree(layout.buildDirectory) {
        include("**/*.exec", "**/*.ec")
    })
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

    // CustomTabs
    implementation(libs.customtabs)

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

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
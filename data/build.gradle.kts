import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

val keysProperties = Properties().apply {
    val file = rootProject.file("keys.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        resValue("string", "GOOGLE_WEB_CLIENT_ID", keysProperties.getProperty("GOOGLE_WEB_CLIENT_ID", ""))
        resValue("string", "NAVER_CLIENT_ID", keysProperties.getProperty("NAVER_CLIENT_ID", ""))
        resValue("string", "NAVER_CLIENT_SECRET", keysProperties.getProperty("NAVER_CLIENT_SECRET", ""))

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
//    kotlinOptions {
//        jvmTarget = JavaVersion.VERSION_1_8.toString()
//    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

dependencies {
    implementation(project(mapOf("path" to ":domain")))

    implementation(libs.hilt.android)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // build.gradle.kts
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)

    // Google Sign in
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // naver sign in
    implementation(libs.oauth)

    implementation(libs.gson)
}
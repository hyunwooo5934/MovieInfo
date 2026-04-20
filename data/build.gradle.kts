import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

// ✅ 수정: CI 환경에서 local.properties 파일이 없어도 정상 작동
val localProperties = Properties().apply {
    val propertiesFile = rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        load(propertiesFile.inputStream())
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

        // ✅ 수정: 기본값 설정 (파일이 없으면 빈 문자열 사용)
        buildConfigField(
            "String",
            "GOOGLE_WEB_CLIENT_ID",
            "\"${localProperties.getProperty("GOOGLE_WEB_CLIENT_ID", "")}\""
        )

        buildConfigField(
            "String",
            "NAVER_CLIENT_ID",
            "\"${localProperties.getProperty("NAVER_CLIENT_ID", "")}\""
        )
        buildConfigField(
            "String",
            "NAVER_CLIENT_SECRET",
            "\"${localProperties.getProperty("NAVER_CLIENT_SECRET", "")}\""
        )

        buildConfigField(
            "String",
            "KAKAO_CLIENT_ID",
            "\"${localProperties.getProperty("KAKAO_CLIENT_ID", "")}\""
        )
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

    buildFeatures {
        buildConfig = true
    }
    kotlinOptions {
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

dependencies {
    implementation(project(mapOf("path" to ":domain")))

    implementation(libs.hilt.android)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // datastore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)

    // Google Sign in
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // naver sign in
    implementation(libs.oauth)
    implementation(libs.gson)

    // kakao sign in
    implementation(libs.v2.all)
    implementation(libs.v2.user)
    implementation(libs.v2.share)
    implementation(libs.v2.talk)
    implementation(libs.v2.friend)
    implementation(libs.v2.navi)
    implementation(libs.v2.cert)
}

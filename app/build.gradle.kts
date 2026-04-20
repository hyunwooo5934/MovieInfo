import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import java.util.Base64 as JavaBase64

val localProperties = Properties().apply {
    if (rootProject.file("local.properties").exists()) {
        load(rootProject.file("local.properties").inputStream())
    }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    id("com.google.gms.google-services")
    alias(libs.plugins.sonarqube)
    id("jacoco")
}

android {
    namespace = "com.example.movieinfo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.movieinfo"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "KAKAO_CLIENT_ID",
            "\"${localProperties["KAKAO_CLIENT_ID"]}\""
        )
    }

    // ✅ signingConfigs를 먼저 정의 (buildTypes 전에)
    signingConfigs {
        // ✅ 로컬 개발용 release signing config
        if (rootProject.file("local.properties").exists()) {
            val keyStorePath = localProperties["KEYSTORE_PATH"]?.toString()
            if (!keyStorePath.isNullOrEmpty()) {
                create("release") {
                    storeFile = file(keyStorePath)
                    storePassword = localProperties["KEYSTORE_PASSWORD"]?.toString()
                    keyAlias = localProperties["KEY_ALIAS"]?.toString()
                    keyPassword = localProperties["KEY_PASSWORD"]?.toString()
                }
            }
        }

        // ✅ CI 환경용 release signing config
        if (System.getenv("CI") == "true" && !System.getenv("KEYSTORE_FILE").isNullOrEmpty()) {
            create("release") {
                storeFile = file("${buildDir}/keystore.jks")
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // ✅ signingConfig는 정의된 후에 참조
            if (signingConfigs.findByName("release") != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
        disable.addAll(listOf(
            "MissingTranslation",
            "ExtraTranslation"
        ))
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

dependencies {
    implementation(project(mapOf("path" to ":data")))
    implementation(project(mapOf("path" to ":domain")))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.protolite.well.known.types)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.assertj:assertj-core:3.24.1")

    // Instrumentation Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestUtil("androidx.test:orchestrator:1.4.2")

    // Compose / Lifecycle / Hilt
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Google Sign in
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // naver sign in
    implementation(libs.oauth)

    // kakao sign in
    implementation(libs.v2.all)
    implementation(libs.v2.user)
    implementation(libs.v2.share)
    implementation(libs.v2.talk)
    implementation(libs.v2.friend)
    implementation(libs.v2.navi)
    implementation(libs.v2.cert)

    // DataStore
    implementation(libs.androidx.datastore.preferences)
}

// ✅ CI 환경에서 Keystore 파일 복원
if (System.getenv("CI") == "true" && !System.getenv("KEYSTORE_FILE").isNullOrEmpty()) {
    tasks.register("restoreKeystore") {
        doFirst {
            val keystoreBase64 = System.getenv("KEYSTORE_FILE")
            if (!keystoreBase64.isNullOrEmpty()) {
                try {
                    val decodedKeystore = JavaBase64.getDecoder()
                        .decode(keystoreBase64.toByteArray())
                    file("${buildDir}/keystore.jks").writeBytes(decodedKeystore)
                    println("✅ Keystore 파일 복원 완료")
                } catch (e: Exception) {
                    println("❌ Keystore 복원 실패: ${e.message}")
                    throw e
                }
            }
        }
    }

    tasks.named("preBuild") {
        dependsOn("restoreKeystore")
    }
}

// ✅ SonarQube 설정
sonarqube {
    properties {
        property("sonar.projectKey", "movieinfo")
        property("sonar.projectName", "MovieInfo")
        property("sonar.sources", "src/main/java,src/main/kotlin")
        property("sonar.exclusions", """
            **/*Test.java,
            **/*Tests.java,
            **/R.java,
            **/BuildConfig.java,
            **/Manifest*.java,
            **/*Test.kt,
            **/*Tests.kt
        """.trimIndent())
        property("sonar.coverage.jacoco.xmlReportPaths",
            "${buildDir}/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
        property("sonar.java.binaries", "${buildDir}/intermediates/javac/debug")
    }
}

// ✅ Jacoco 설정
jacoco {
    toolVersion = "0.8.10"
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    group = "Verification"
    description = "Generate Jacoco coverage reports"

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    val excludes = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Activity*.*",
        "**/*Fragment*.*",
        "**/*Dialog*.*",
        "**/*Adapter*.*",
        "**/Lambda$*.class",
        "**/Lambda.class",
        "**/-Init.class"
    )

    classDirectories.setFrom(
        fileTree("${buildDir}/intermediates/javac/debug") {
            exclude(excludes)
        }
    )
    sourceDirectories.setFrom(files(
        "${project.projectDir}/src/main/java",
        "${project.projectDir}/src/main/kotlin"
    ))
    executionData.setFrom(files("${buildDir}/jacoco/testDebugUnitTest.exec"))
}

// ✅ SonarQube가 Jacoco 리포트에 의존
tasks.named("sonarqube") {
    dependsOn("jacocoTestReport")
}

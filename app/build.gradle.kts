plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("jacoco")
}

android {
    namespace = "app.test.demochat"
    compileSdk = 35

    defaultConfig {
        applicationId = "app.test.demochat"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        
        buildConfigField("String", "BASE_URL", "\"https://plannerok.ru/\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            enableUnitTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}


// Simplified JaCoCo Report Task
tasks.register("testDebugUnitTestCoverage", JacocoReport::class) {
    dependsOn("testDebugUnitTest")
    group = "Reporting"
    description = "Generate Jacoco code coverage reports for the debug build."

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        // Android-specific files
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        // Generated data binding
        "android/databinding/**/*.class",
        // Dagger, Koin, etc.
        "**/*_MembersInjector.class",
        "**/Dagger*Component.class",
        "**/*_Factory.class",
        "**/*_Provide*Factory*.*"
    )

    // Specifies the directories with the compiled class files.
    // This path is correct for recent AGP versions.
    val classesDir = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }

    // Specifies the source code directories
    val sourceDir = files("${project.projectDir}/src/main/kotlin")

    classDirectories.setFrom(classesDir)
    sourceDirectories.setFrom(sourceDir)
    // Location of the execution data from the test task
    executionData.setFrom(file("${buildDir}/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"))
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.coil.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
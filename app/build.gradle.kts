import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    namespace = AppConfig.namespace
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_BASE_URL", "\"https://api.coingecko.com/api/v3/\"")
    }

    buildTypes {
        getByName("debug") {
            enableUnitTestCoverage = true
        }

        getByName("release") {
            enableUnitTestCoverage = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    flavorDimensions.add("main")
    productFlavors {
        create("dev") {
            dimension = "main"
            applicationIdSuffix = ".dev"
        }

        create("qa") {
            dimension = "main"
            versionNameSuffix = ".qa"
        }

        create("prod") {
            dimension = "main"
        }
    }

    lint {
        ignoreTestSources = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    val kotlinVersion = "1.9.10"
    val lifecycleVersion = "2.2.0"
    val koinVersion = "3.4.0"
    val coroutinesVersion = "1.7.3"
    val glideVersion = "4.14.2"
    val navigationVersion = "2.7.6"
    val detektVersion = "1.23.3"

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    // Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Koin
    implementation("io.insert-koin:koin-android:$koinVersion")

    // Navigation
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    // Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")
    implementation("jp.wasabeef:glide-transformations:4.3.0")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Detekt formatting plugin
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")

    // Tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.insert-koin:koin-test-junit5:$koinVersion")
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
}

detekt {
    config.setFrom(project.file("$rootDir/app/detekt-config.yml"))
    parallel = true
    buildUponDefaultConfig = true
    autoCorrect = true
    basePath = projectDir.absolutePath
}

ktlint {
    android.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")

    reporters {
        reporter(ReporterType.PLAIN)
    }
    filter {
        exclude("**/generated/**")
        include("**/java/**")
        include("**/kotlin/**")
        include("**/test/**")
    }
}

val runAllChecks by tasks.registering {
    dependsOn("detekt")
    dependsOn("ktlintCheck")
    dependsOn("test")
    dependsOn("lint")

    description = "Runs tests, detekt, ktlint and lint checks as single task"
    group = "Verification"
}

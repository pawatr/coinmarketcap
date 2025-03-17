import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
}

android {
    namespace = "com.pawatr.coinmarketcap"
    compileSdk = 35

    val environmentProperties = Properties().apply {
        load(project.rootProject.file("environment.properties").inputStream())
    }

    defaultConfig {
        applicationId = "com.pawatr.coinmarketcap"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        println("HOST_NAME: ${environmentProperties.getProperty("hostName")}")
        println("API_KEY: ${environmentProperties.getProperty("apiKey")}")

        buildConfigField("String","HOST_NAME", environmentProperties.getProperty("hostName"))
        buildConfigField("String","API_KEY", environmentProperties.getProperty("apiKey"))
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // ViewModel และ LiveData
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp3.logging)

    // koin
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    // paging 3
    implementation(libs.paging)

    // coil
    implementation(libs.coil)
    implementation(libs.coil.svg)

    // swipe refresh
    implementation(libs.swiperefreshlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
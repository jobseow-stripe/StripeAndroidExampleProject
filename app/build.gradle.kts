import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

// Load secrets from secrets.properties
val localProperties =  Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

android {
    namespace = "com.example.stripeandroidexampleproject"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.stripeandroidexampleproject"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Add Stripe publishable key to BuildConfig
        buildConfigField("String", "STRIPE_PUBLISHABLE_KEY",
            "\"${localProperties.getProperty("STRIPE_PUBLISHABLE_KEY", "")}\"")
        buildConfigField("String", "ACCOUNT_ID",
            "\"${localProperties.getProperty("ACCOUNT_ID", "")}\"")
        buildConfigField("String", "CUSTOMER_ID",
            "\"${localProperties.getProperty("CUSTOMER_ID", "")}\"")
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
        compose = true
        buildConfig = true
    }
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
    implementation(libs.androidx.appcompat)
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.play.services.wallet)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Stripe
    implementation("com.stripe:stripe-android:22.2.0")
    implementation("com.stripe:financial-connections:22.2.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}
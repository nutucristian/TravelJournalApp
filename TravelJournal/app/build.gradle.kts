plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    namespace = "com.example.traveljournal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.traveljournal"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.activity)
    val kotlin_version = "1.8.10"
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Compose dependencies
    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.3")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.gms:play-services-base:17.6.0")
    implementation("com.google.android.gms:play-services-vision:20.1.3")
    implementation("com.google.android.gms:play-services-base:17.6.0")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("com.google.android.gms:play-services-location:19.0.1")
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("com.google.android.gms:play-services-places:17.0.0")
    implementation("com.google.android.libraries.places:places:2.5.0")
    implementation("com.google.android.gms:play-services-maps:17.0.0")
    implementation("com.google.android.gms:play-services-location:19.0.1")

    // ViewModel dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
}

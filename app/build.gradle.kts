plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    buildFeatures {
        viewBinding = true
    }

    namespace = "com.example.apitest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.apitest"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.opencsv:opencsv:5.6")
    implementation("androidx.test:core:1.5.0-rc01")
    implementation("com.otaliastudios:zoomlayout:1.9.0")

    // Gson 컨버터 추가 (주석 처리)
    //implementation("com.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    implementation(kotlin("script-runtime"))

    // Firebase BoM (Bill of Materials)
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))

    // Firebase Analytics (BoM으로 관리되므로 버전 생략)
    implementation("com.google.firebase:firebase-analytics")

    // Firebase Realtime Database 의존성 (BoM으로 관리되므로 버전 생략)
    implementation("com.google.firebase:firebase-database")

    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
}

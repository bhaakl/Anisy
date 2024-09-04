plugins {
    id("com.android.library")
//    id("org.jetbrains.kotlin.android")
    kotlin("android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
    // Hilt
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.bhaakl.anisy.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 34


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(project(":domain"))

    // Retrofit / OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
//    implementation "com.squareup.retrofit2:converter-gson:2.9.0"
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-ktx:$room_version")
    api("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    // Hilt
    val hilt_version = "2.52"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    ksp("com.google.dagger:hilt-android-compiler:$hilt_version")

    // Paging
    val paging_version = "3.3.2"
    api("androidx.paging:paging-runtime-ktx:$paging_version")

}
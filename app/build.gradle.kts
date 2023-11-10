plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.ways.themoviedb"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ways.themoviedb"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
        buildConfigField(
            "String",
            "TOKEN",
            "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwMGUyMjBkMDg4NTc0Mzg4MWI2ZjFlYTlhOGNmNTJmMiIsInN1YiI6IjVmM2EwODAyMmQxZTQwMDAzNDk4NjAxNCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.2vDkGJoWstMVlmG6oElJVzx2W7r924_pFI0p3LZEYuM\""
        )
        buildConfigField("String", "BASE_IMG_URL", "\"https://image.tmdb.org/t/p/original\"")
        buildConfigField("String", "BASE_YOUTUBE_URL", "\"https://www.youtube.com/watch?v=\"")
        buildConfigField("String", "BASE_YOUTUBE_THUMBNAIL_URL", "\"https://img.youtube.com/vi/\"")
        buildConfigField("String", "BASE_YOUTUBE_THUMBNAIL_URL_ENDPOINT", "\"/sddefault.jpg\"")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    android.buildFeatures.buildConfig = true
    android.buildFeatures.viewBinding = true
}

dependencies {

    val coroutineAndroidVersion = "1.7.3"
    val coroutineCoreVersion = "1.7.3"
    val daggerHiltVersion = "2.48"
    val datastoreVersion = "1.1.0-alpha06"
    val fragmentKtxVersion = "1.6.2"
    val kotlinCoroutineAdapter = "0.9.2"
    val lifecycleVersion = "2.6.2"
    val loggingInterceptorVersion = "5.0.0-alpha.7"
    val lottieVersion = "6.1.0"
    val navVersion = "2.7.5"
    val pagingVersion = "3.2.1"
    val retrofitVersion = "2.9.0"
    val roomVersion = "2.6.0"
    val timberVersion = "5.0.1"
    val videoPlayerVersion = "12.1.0"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:5.0.0-alpha.7")
    testImplementation("com.google.truth:truth:1.1.3")

    //view
    implementation("com.airbnb.android:lottie:${lottieVersion}")
    implementation("com.squareup.picasso:picasso:2.5.2")

    //logging
    implementation("com.jakewharton.timber:timber:${timberVersion}")

    //kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${coroutineAndroidVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutineCoreVersion}")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${kotlinCoroutineAdapter}")

    //lifecycle component
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${lifecycleVersion}")

    //fragment ktx
    implementation("androidx.fragment:fragment-ktx:${fragmentKtxVersion}")
    implementation("androidx.navigation:navigation-fragment-ktx:${navVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:${navVersion}")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:${retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${retrofitVersion}")

    //datastore
    implementation("androidx.datastore:datastore-preferences:${datastoreVersion}")

    //room database
    implementation("androidx.room:room-runtime:${roomVersion}")
    ksp("androidx.room:room-compiler:${roomVersion}")
    implementation("androidx.room:room-ktx:${roomVersion}")

    //logging interceptors
    implementation("com.squareup.okhttp3:logging-interceptor:${loggingInterceptorVersion}")

    //dagger hilt
    implementation("com.google.dagger:hilt-android:${daggerHiltVersion}")
    ksp("com.google.dagger:hilt-compiler:${daggerHiltVersion}")

    //paging
    implementation("androidx.paging:paging-runtime-ktx:${pagingVersion}")

    //video
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:${videoPlayerVersion}")
}
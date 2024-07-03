plugins {
    id("com.android.application")
}

android {
    namespace = "com.vtdglobal.liedetector"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vtdglobal.liedetector"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("me.relex:circleindicator:2.1.6")

    implementation ("androidx.camera:camera-core:1.2.0-beta01")
    implementation ("androidx.camera:camera-camera2:1.2.0-beta01")
    implementation ("androidx.camera:camera-lifecycle:1.2.0-beta01")
    implementation ("androidx.camera:camera-view:1.2.0-beta01")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.23")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("androidx.cardview:cardview:1.0.0")
//    implementation ("com.afollestad.material-dialogs:core:0.9.6.0")


}
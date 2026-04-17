plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.pavlusha.landmarksapp"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.pavlusha.landmarksapp"
        minSdk = 24
        targetSdk = 36
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        mlModelBinding = true
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))

//    implementation(libs.tensorflow.lite)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
//    implementation(libs.tensorflow.lite.task.vision)

//    implementation(libs.litert)
//    implementation(libs.litert.support)
//    implementation(libs.litert.metadata)
//    implementation(libs.litert.tensorflow.compat)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}



//configurations.all {
//    exclude(group = "org.tensorflow", module = "tensorflow-lite-api")
//    exclude(group = "org.tensorflow", module = "tensorflow-lite-support-api")
//    exclude(group = "org.tensorflow", module = "tensorflow-lite-runtime")
//    exclude(group = "com.google.ai.edge.litert", module = "litert-api")
//    exclude(group = "com.google.ai.edge.litert", module = "litert-support-api")
//}

//configurations.all {
//    resolutionStrategy.dependencySubstitution {
//        substitute(module("org.tensorflow:tensorflow-lite"))
//            .using(module("com.google.ai.edge.litert:litert:1.0.1"))
//        substitute(module("org.tensorflow:tensorflow-lite-support"))
//            .using(module("com.google.ai.edge.litert:litert-support:1.0.1"))
//        substitute(module("org.tensorflow:tensorflow-lite-metadata"))
//            .using(module("com.google.ai.edge.litert:litert-metadata:1.0.1"))
//    }
//
//    exclude(group = "com.google.ai.edge.litert", module = "litert-support-api")
//    exclude(group = "com.google.ai.edge.litert", module = "litert-api")
//}
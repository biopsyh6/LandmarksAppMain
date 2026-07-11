import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

val mapkitKey = localProperties.getProperty("MAPKIT_API_KEY") ?: ""


android {
    namespace = "com.pavlusha.landmarksapp"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.pavlusha.landmarksapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField ("String", "MAPKIT_API_KEY", "\"$mapkitKey\"")
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
        buildConfig = true
        compose = true
        mlModelBinding = true
    }
}

dependencies {
    implementation("com.yandex.android:maps.mobile:4.33.1-full")
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.material3)
    implementation(libs.coil.compose)

    implementation(libs.retrofit)

    implementation(libs.converter.gson)

    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    implementation(libs.arsceneview)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation (libs.koin.android)
    implementation (libs.koin.androidx.navigation)
    implementation (libs.koin.androidx.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)

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
    implementation(libs.androidx.navigation.compose)
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
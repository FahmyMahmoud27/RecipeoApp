plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.linkdevelopment.domain"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.kotlinx.coroutines.core)
}
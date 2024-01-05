plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("maven-publish")
    id("realm-android")
}

android {
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        applicationId = BuildConfig.applicationId
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk
        versionCode = BuildConfig.versionCode
        versionName = BuildConfig.versionName

        testInstrumentationRunner = BuildConfig.testInstrumentationRunner

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }

        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
    }

    buildFeatures {
        viewBinding = true
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
            assets.srcDirs("src/main/assets", "src/main/assets/")
            res.srcDirs("src/main/res", "src/main/res-night")
        }
    }

    compileOptions {
        sourceCompatibility = Jdk.sourceCompatibility
        targetCompatibility = Jdk.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = Jdk.jvmTarget
    }

    signingConfigs {
        create("release") {
            storeFile = file("inspectorapp.jks")
            storePassword = "D0ub1e"
            keyAlias = "inspector"
            keyPassword = "D0ub1e"
        }
        getByName("debug") {
            storeFile = file("inspectorapp.jks")
            storePassword = "D0ub1e"
            keyAlias = "inspector"
            keyPassword = "D0ub1e"
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isDebuggable = true
            isShrinkResources = false
            manifestPlaceholders["CHANNEL"] = "def"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "is_debug", BuildConfig.debug_is_debug)
            var debug_is_dev = BuildConfig.debug_is_dev.toBoolean()
//            if(!debug_is_dev){
//                debug_is_dev = is_dev_api
//            }
            buildConfigField("boolean", "is_dev", debug_is_dev.toString())
            buildConfigField("boolean", "is_proxy", BuildConfig.debug_is_proxy)
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            manifestPlaceholders["CHANNEL"] = "def"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "is_debug", BuildConfig.release_is_debug)
            buildConfigField("boolean", "is_dev", BuildConfig.release_is_dev)
            buildConfigField("boolean", "is_proxy", BuildConfig.release_is_proxy)
        }
    }

    android.applicationVariants.all {
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                this.outputFileName =
                    "inspector_${versionName}_${versionCode}_${Time.getDate()}_${buildType.name}_${BuildConfig.debug_is_dev}.apk"
            }
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(project(":common"))

    //ARouter
    implementation(ThirdPart.arouter)
    kapt(ThirdPart.arouter_compiler)

}
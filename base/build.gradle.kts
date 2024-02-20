plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("maven-publish")
    id("kotlin-parcelize")
    id("realm-android")
}

android {
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk

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
            res.srcDirs("src/main/res", "src/main/res-night")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "is_debug", BuildConfig.debug_is_debug)
            buildConfigField("boolean", "is_dev", BuildConfig.debug_is_dev)
            buildConfigField("boolean", "is_proxy", BuildConfig.debug_is_proxy)
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "is_debug", BuildConfig.release_is_debug)
            buildConfigField("boolean", "is_dev", BuildConfig.release_is_dev)
            buildConfigField("boolean", "is_proxy", BuildConfig.release_is_proxy)
        }
    }

    compileOptions {
        sourceCompatibility = Jdk.sourceCompatibility
        targetCompatibility = Jdk.targetCompatibility
    }

    kotlinOptions {
        jvmTarget = Jdk.jvmTarget
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    api(Kotlin.stdlib)

    //ARouter
    api(ThirdPart.arouter)
    kapt(ThirdPart.arouter_compiler)

    //radius
    api(ThirdPart.UIWidget_core)
    api(ThirdPart.UIWidget_alert)
    api(ThirdPart.UIWidget_collapsing)
    api(ThirdPart.UIWidget_tab_layout)

    //basequickadapter
    api(ThirdPart.BaseRecyclerViewAdapterHelper)
    api(AndroidX.androidXRecyclerview)
    //原角图
    api(ThirdPart.roundedimageview)

    //下拉刷新
    api(ThirdPart.refresh_layout_kernel)      //core
    api(ThirdPart.refresh_header_classics)    //ClassicsHeader
//    api("com.scwang.smart:refresh-header-radar:2.0.5")       //BezierRadarHeader
//    api("com.scwang.smart:refresh-header-falsify:2.0.5")     //FalsifyHeader
//    api("com.scwang.smart:refresh-header-material:2.0.5")    //MaterialHeader
//    api("com.scwang.smart:refresh-header-two-level:2.0.5")   //TwoLevelHeader
//    api("com.scwang.smart:refresh-footer-ball:2.0.5")        //BallPulseFooter
    api(ThirdPart.refresh_footer_classics)    //ClassicsFooter

    //工具库
    api(ThirdPart.utilcodex)

    //eventbus
    api(ThirdPart.eventbus)

    //协程
    api(Kotlin.reflect)
    api(Kotlin.Coroutines.android)

    //glide
    api(ThirdPart.glide)
    kapt(ThirdPart.glide_compiler)

    //rx
    api(ThirdPart.rxjava2_rxkotlin)
    api(ThirdPart.rxjava2_rxandroid)
    api(ThirdPart.rxjava3_rxjava)
    api(ThirdPart.rxjava)
    api(ThirdPart.rxandroid)

    //retrofit
    api(ThirdPart.retrofit)
    api(ThirdPart.retrofit_converter_scalars)
    api(ThirdPart.retrofit_converter_gson)
    api(ThirdPart.okhttp3_logging_interceptor)
    api(ThirdPart.retrofit_kotlin_coroutines_adapter)
    api(ThirdPart.retrofit_rxjava2_adapter)

    //okhttp3
    api(ThirdPart.okhttp3)

    //fragment_swipe
    api(ThirdPart.fragmentationx)
    api(ThirdPart.fragmentationx_swipeback)

    //gson
    api(ThirdPart.fastjson)
    api(ThirdPart.gson)

    // Preferences DataStore（可以直接使用）
    api(ThirdPart.datastore)
    // Preferences DataStore （没有Android依赖项，包含仅适用于 Kotlin 的核心 API）
    api(ThirdPart.datastore_core)

    api(ThirdPart.datastore) {
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
    }
    api(ThirdPart.datastore_core) {
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
    }

    //锚点任务，可以用来解决多线程加载任务依赖的问题
    api(ThirdPart.anchortask)

    //rxpermission
    api(ThirdPart.rxpermissions)

    //zxing
    api(ThirdPart.zxing_android_core)
    api(ThirdPart.zxing_core)

    //realm
    api(ThirdPart.realm)

    //flycoTabLayout
    api(ThirdPart.flycoTabLayout)

    //svga
//    api(ThirdPart.SVGAPlayer)

    //模拟器检测
//    api(ThirdPart.Antifake)

    //lottie动画
    api(ThirdPart.lottie)
    //流式布局
//    api(ThirdPart.flowlayout)

//    api(ThirdPart.constraintlayout)

    //span
    api(ThirdPart.spanBuilder)

    //车牌识别
    api(ThirdPart.hyperlpr)

    api(files("libs/zp_cpcl_new.jar"))
    api(files("libs/zpSDK.jar"))

    //gsyVideoPlayer
    api(ThirdPart.gsyVideoPlayer)

    //photoView
    api(ThirdPart.photoView)

    //filedownloader
    api(ThirdPart.filedownloader)

    //umeng
    api(ThirdPart.umsdk_common)
    api(ThirdPart.umsdk_asms)
    api(ThirdPart.umeng_apm)

    api(ThirdPart.luban)
//    {
//        exclude(group = "androidx.appcompat")
//        exclude(group = "androidx.core")
//    }
//    api(ThirdPart.gsyVideoPlayer_exo) {
//        exclude(group = "androidx.appcompat")
//        exclude(group = "androidx.core")
//    }
}
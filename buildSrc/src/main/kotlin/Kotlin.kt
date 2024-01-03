object Kotlin {
    const val kotlinVersion = "1.6.21"

    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    const val stdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"
    const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    const val test = "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion"
    const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"

    object Coroutines {
        private const val version = "1.6.4"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }
}
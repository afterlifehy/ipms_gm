// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath(Kotlin.plugin)
        classpath(Gradle.plugin)
        classpath("io.realm:realm-gradle-plugin:10.11.1")
    }
}
plugins {
    id("com.android.application") version "7.4.2" apply false
    id("com.android.library") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}

//subprojects {
//    apply from: "${rootDir}/gradle/version.gradle"
//}
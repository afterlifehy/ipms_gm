import org.gradle.api.JavaVersion

object Jdk {
    val sourceCompatibility = JavaVersion.VERSION_11
    val targetCompatibility = JavaVersion.VERSION_11
    const val jvmTarget = "11"
}
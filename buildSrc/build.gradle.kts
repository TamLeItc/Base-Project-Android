import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

gradlePlugin {
    plugins {
        register("app-plugin") {
            id = "app-plugin"
            implementationClass = "AppPlugin"
        }
    }
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    maven(url = "https://plugins.gradle.org/m2/")
}

dependencies {
    compileOnly(gradleKotlinDsl())
    compileOnly(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("gradle-plugin", version = "1.8.10"))
    implementation("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
    implementation("com.android.tools.build:gradle:7.4.2")
}
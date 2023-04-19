buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://android-sdk.is.com/")
    }
}

plugins {
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
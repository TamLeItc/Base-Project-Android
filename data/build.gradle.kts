import plugins.*

plugins {
    `app-plugin`
    id("com.android.library")
    id("com.google.devtools.ksp")
}

android {
    namespace = "fxc.dev.data"
}

dependencies {
    implementation(project(":common"))

    kotlinDependencies()
    lifecycleDependencies()
    retrofitDependencies()
    roomDependencies()
    injectionDependencies()
}
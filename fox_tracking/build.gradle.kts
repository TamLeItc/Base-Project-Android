import plugins.billingDependencies
import plugins.eventTrackingDependencies
import plugins.firebaseConfigDependencies
import plugins.injectionDependencies
import plugins.kotlinDependencies
import plugins.lifecycleDependencies

plugins {
    `app-plugin`
    id("com.android.library")
}

android {
    namespace = "fxc.dev.fox_tracking"
}

dependencies {
    implementation(project(":common"))

    kotlinDependencies()
    lifecycleDependencies()
    firebaseConfigDependencies()
    injectionDependencies()
    billingDependencies()
    eventTrackingDependencies()
}
import plugins.billingDependencies
import plugins.firebaseConfigDependencies
import plugins.injectionDependencies
import plugins.kotlinDependencies
import plugins.lifecycleDependencies

plugins {
    `app-plugin`
    id("com.android.library")
}

android {
    namespace = "fxc.dev.fox_purchase"
}

dependencies {
    implementation(project(":common"))

    kotlinDependencies()
    lifecycleDependencies()
    billingDependencies()
    firebaseConfigDependencies()
    injectionDependencies()

    implementation(Dependencies.AndroidX.appCompat)

    implementation(Dependencies.View.constraintLayout)
    implementation(Dependencies.View.recyclerView)

    implementation(Dependencies.More.prefKtx)
    implementation(Dependencies.More.prefSupport)
}
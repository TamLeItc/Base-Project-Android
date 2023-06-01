import plugins.adsDependencies
import plugins.injectionDependencies
import plugins.kotlinDependencies
import plugins.lifecycleDependencies
import plugins.viewBindingDependencies

plugins {
    `app-plugin`
    id("com.android.library")
}

android {
    namespace = "fxc.dev.fox_ads"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":fox_purchase"))

    kotlinDependencies()
    viewBindingDependencies()
    injectionDependencies()
    adsDependencies()
    lifecycleDependencies()

    implementation(Dependencies.AndroidX.work)
    implementation(Dependencies.AndroidX.appCompat)

    implementation(Dependencies.View.material)

    implementation(Dependencies.View.cardView)
    implementation(Dependencies.View.constraintLayout)

    implementation(Dependencies.More.prefKtx)
    implementation(Dependencies.More.prefSupport)
}
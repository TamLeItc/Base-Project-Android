import plugins.adsDependencies
import plugins.injectionDependencies
import plugins.kotlinDependencies
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

    kotlinDependencies()
    viewBindingDependencies()
    injectionDependencies()
    adsDependencies()

    implementation(Dependencies.View.cardView)
    implementation(Dependencies.View.constraintLayout)

    implementation(Dependencies.More.prefKtx)
    implementation(Dependencies.More.prefSupport)
}
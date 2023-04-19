import plugins.*

plugins {
    `app-plugin`
    id("com.android.library")
}

android {
    namespace = "fxc.dev.base"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":fox_ads"))
    implementation(project(":fox_purchase"))

    lifecycleDependencies()
    injectionDependencies()
    viewBindingDependencies()
    billingDependencies()
    adsDependencies()

    implementation(Dependencies.View.material)

    implementation(Dependencies.More.prefKtx)
    implementation(Dependencies.More.prefSupport)
    implementation(Dependencies.More.gson)
    implementation(Dependencies.More.otto)
    implementation(Dependencies.More.peko)
}
import plugins.*

plugins {
    `app-plugin`
    id("com.android.library")
}

android {
    namespace = "fxc.dev.common"
}

dependencies {
    lifecycleDependencies()
    injectionDependencies()
    viewBindingDependencies()
    firebaseConfigDependencies()

    implementation(Dependencies.GoogleService.playCore)

    implementation(Dependencies.View.material)

    implementation(Dependencies.More.prefKtx)
    implementation(Dependencies.More.prefSupport)
    implementation(Dependencies.More.gson)
    implementation(Dependencies.More.otto)
    implementation(Dependencies.More.peko)
}
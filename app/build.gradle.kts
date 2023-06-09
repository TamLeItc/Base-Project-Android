import plugins.*

plugins {
    `app-plugin`
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "fxc.dev.app"
}

dependencies {
    implementation(
        fileTree(
            mapOf(
                "dir" to "libs",
                "include" to listOf("*.jar")
            )
        )
    )

    implementation(project(":common"))
    implementation(project(":base"))
    implementation(project(":data"))
    implementation(project(":fox_ads"))
    implementation(project(":fox_purchase"))
    implementation(project(":fox_tracking"))

    kotlinDependencies()
    lifecycleDependencies()
    injectionDependencies()
    viewBindingDependencies()
    glideDependencies()
    billingDependencies()
    firebaseConfigDependencies()

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    implementation(Dependencies.AndroidX.work)
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.AndroidX.fragmentKtx)
    implementation(Dependencies.AndroidX.activityKtx)
    implementation(Dependencies.AndroidX.navigationFrgKtx)
    implementation(Dependencies.AndroidX.navigationUiKtx)

    implementation(Dependencies.View.material)
    implementation(Dependencies.View.cardView)
    implementation(Dependencies.View.constraintLayout)
    implementation(Dependencies.View.recyclerView)
    implementation(Dependencies.View.recyclerViewSelection)

    implementation(Dependencies.More.prefKtx)
    implementation(Dependencies.More.prefSupport)
    implementation(Dependencies.More.gson)
    implementation(Dependencies.More.otto)
    implementation(Dependencies.More.peko)
    implementation(Dependencies.More.multidex)
}
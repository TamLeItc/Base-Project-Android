import plugins.*

plugins {
    `app-plugin`
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
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

    kotlinDependencies()
    lifecycleDependencies()
    injectionDependencies()
    viewBindingDependencies()
    glideDependencies()
    billingDependencies()

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

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

    implementation(platform(Dependencies.Firebase.bom))
    implementation(Dependencies.Firebase.analytics)
    implementation(Dependencies.Firebase.crashlytics)
    implementation(Dependencies.Firebase.configKtx)

    implementation(Dependencies.More.prefKtx)
    implementation(Dependencies.More.prefSupport)
    implementation(Dependencies.More.gson)
    implementation(Dependencies.More.otto)
    implementation(Dependencies.More.peko)
    implementation(Dependencies.More.multidex)
}
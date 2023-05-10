package plugins

import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 *
 * Created by tamle on 17/04/2023
 *
 */

fun DependencyHandlerScope.kotlinDependencies() {
    "implementation"(Dependencies.Kotlin.core)
    "implementation"(Dependencies.Kotlin.coroutinesCore)
    "implementation"(Dependencies.Kotlin.coroutinesAndroid)
}

fun DependencyHandlerScope.lifecycleDependencies() {
    "implementation"(Dependencies.Lifecycle.common)
    "implementation"(Dependencies.Lifecycle.viewModel)
    "implementation"(Dependencies.Lifecycle.runtime)
    "implementation"(Dependencies.Lifecycle.process)
}

fun DependencyHandlerScope.roomDependencies() {
    "implementation"(Dependencies.Room.roomKtx)
    "implementation"(Dependencies.Room.roomRuntime)
    "ksp"(Dependencies.Room.roomCompiler)
}

fun DependencyHandlerScope.retrofitDependencies() {
    "implementation"(Dependencies.Retrofit.retrofit)
    "implementation"(Dependencies.Retrofit.converterGson)
    "implementation"(Dependencies.Retrofit.loggingInterceptor)
}

fun DependencyHandlerScope.injectionDependencies() {
    "implementation"(Dependencies.Koin.core)
    "implementation"(Dependencies.Koin.android)
}

fun DependencyHandlerScope.viewBindingDependencies() {
    "implementation"(Dependencies.ViewBinding.delegate)
    "implementation"(Dependencies.ViewBinding.noreflection)
}

fun DependencyHandlerScope.glideDependencies() {
    "implementation"(Dependencies.Glide.core)
    "implementation"(Dependencies.Glide.compiler)
    "implementation"(Dependencies.Glide.integration)
}

fun DependencyHandlerScope.billingDependencies() {
    "implementation"(Dependencies.GoogleService.billing)
    "implementation"(Dependencies.GoogleService.billingKtx)
}

fun DependencyHandlerScope.adsDependencies() {
    "implementation"(Dependencies.GoogleService.playServicesAds)
    "implementation"(Dependencies.GoogleService.firebaseAds)
    "implementation"(Dependencies.GoogleService.identifierAds)
}

fun DependencyHandlerScope.firebaseConfigDependencies() {
    "implementation"(platform(Dependencies.Firebase.bom))
    "implementation"(Dependencies.Firebase.analytics)
    "implementation"(Dependencies.Firebase.crashlytics)
    "implementation"(Dependencies.Firebase.configKtx)
}

fun DependencyHandlerScope.eventTrackingDependencies() {
    "implementation"(Dependencies.EventTracking.appsFlyer)
    "implementation"(Dependencies.EventTracking.adsjust)
}
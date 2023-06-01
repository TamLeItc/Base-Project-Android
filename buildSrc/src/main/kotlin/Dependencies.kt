/**
 *
 * Created by tamle on 13/04/2023
 *
 */

object DepVersions {
    object Kotlin {
        const val core = "1.8.0"
        const val coroutine = "1.6.1"
    }

    object AndroidX {
        const val appcompat = "1.5.0"
        const val workRuntime = "2.8.1"
        const val fragmentKtx = "1.5.6"
        const val activityKtx = "1.7.0"
        const val navigationKtx = "2.5.3"
    }

    object Lifecycle {
        const val lifecycle = "2.6.1"
    }

    object Room {
        const val core = "2.4.2"
    }

    object Koin {
        const val core = "3.2.0"
    }

    object Retrofit {
        const val retrofit = "2.9.0"
        const val loggingInterceptor = "4.9.3"
    }

    object Glide {
        const val core = "4.13.0"
    }

    object Firebase {
        const val bom = "32.0.0"
    }

    object GoogleService {
        const val playCore = "1.10.3"
        const val playServicesAds = "22.0.0"
        const val firebaseAds = "22.0.0"
        const val identifierAds = "18.0.1"
        const val billing = "6.0.0"
    }

    object EventTracking {
        const val appsFlyer = "6.3.2"
        const val adsjust = "4.33.3"
        const val installreferrer = "2.2"
    }

    object View {
        const val material = "1.6.1"
        const val cardView = "1.0.0"
        const val constraintLayout = "2.1.4"
        const val recyclerView = "1.2.1"
        const val recyclerViewSelection = "1.1.0"
    }

    object ViewBinding {
        const val core = "1.5.6"
    }

    object More {
        const val peko = "2.1.4"
        const val gson = "2.9.0"
        const val prefKtx = "2.13.1"
        const val otto = "1.3.8"
        const val multidex = "2.0.1"
    }
}

object Dependencies {
    object Kotlin {
        const val core = "androidx.core:core-ktx:${DepVersions.Kotlin.core}"
        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${DepVersions.Kotlin.coroutine}"
        const val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${DepVersions.Kotlin.coroutine}"
    }

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:${DepVersions.AndroidX.appcompat}"
        const val work = "androidx.work:work-runtime:${DepVersions.AndroidX.workRuntime}"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:${DepVersions.AndroidX.fragmentKtx}"
        const val activityKtx = "androidx.activity:activity-ktx:${DepVersions.AndroidX.activityKtx}"
        const val navigationFrgKtx =
            "androidx.navigation:navigation-fragment-ktx:${DepVersions.AndroidX.navigationKtx}"
        const val navigationUiKtx =
            "androidx.navigation:navigation-ui-ktx:${DepVersions.AndroidX.navigationKtx}"
    }

    object Lifecycle {
        const val common = "androidx.lifecycle:lifecycle-common:${DepVersions.Lifecycle.lifecycle}"
        const val viewModel =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${DepVersions.Lifecycle.lifecycle}"
        const val runtime =
            "androidx.lifecycle:lifecycle-runtime-ktx:${DepVersions.Lifecycle.lifecycle}"
        const val process =
            "androidx.lifecycle:lifecycle-process:${DepVersions.Lifecycle.lifecycle}"
    }

    object Room {
        const val roomKtx = "androidx.room:room-ktx:${DepVersions.Room.core}"
        const val roomRuntime = "androidx.room:room-runtime:${DepVersions.Room.core}"
        const val roomCompiler = "androidx.room:room-compiler:${DepVersions.Room.core}"
    }

    object Koin {
        const val core = "io.insert-koin:koin-core:${DepVersions.Koin.core}"
        const val android = "io.insert-koin:koin-android:${DepVersions.Koin.core}"
    }

    object Retrofit {
        const val retrofit = "com.squareup.retrofit2:retrofit:${DepVersions.Retrofit.retrofit}"
        const val converterGson =
            "com.squareup.retrofit2:converter-gson:${DepVersions.Retrofit.retrofit}"
        const val loggingInterceptor =
            "com.squareup.okhttp3:logging-interceptor:${DepVersions.Retrofit.loggingInterceptor}"
    }

    object Glide {
        const val core = "com.github.bumptech.glide:glide:${DepVersions.Glide.core}"
        const val compiler = "com.github.bumptech.glide:compiler:${DepVersions.Glide.core}"
        const val integration =
            "com.github.bumptech.glide:okhttp3-integration:${DepVersions.Glide.core}"
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:${DepVersions.Firebase.bom}"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val configKtx = "com.google.firebase:firebase-config-ktx"
    }

    object GoogleService {
        const val playCore = "com.google.android.play:core:${DepVersions.GoogleService.playCore}"
        const val playServicesAds =
            "com.google.android.gms:play-services-ads:${DepVersions.GoogleService.playServicesAds}"
        const val firebaseAds =
            "com.google.firebase:firebase-ads:${DepVersions.GoogleService.firebaseAds}"
        const val identifierAds =
            "com.google.android.gms:play-services-ads-identifier:${DepVersions.GoogleService.identifierAds}"
        const val billing = "com.android.billingclient:billing:${DepVersions.GoogleService.billing}"
        const val billingKtx =
            "com.android.billingclient:billing-ktx:${DepVersions.GoogleService.billing}"
    }

    object EventTracking {
        const val appsFlyer = "com.appsflyer:af-android-sdk:${DepVersions.EventTracking.appsFlyer}"
        const val adsjust = "com.adjust.sdk:adjust-android:${DepVersions.EventTracking.adsjust}"
        const val installreferrer = "com.android.installreferrer:installreferrer:${DepVersions.EventTracking.installreferrer}"
    }

    object View {
        const val material = "com.google.android.material:material:${DepVersions.View.material}"
        const val cardView = "androidx.cardview:cardview:${DepVersions.View.cardView}"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${DepVersions.View.constraintLayout}"
        const val recyclerView =
            "androidx.recyclerview:recyclerview:${DepVersions.View.recyclerView}"
        const val recyclerViewSelection =
            "androidx.recyclerview:recyclerview-selection:${DepVersions.View.recyclerViewSelection}"
    }

    object ViewBinding {
        const val delegate =
            "com.github.kirich1409:viewbindingpropertydelegate:${DepVersions.ViewBinding.core}"
        const val noreflection =
            "com.github.kirich1409:viewbindingpropertydelegate-noreflection:${DepVersions.ViewBinding.core}"
    }

    object More {
        const val peko = "com.markodevcic:peko:${DepVersions.More.peko}"
        const val gson = "com.google.code.gson:gson:${DepVersions.More.gson}"
        const val prefKtx = "com.chibatching.kotpref:kotpref:${DepVersions.More.prefKtx}"
        const val prefSupport = "com.chibatching.kotpref:enum-support:${DepVersions.More.prefKtx}"
        const val otto = "com.squareup:otto:${DepVersions.More.otto}"
        const val multidex = "androidx.multidex:multidex:2.0.1"
    }
}
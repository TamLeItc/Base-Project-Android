Base Android Clean Architecture

### Module
    app
    base
    common (used for all modules)
    data
    fox_ads
    fox_purchase
    buildSrc

### What is code base?
    MVVM Clean Architecture
     - DSLs with Kotlin
     - Dependency injection with Koin
     - REST api with Retrofit
     - Local database with Room
     - Tracking event with Appsflyer, Adjust
     - Firebase analytics/crashlytics
     
    Kotlin
    View binding
    Androidx
    Navigation
    Material
    Glide
    Lifecycle
    Coroutine
    In-app billing
    Admob
     

### Config release
    name_name app_id, inapp_id
    Những thông tin trên edit trong file app/configs.xml

    applicationId, versionCode, versionName
    Những thông tin này edit trong file buildSrc/src/main/kotlin/AppConfig.kt

### How to set app version?
    https://blog.dipien.com/versioning-android-apps-d6ec171cfd82
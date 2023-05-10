import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.gradle.api.Incubating
import org.gradle.api.JavaVersion.VERSION_1_8
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*

private inline val Project.libraryExtension get() = extensions.getByType<LibraryExtension>()
private inline val Project.appExtension get() = extensions.getByType<AppExtension>()

class AppPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            plugins.all {
                when (this) {
                    is LibraryPlugin -> {
                        applyPlugins()
                        configAndroidLibrary()
                    }
                    is AppPlugin -> {
                        applyPlugins()
                        configAndroidApplication()
                    }
                    is KotlinBasePluginWrapper -> configKotlinOptions()
                }
            }
        }
    }
}

private fun Project.applyPlugins() {
    plugins.run {
        apply("kotlin-android")
        apply("kotlin-parcelize")
    }
}

private fun Project.configKotlinOptions() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = VERSION_1_8.toString()
            freeCompilerArgs = freeCompilerArgs + listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }
}

private fun Project.configAndroidLibrary() = libraryExtension.run {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
    }

    buildTypes {
        getByName("release") {
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = VERSION_1_8
        targetCompatibility = VERSION_1_8
    }
}

@Incubating
private fun Project.configAndroidApplication() = appExtension.run {
    compileSdkVersion(AppConfig.compileSdk)

    defaultConfig {
        applicationId = AppConfig.applicationId

        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk

        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val keystoreProperties = Properties()
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"].toString()
            keyPassword = keystoreProperties["keyPassword"].toString()
            storeFile = file(keystoreProperties["storeFile"].toString())
            storePassword = keystoreProperties["storePassword"].toString()
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false

            manifestPlaceholders(
                mapOf(
                    Pair("crashlyticsCollectionEnabled", true)
                )
            )

            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isDebuggable = true

            manifestPlaceholders(
                mapOf(
                    Pair("crashlyticsCollectionEnabled", false)
                )
            )
        }
    }

    buildFeatures.run {
        viewBinding = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = VERSION_1_8
        targetCompatibility = VERSION_1_8
    }

    ndkVersion = "22.0.7026061"

    applicationVariants.all {
        val dateFormat = SimpleDateFormat("ddMMyy_HHmm", Locale.US)
        val projectName = rootProject.name

        val dateStr = dateFormat.format(Date())
        val variantName = name
        val variantVersion = versionName
        outputs.all {
            val output = this as? BaseVariantOutputImpl
            output?.outputFileName =
                "${projectName}-${variantName}-${variantVersion}-${dateStr}.apk"
        }
    }
}

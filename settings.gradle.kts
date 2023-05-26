pluginManagement {
    repositories {
        google()
        mavenCentral()
    }
}

//@Incubating
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "base_mvvm"
include(":app")
include(":common")
include(":fox_ads")
include(":fox_purchase")
include(":data")
include(":base")
include(":fox_tracking")

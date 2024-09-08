pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        // Add any other repositories you need
    }
}


rootProject.name = "Base Project Application"
include(":app")
include(":data")
include(":domain")
include(":features")
include(":data:local")
include(":data:model")
include(":data:remote")
include(":data:repository")
include(":libraries")
include(":libraries:framework")
include(":common")
include(":common:provider")
include(":features:login")
include(":common:ui-resource")
include(":features:home")

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

        maven{
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            credentials {
                username = "mapbox"
                password = "pk.eyJ1IjoiZ3JlZW4xNjExIiwiYSI6ImNtM3UzdmlrajBmc2gycW9zOW4wN2o5bGYifQ.o-JcrEJFjBiV3WLKut-WNw"
            }

        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            credentials {
                username = "mapbox"
                password = "pk.eyJ1IjoiZ3JlZW4xNjExIiwiYSI6ImNtM3UzdmlrajBmc2gycW9zOW4wN2o5bGYifQ.o-JcrEJFjBiV3WLKut-WNw" // Replace with your actual token
            }
        }
    }
}

rootProject.name = "CMU_Geocaching"
include(":app")
 
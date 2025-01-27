import com.android.build.api.dsl.LintOptions
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "ipp.estg.mobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "ipp.estg.mobile"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        // Load the values from .properties file
        val properties = Properties()
        properties.load(project.rootProject.file("gradle.properties").inputStream())

        debug {
            // Configure gradle.properties variables for development
            buildConfigField("String", "LEAFLINGS_API_BASE_URL", "\"${properties.getProperty("LEAFLINGS_API_DEVELOPMENT_URL")}\"")
        }

        release {
            // Configure gradle.properties variables for production
            buildConfigField("String", "LEAFLINGS_API_BASE_URL", "\"${properties.getProperty("LEAFLINGS_API_PRODUCTION_URL")}\"")

            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true // para usar o buildConfig para aceder a variáveis do gradle.properties
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    lint {
        baseline = file("lint-baseline.xml")
        abortOnError = false // Set to true to abort the build if errors are found.
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // permitir usar permissões no jetpack compose
    implementation(libs.accompanist.permissions)

    // Test rules and transitive dependencies:
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.5")
    // Needed for createComposeRule(), but not for createAndroidComposeRule<YourActivity>():
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.5")

    // Room - to save the data locally and see it in offline mode (cache que funciona como uma base de dados sql)
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")

    // retrofit - para fazer pedidos http e fazer parse de json para classes em kotlin ou java
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // glide - para passar imagens de um link da web para o kotlin
    implementation(libs.glide)
    implementation("com.github.skydoves:landscapist-glide:1.4.8") // GlideCard

    // ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
    implementation("androidx.fragment:fragment-ktx:1.8.2") // Use the latest version

    // load images from url
    implementation("io.coil-kt.coil3:coil-compose:3.0.0-rc02")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0-rc02")
    implementation("io.coil-kt.coil3:coil-gif:3.0.0-rc02") // suport with gifs

    // mais icons
    implementation("androidx.compose.material:material-icons-extended:1.7.1")

    // For Camera
    implementation("androidx.camera:camera-camera2:1.1.0-alpha04")
    implementation("androidx.camera:camera-lifecycle:1.1.0-alpha04")
    implementation("androidx.camera:camera-view:1.0.0-alpha21")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")

    // To use LiveData with Compose
    implementation("androidx.compose.runtime:runtime-livedata:1.7.5")

    //Logging Network Calls
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation(kotlin("script-runtime"))
}
plugins {
//    id ("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
    id("com.android.application")

    id("io.freefair.lombok") version "8.11"

    id("de.mannodermaus.android-junit5")

    id("androidx.navigation.safeargs")

    id("idea")
}


android {

    idea {
        module {
            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }

    namespace = "bruzsa.laszlo.dartsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "bruzsa.laszlo.dartsapp"
        minSdk = 29
        targetSdk = 35
        versionCode = 3
        versionName = "0.1"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

//        tasks.withType<JavaCompile> {
//            val compilerArgs = options.compilerArgs
//            compilerArgs.add("-Xdoclint:all,-missing")
//            compilerArgs.add("-Xlint:all")
//            compilerArgs.add("--enable-preview")
//        }

//        tasks.withType<Test>{
//            val jvmArgs = allJvmArgs
//            jvmArgs.add("--enable-preview")
//        }

    }
    buildFeatures {
        viewBinding = true
//        compose = true
    }

//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.15"
//    }

//    testOptions {
//        unitTests.all {
//            JUnitPlatformOptions()
//        }
//    }


    dataBinding {
        enable = true
    }

//    kotlinOptions {
//        jvmTarget = "21"
//    }

    sourceSets {
        named("main") {
            java.srcDir("src/main/assets")
        }
    }


//    kotlin {
//        jvmToolchain(21)
//    }

}

//kapt {
//    correctErrorTypes =true
//    keepJavacAnnotationProcessors = true
//}


//
//kotlinLombok {
//    lombokConfigurationFile(file("lombok.config"))
//}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    testImplementation("junit:junit:4.13.2")
    //    compileOnly(libs.lombok)
//    annotationProcessor(libs.lombok)
//    testCompileOnly(libs.lombok)
//    testAnnotationProcessor(libs.lombok)

    implementation("org.nanohttpd:nanohttpd:2.3.1")

    implementation("org.slf4j:slf4j-simple:2.0.16")

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.5")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    testImplementation("org.assertj:assertj-core:3.27.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    testImplementation("org.mockito:mockito-core:5.14.2")


    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf
    implementation("org.thymeleaf:thymeleaf:3.1.3.RELEASE")

    implementation("androidx.datastore:datastore-preferences:1.1.1")
    // optional - RxJava3 support
    implementation("androidx.datastore:datastore-preferences-rxjava3:1.1.1")


    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test:rules:1.6.1")
    // Optional -- UI testing with Espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    // Optional -- UI testing with UI Automator
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")
    // Optional -- UI testing with Compose
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.6")
}
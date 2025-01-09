import java.util.Locale

plugins {
    id("com.google.dagger.hilt.android")

    id("org.jetbrains.kotlin.android")

    id("com.android.application")

    id("androidx.navigation.safeargs")

    id("idea")

    //  gradlew JacocoDebugCodeCoverage
    jacoco

    //  gradlew buildHealth
    // Fix dependency issues automatically: gradlew fixDependencies
    id("com.autonomousapps.dependency-analysis")
}


android {

    hilt {
        enableAggregatingTask = true
    }

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
//        multiDexEnabled = true

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "bruzsa.laszlo.dartsapp.MyTestRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

    }
    buildFeatures {
        viewBinding = true
    }

    dataBinding {
        enable = true
    }

    sourceSets {
        named("main") {
            java.srcDir("src/main/assets")
        }
    }

}

dependencies {

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.54")
    annotationProcessor("com.google.dagger:hilt-compiler:2.54")
    testAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.54")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.54")

    // LiveData Test - InstantTaskExecutorRule
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")

    // (Required) Writing and executing Unit Tests on the JUnit Platform
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
    // (Optional) If you need "Parameterized Tests"
    // (Optional) If you also have JUnit 4-based tests
    androidTestImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.11.4")
    androidTestImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    // @ExtendWith(InstantExecutorExtension.class)

    // AssertJ
    androidTestCompileOnly("com.squareup.assertj:assertj-android:1.2.0")

    // For local unit tests
//    testImplementation 'com.google.dagger:hilt-android-testing:2.54'
//    testAnnotationProcessor ("com.google.dagger:hilt-compiler:2.54")

    //Unit test
//    testImplementation ("org.robolectric:robolectric:4.14.1")
    //UI test
//    androidTestImplementation ("org.robolectric:robolectric:4.14.1")

    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    implementation("org.nanohttpd:nanohttpd:2.3.1")

    runtimeOnly("org.slf4j:slf4j-simple:2.0.16")

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    testImplementation("org.assertj:assertj-core:3.27.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")

    // https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf
    implementation("org.thymeleaf:thymeleaf:3.1.3.RELEASE")

    // optional - RxJava3 support


    androidTestImplementation("androidx.test:runner:1.6.2")
    // Optional -- UI testing with Espresso
    implementation("androidx.activity:activity:1.9.3")
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("androidx.core:core:1.15.0")
    implementation("androidx.customview:customview:1.1.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("androidx.fragment:fragment:1.8.5")
    implementation("androidx.lifecycle:lifecycle-common:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-core:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.7")
    implementation("androidx.navigation:navigation-common:2.8.5")
    implementation("androidx.navigation:navigation-fragment:2.8.5")
    implementation("androidx.navigation:navigation-runtime:2.8.5")
    implementation("androidx.navigation:navigation-ui:2.8.5")
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.sqlite:sqlite:2.4.0")
    androidTestImplementation("androidx.test:core:1.6.1")
    androidTestImplementation("androidx.test:monitor:1.7.2")
    annotationProcessor("com.google.dagger:dagger-compiler:2.54")
    implementation("com.google.dagger:dagger:2.54")
    implementation("com.google.dagger:hilt-core:2.54")
    implementation("javax.inject:javax.inject:1")
}

// --------------------- JACOCO START -------------------
val exclusions = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*"
)

tasks.withType(Test::class) {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

android {
    applicationVariants.all(closureOf<com.android.build.gradle.internal.api.BaseVariantImpl> {
        val variant = this@closureOf.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }

        val unitTests = "test${variant}UnitTest"
        val androidTests = "connected${variant}AndroidTest"

        tasks.register<JacocoReport>("Jacoco${variant}CodeCoverage") {
            dependsOn(listOf(unitTests, androidTests))
            group = "Reporting"
            description = "Execute ui and unit tests, generate and combine Jacoco coverage report"
            reports {
                xml.required.set(true)
                html.required.set(true)
            }
            sourceDirectories.setFrom(layout.projectDirectory.dir("src/main"))
            classDirectories.setFrom(files(
                fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
                    exclude(exclusions)
                },
                fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
                    exclude(exclusions)
                }
            ))
            executionData.setFrom(files(
                fileTree(layout.buildDirectory) { include(listOf("**/*.exec", "**/*.ec")) }
            ))
        }
    })
}
//---------------------- JACOCO END -----------------------

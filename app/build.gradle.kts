import java.util.Locale

plugins {
    id("com.google.dagger.hilt.android")

    id("org.jetbrains.kotlin.android") version "2.2.21"

    id("com.android.application")

    id("de.mannodermaus.android-junit5") version "1.14.0.0"

    id("androidx.navigation.safeargs")

    id("idea")

    jacoco
    
    id("com.autonomousapps.dependency-analysis")
}


android {

    lint {
        baseline = file("lint-baseline.xml")
    }

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
    compileSdk = 36

    defaultConfig {
        applicationId = "bruzsa.laszlo.dartsapp"
        minSdk = 29
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21

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
    testImplementation("androidx.test:runner:1.7.0")
    testImplementation("androidx.test.ext:junit:1.3.0")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.57.2")
    annotationProcessor("com.google.dagger:hilt-compiler:2.57.2")
    testImplementation("com.google.dagger:hilt-android-testing:2.57.2")
    testAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.57.2")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.57.2")
    androidTestAnnotationProcessor("com.google.dagger:hilt-compiler:2.57.2")

    // LiveData Test - InstantTaskExecutorRule
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")

    // (Required) Writing and executing Unit Tests on the JUnit Platform
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.1")
    // (Optional) If you need "Parameterized Tests"
    testImplementation("org.junit.jupiter:junit-jupiter-params:6.0.1")
    // (Optional) If you also have JUnit 4-based tests
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:6.0.1")
    androidTestImplementation("org.junit.jupiter:junit-jupiter-api:6.0.1")
    // @ExtendWith(InstantExecutorExtension.class)
    testImplementation("io.github.neboskreb:instant-task-executor-extension:1.0.0")

    // AssertJ
    androidTestCompileOnly("com.squareup.assertj:assertj-android:1.2.0")

    // For local unit tests
//    testImplementation 'com.google.dagger:hilt-android-testing:2.54'
//    testAnnotationProcessor ("com.google.dagger:hilt-compiler:2.54")

    //Unit test
//    testImplementation ("org.robolectric:robolectric:4.14.1")
    //UI test
//    androidTestImplementation ("org.robolectric:robolectric:4.14.1")

    implementation("androidx.room:room-runtime:2.8.4")
    annotationProcessor("androidx.room:room-compiler:2.8.4")

    implementation("org.nanohttpd:nanohttpd:2.3.1")

    implementation("org.slf4j:slf4j-simple:2.0.17")

    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.10.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.window:window:1.5.1")

    testImplementation("org.assertj:assertj-core:3.27.6")
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:6.0.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.1")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    testImplementation("org.mockito:mockito-core:5.21.0")


    // https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf
    implementation("org.thymeleaf:thymeleaf:3.1.3.RELEASE")
    implementation("org.unbescape:unbescape:1.1.6.RELEASE")

    implementation("androidx.datastore:datastore-preferences:1.2.0")
    // optional - RxJava3 support
    implementation("androidx.datastore:datastore-preferences-rxjava3:1.2.0")


    androidTestImplementation("androidx.test:runner:1.7.0")
    androidTestImplementation("androidx.test:rules:1.7.0")
    // Optional -- UI testing with Espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")


    androidTestImplementation("androidx.test:core:1.7.0")
    androidTestImplementation("androidx.test:monitor:1.8.0")
    annotationProcessor("com.google.dagger:dagger-compiler:2.57.2")
    implementation("androidx.activity:activity:1.12.1")
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.3.0")
    implementation("androidx.core:core:1.17.0")
    implementation("androidx.customview:customview:1.2.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("androidx.fragment:fragment:1.8.9")
    implementation("androidx.lifecycle:lifecycle-common:2.10.0")
    implementation("androidx.lifecycle:lifecycle-livedata-core:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.10.0")
    implementation("androidx.navigation:navigation-common:2.9.6")
    implementation("androidx.navigation:navigation-fragment:2.9.6")
    implementation("androidx.navigation:navigation-runtime:2.9.6")
    implementation("androidx.navigation:navigation-ui:2.9.6")
    implementation("androidx.room:room-common:2.8.4")
    implementation("androidx.sqlite:sqlite:2.6.2")
    implementation("com.google.dagger:dagger:2.57.2")
    implementation("com.google.dagger:hilt-core:2.57.2")
    implementation("javax.inject:javax.inject:1")
}

tasks.withType(JavaCompile::class.java) {
    options.compilerArgs.add("-Xlint:deprecation")
}

// --------------------- JACOCO START -------------------    gradlew JacocoDebugCodeCoverage
val exclusions = listOf(
    "**/R.class",
    "**/R*.class",
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
    buildToolsVersion = "36.1.0"
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

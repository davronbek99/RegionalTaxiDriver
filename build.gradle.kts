buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
//        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
//        classpath("com.android.tools.build:gradle:7.4.2")
    }

    repositories {
        mavenCentral()
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.android.library") version "7.4.2" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}

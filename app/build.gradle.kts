plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
//    id("com.google.firebase.crashlytics")
}

android {
    namespace = "dev.davron.regionaltaxidriver"
    compileSdk = 34

        buildFeatures {
            buildConfig = true
        }

        defaultConfig {
            versionCode = 4
        }

        defaultConfig {
            applicationId = "dev.davron.regionaltaxidriver"
            minSdk = 24
            targetSdk = 34
            versionCode = 1
            versionName = "1.0"
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
        }
        kotlinOptions {
            jvmTarget = "17"
        }

        buildFeatures {
            viewBinding = true
        }
    }

    dependencies {

        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.9.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
        implementation("androidx.navigation:navigation-ui-ktx:2.7.4")
        implementation("androidx.legacy:legacy-support-v4:1.0.0")
        implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

        // firebase
        implementation("com.google.firebase:firebase-messaging:23.2.1")
        implementation("com.google.firebase:firebase-database:20.2.2")
//    implementation("com.google.firebase:firebase-crashlytics:18.4.3")
        implementation("com.google.firebase:firebase-analytics:21.3.0")
        implementation("com.google.firebase:firebase-storage:20.2.1")
        implementation("com.google.firebase:firebase-config:21.4.1")


        //lotti animation
        implementation("com.airbnb.android:lottie:5.0.3")

        //mask edittext
        implementation("com.github.santalu:maskara:1.0.0")

        //sms input layout...
        implementation("com.github.mukeshsolanki:android-otpview-pinview:2.1.2")

        //circleImageView...
        implementation("de.hdodenhof:circleimageview:3.1.0")

        //toggle button
        implementation("nl.bryanderidder:themed-toggle-button-group:1.4.1")

        //rounded corner imagge view
        implementation("com.makeramen:roundedimageview:2.3.0")

        //photo view
        implementation("com.github.chrisbanes:PhotoView:2.3.0")

        //camera view (cameraX)
        implementation("androidx.camera:camera-core:1.4.0-alpha01")
        implementation("androidx.camera:camera-camera2:1.4.0-alpha01")
        implementation("androidx.camera:camera-lifecycle:1.4.0-alpha01")
        implementation("androidx.camera:camera-video:1.4.0-alpha01")

        implementation("androidx.camera:camera-view:1.4.0-alpha01")
        implementation("androidx.camera:camera-extensions:1.4.0-alpha01")

        //lingver library
        implementation("com.github.YarikSOffice:lingver:1.3.0")

        //shimmer layout
        implementation("com.facebook.shimmer:shimmer:0.5.0")

        //dagger hilt
        implementation("com.google.dagger:hilt-android:2.44")
        kapt("com.google.dagger:hilt-compiler:2.48")

        //lifecycle viewmodel
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

        //retrofit 2
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")

        //chuck...
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
        debugImplementation("com.github.chuckerteam.chucker:library:3.5.2")
        releaseImplementation("com.github.chuckerteam.chucker:library-no-op:3.5.2")

        //multi dex application
        implementation("androidx.multidex:multidex:2.0.1")

        //karumi dexter
        implementation ("com.karumi:dexter:6.2.3")

        //calendar view
        implementation("com.github.prolificinteractive:material-calendarview:2.0.1")

        //number picker
        implementation("io.github.ShawnLin013:number-picker:2.4.13")

        //switch button
        implementation("com.github.zcweng:switch-button:0.0.3@aar")

        //swipe refresh layout
        implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

        ///room
        implementation("androidx.room:room-runtime:2.5.2")
        annotationProcessor("androidx.room:room-compiler:2.5.2")


        //mad location manager
        implementation("com.github.maddevsio:mad-location-manager:0.1.14")

    }

    kapt {
        correctErrorTypes = true
    }
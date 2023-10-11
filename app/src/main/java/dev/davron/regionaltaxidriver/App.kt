package dev.davron.regionaltaxidriver

import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import dev.davron.regionaltaxidriver.utils.Common
import dev.davron.regionaltaxidriver.utils.MySharedPreferences

@HiltAndroidApp
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        //init lang
        val lang = MySharedPreferences.getLang(this) ?: ""
        if (lang != "") {
            Lingver.init(this, lang)
        } else {
            Lingver.init(this, "en")
        }

        //here
        FirebaseApp.initializeApp(this)

        //init application context
        Common.globalContext = this
        Common.packageName = this.packageName
    }
}
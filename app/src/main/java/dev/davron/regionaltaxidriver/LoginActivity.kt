package dev.davron.regionaltaxidriver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.davron.regionaltaxidriver.databinding.ActivityLoginBinding
import dev.davron.regionaltaxidriver.utils.MySharedPreferences
import dev.davron.regionaltaxidriver.utils.getAutoNightMode
import dev.davron.regionaltaxidriver.utils.statusBarColor
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setUpUI()
    }


    private fun setTheme() {
        when (MySharedPreferences.getNightMode(this)) {
            "day" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            "night" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            "auto" -> {
                setListeners()

                when (getAutoNightMode()) {
                    "day" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }

                    "night" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
            }
        }
        changeStatusBarColor()
    }

    private fun setUpUI() {
        changeStatusBarColor()
    }

    private fun changeStatusBarColor() {

        var nightMode = MySharedPreferences.getNightMode(this)

        if (nightMode == "auto") nightMode = getAutoNightMode()

        statusBarColor(
            ResourcesCompat.getColor(resources, R.color.main_status_color, theme),
            ResourcesCompat.getColor(resources, R.color.main_status_color, theme),
            nightMode != "night"
        )
    }

    private fun setListeners() {
        val intentFilter = IntentFilter(Intent.ACTION_TIME_TICK)
        registerReceiver(timerReceiver, intentFilter)
    }

    private val timerReceiver = object : BroadcastReceiver() {
        @SuppressLint("SimpleDateFormat")
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1 != null) {
                if (p1.action == Intent.ACTION_TIME_TICK) {
                    val time = Date()
                    val dH = SimpleDateFormat("HH")
                    val dM = SimpleDateFormat("mm")
                    val hour = dH.format(time).toInt()
                    val minute = dM.format(time).toInt()
                    val nightMode = MySharedPreferences.getNightMode(this@LoginActivity)

                    if (nightMode == "auto") {
                        if (hour == 18 || hour == 6) {
                            if (minute == 0) {
//                                finishAffinity()
//                                val intent =
//                                    Intent(this@MainActivity, MainActivity::class.java)
//                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }

}
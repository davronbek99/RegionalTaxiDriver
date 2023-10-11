package dev.davron.regionaltaxidriver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.davron.regionaltaxidriver.databinding.ActivitySplashScreenBinding
import dev.davron.regionaltaxidriver.fragment.onlineRegistration.main.RegistrationMainViewModel
import dev.davron.regionaltaxidriver.utils.Common
import dev.davron.regionaltaxidriver.utils.MySharedPreferences
import dev.davron.regionaltaxidriver.utils.isOnline
import dev.davron.regionaltaxidriver.utils.statusBarColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var registrationMainViewModel: RegistrationMainViewModel

    @SuppressLint("SetTextI18n", "UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.versionTv.text = "${getString(R.string.app_version)} ${BuildConfig.VERSION_NAME}"

        changeStatusBarColor()
        registerReceiver(locationReceiver, IntentFilter("location.update"))
        registerReceiver(locationReceiver, IntentFilter("time.update"))

        registrationMainViewModel = ViewModelProvider(this)[RegistrationMainViewModel::class.java]

        if (MySharedPreferences.getToken(this) == "") {
            lifecycleScope.launch {
                delay(1000)
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            if (isOnline(this)) {
                setViewModelListener()
                setViewModel()
            } else {
                val status = MySharedPreferences.getStatus(this)
                Common.accountStatus = status

                lifecycleScope.launch {
                    delay(1000)
                    val intent =
                        Intent(this@SplashScreenActivity, NoInternetConnectionActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }
    }

    private fun setViewModel() {

    }

    private fun setViewModelListener() {

    }

    private fun changeStatusBarColor() {
        statusBarColor(
            ResourcesCompat.getColor(resources, R.color.madrid_100, theme),
            ResourcesCompat.getColor(resources, R.color.madrid_100, theme),
            true
        )
    }


    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1 != null) {
                if (p1.action == "location.update") {
                    val location = p1.getParcelableExtra<Location>("location")
                    val state = p1.getStringExtra("state") ?: ""
                    Common.state = state
                    Common.myLocation = location
                    Common.mutableLocation.postValue(location)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(locationReceiver)
    }


}
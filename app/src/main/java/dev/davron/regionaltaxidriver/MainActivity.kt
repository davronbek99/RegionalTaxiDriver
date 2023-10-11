package dev.davron.regionaltaxidriver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.davron.regionaltaxidriver.databinding.ActivityMainBinding
import dev.davron.regionaltaxidriver.utils.MySharedPreferences
import dev.davron.regionaltaxidriver.utils.NetworkState
import dev.davron.regionaltaxidriver.utils.broadcastReciever.NetworkBroadcastReceiver
import dev.davron.regionaltaxidriver.utils.getAutoNightMode
import dev.davron.regionaltaxidriver.utils.statusBarColor
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NetworkState {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var networkBroadcastReceiver: NetworkBroadcastReceiver
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {

//        setTheme()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUpdateNewVersion()


        init()
        setBottomNav()
        setNavController()
        setUpUI()
    }

    private fun checkUpdateNewVersion() {

    }

    private fun setBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupWithNavController(binding.bottomNavigation, navController)
        adjustFontScale(resources.configuration)

    }

    private fun setNavController() {
        binding.apply {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.region_taxi, R.id.activity, R.id.profile -> {
                        binding.bottomNavigation.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.bottomNavigation.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun init() {

//        FirebaseApp.initializeApp(this)
//        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)


        networkBroadcastReceiver = NetworkBroadcastReceiver(this)
        registerReceiver(
            networkBroadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setUpUI() {
//        changeStatusBarColor()
    }

    private var isFirstEntering: Boolean = true
    override fun isOnline(value: Boolean) {
        if (value) {
            Log.d(TAG, "isOnline: online")
            if (!isFirstEntering) {
                binding.networkStateTv.isVisible = true
                binding.networkStateTv.text = resources.getString(R.string.online)
                binding.networkStateTv.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.geneva_100
                    )
                )

                Handler(Looper.getMainLooper()).postDelayed({
                    binding.networkStateTv.isVisible = false
                }, 3000)
            }
        } else {
            Log.d(TAG, "isOnline: offline")

            binding.networkStateTv.isVisible = true
            binding.networkStateTv.text = resources.getString(R.string.no_internet)
            binding.networkStateTv.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.moscow_100
                )
            )
            isFirstEntering = false
        }
    }

    private fun adjustFontScale(configuration: Configuration) {
        if (configuration.fontScale > 1f || configuration.fontScale < 1f) {
            configuration.fontScale = 1f
            val metrics = resources.displayMetrics
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
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
                    val nightMode = MySharedPreferences.getNightMode(this@MainActivity)

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
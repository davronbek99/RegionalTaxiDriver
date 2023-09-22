package dev.davron.regionaltaxidriver

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
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import dev.davron.regionaltaxidriver.databinding.ActivityMainBinding
import dev.davron.regionaltaxidriver.utils.NetworkState
import dev.davron.regionaltaxidriver.utils.broadcastReciever.NetworkBroadcastReceiver

class MainActivity : AppCompatActivity(), NetworkState {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var networkBroadcastReceiver: NetworkBroadcastReceiver
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUpdateNewVersion()
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

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
}
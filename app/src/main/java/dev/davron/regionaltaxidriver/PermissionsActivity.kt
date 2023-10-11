package dev.davron.regionaltaxidriver

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.text.TextUtils
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dev.davron.regionaltaxidriver.adapters.permissionRequest.PermissionsRvAdapter
import dev.davron.regionaltaxidriver.databinding.ActivityPermissionsBinding
import dev.davron.regionaltaxidriver.dialogs.AutoClickerDialog
import dev.davron.regionaltaxidriver.models.permissionRequest.PermissionModel
import dev.davron.regionaltaxidriver.utils.AutoClickerDetector
import dev.davron.regionaltaxidriver.utils.MySharedPreferences
import dev.davron.regionaltaxidriver.utils.getAutoNightMode
import dev.davron.regionaltaxidriver.utils.statusBarColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class PermissionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionsBinding

    private lateinit var permissionList: ArrayList<PermissionModel>
    private lateinit var adapter: PermissionsRvAdapter
    private var isMiuiClicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()
        binding = ActivityPermissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor()
        loadPermissionsList()
        loadPermissionAdapter()
        adjustFontScale(resources.configuration)
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
//                setListeners()

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

    fun adjustFontScale(configuration: Configuration) {
        if (configuration.fontScale > 1f || configuration.fontScale < 1f) {
            configuration.fontScale = 1f
            val metrics = resources.displayMetrics
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }

    private fun loadPermissionAdapter() {
        adapter =
            PermissionsRvAdapter(permissionList, object : PermissionsRvAdapter.OnItemClickListener {
                override fun onItemClick(permission: PermissionModel, position: Int) {
                    when (permission.scheme) {
                        "location" -> {
                            requestPermissionForLocation()
                        }

                        "draw_overlay" -> {
                            requestForDrawOverlay()
                        }

                        "autoclicker" -> {
                            val dialog = AutoClickerDialog(this@PermissionsActivity)
                            dialog.show()
                        }

                        "battery_optimization" -> {
                            val batterySaverIntent = Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)
                            startActivity(batterySaverIntent)
                        }

                        "on_gps" -> {
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        }

                        "miui" -> {
                            isMiuiClicked = true
                            requestXiaomi()
                        }

                        "battery_ignoring" -> {
                            val intent = Intent()
                            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;
                            intent.data = Uri.parse("package:$packageName");
                            startActivity(intent)
                        }
                    }
                }
            })

        binding.rvPermissions.adapter = adapter
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

    private fun loadPermissionsList() {
        val list = intent.getStringArrayListExtra("permission") ?: emptyList<String>()

        permissionList = ArrayList()

        list.forEach {
            when (it) {
                "location" -> {
                    permissionList.add(
                        PermissionModel(
                            getString(R.string.location),
                            getString(R.string.location_request_info),
                            it
                        )
                    )
                }

                "autoclicker" -> {
                    permissionList.add(
                        PermissionModel(
                            "Autoclicker",
                            getString(R.string.autoclicker_des),
                            it,
                        )
                    )
                }

                "draw_overlay" -> {
                    permissionList.add(
                        PermissionModel(
                            getString(R.string.draw_overlay_title),
                            getString(R.string.draw_overlay_description),
                            it
                        )
                    )
                }

                "battery_optimization" -> {
                    permissionList.add(
                        PermissionModel(
                            getString(R.string.battery_title),
                            getString(R.string.battery_description),
                            it
                        )
                    )
                }

                "on_gps" -> {
                    permissionList.add(
                        PermissionModel(
                            getString(R.string.on_gps_title),
                            getString(R.string.on_gps_description),
                            it
                        )
                    )
                }

                "miui" -> {
                    permissionList.add(
                        PermissionModel(
                            getString(R.string.miui_title),
                            getString(R.string.miui_description),
                            it
                        )
                    )
                }

                "battery_ignoring" -> {
                    permissionList.add(
                        PermissionModel(
                            getString(R.string.battery_optimization),
                            getString(R.string.battery_description),
                            it
                        )
                    )
                }
            }
        }
    }

    private fun requestXiaomi() {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.setClassName(
            "com.miui.securitycenter",
            "com.miui.permcenter.permissions.PermissionsEditorActivity"
        )
        intent.putExtra("extra_pkgname", packageName)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        updateRv()
    }

    private fun requestForDrawOverlay() {
        val intent1 = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${packageName}")
        )
        startActivity(intent1)
    }

    private fun requestPermissionForLocation() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) { /* ... */
                    updateRv()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    token: PermissionToken?
                ) { /* ... */
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun updateRv() {

        var count = 0

        permissionList.forEach {
            when (it.scheme) {
                "location" -> {
                    it.granted =
                        checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                }

                "draw_overlay" -> {
                    it.granted = checkHasDrawOverlayPermissions()
                }

                "battery_optimization" -> {
                    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager

                    it.granted = !powerManager.isPowerSaveMode
                }

                "on_gps" -> {
                    val locationManager =
                        getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    it.granted = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                }

                "miui" -> {
                    it.granted = isMiuiClicked
                }

                "battery_ignoring" -> {
                    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        it.granted = powerManager.isIgnoringBatteryOptimizations(packageName)
                    } else {
                        it.granted = true
                    }
                }

                "autoclicker" -> {
                    lifecycleScope.launch {
                        delay(500)
                        it.granted =
                            !(AutoClickerDetector.isDetectingSimilarGestures && AutoClickerDetector.isAccessibilityServiceEnabled(
                                this@PermissionsActivity
                            ))
                    }
                }

            }

            if (it.granted) count++
        }

        adapter.notifyDataSetChanged()

        if (count == permissionList.size) {
            lifecycleScope.launch {
                delay(500)
                val intent = Intent(this@PermissionsActivity, SplashScreenActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return line
    }

    fun isMiUi(): Boolean {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"))
    }

    private fun checkHasDrawOverlayPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
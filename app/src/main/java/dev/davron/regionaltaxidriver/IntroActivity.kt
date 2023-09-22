package dev.davron.regionaltaxidriver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.davron.regionaltaxidriver.adapters.intro.IntroViewPagerAdapter
import dev.davron.regionaltaxidriver.databinding.ActivityIntroBinding
import dev.davron.regionaltaxidriver.databinding.ItemTabIndicatorBinding
import dev.davron.regionaltaxidriver.models.ScreenItem
import dev.davron.regionaltaxidriver.utils.MySharedPreferences
import dev.davron.regionaltaxidriver.utils.getAutoNightMode
import dev.davron.regionaltaxidriver.utils.statusBarColor
import java.text.SimpleDateFormat
import java.util.Date

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    private lateinit var adapter: IntroViewPagerAdapter
    private lateinit var list: ArrayList<ScreenItem>
    private lateinit var sharedPreferences: MySharedPreferences
    var position = 0
    private lateinit var btnAnimation: Animation

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setUpUI()

        loadList()

        adapter = IntroViewPagerAdapter(list)
//        binding.screenViewPager.removeOverScroll()
        binding.screenViewPager.adapter = adapter
//        binding.tabIndicator.setViewPager2(binding.screenViewPager)
        position = binding.screenViewPager.currentItem

        TabLayoutMediator(binding.tabIndicator, binding.screenViewPager) { tab, position ->
            val tabBinding = ItemTabIndicatorBinding.inflate(layoutInflater)

            if (position != 0) {
                tabBinding.root.background = null
            }

            tab.customView = tabBinding.root
        }.attach()

        binding.tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        if (position == list.size - 1) {
            binding.btnNext.setOnClickListener {
                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        binding.btnNext.setOnClickListener {

            if (position <= list.size) {
                position++
                binding.screenViewPager.currentItem = position

            }
//            if (position == list.size - 1) {  // when we rech to the last screen
//                binding.btnSkip.visibility = View.INVISIBLE
//                binding.btnNext.text = getString(R.string.tv_start)
//
//            } else {
//                binding.btnSkip.visibility = View.VISIBLE
//                binding.btnNext.text = getString(R.string.tv_next)
//            }

            if (position >= list.size) {
                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.btnPreview.setOnClickListener {
            if (position > 0) {
                position--
                binding.screenViewPager.currentItem = position

            }
        }

        binding.btnSkip.setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab?.position!!
                when (tab.position) {
                    0 -> {
                        binding.btnPreview.visibility = View.INVISIBLE
                        binding.btnSkip.visibility = View.VISIBLE
                        binding.btnNext.text = getString(R.string.tv_next)
                    }

                    1 -> {
                        binding.btnPreview.visibility = View.VISIBLE
                        binding.btnSkip.visibility = View.VISIBLE
                        binding.btnNext.text = getString(R.string.tv_next)
                    }

                    2 -> {
                        binding.btnPreview.visibility = View.VISIBLE
                        binding.btnSkip.visibility = View.INVISIBLE
                        binding.btnNext.text = getString(R.string.tv_start)
                    }

                    else -> {
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
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
                val newNightMode = getAutoNightMode()

                when (newNightMode) {
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
                    val nightMode = MySharedPreferences.getNightMode(this@IntroActivity)

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

    private fun loadList() {
        list = ArrayList()
        list.add(ScreenItem(getString(R.string.tv_intro1), R.drawable.intro1))
        list.add(ScreenItem(getString(R.string.tv_intro2), R.drawable.intro2))
        list.add(ScreenItem(getString(R.string.tv_intro3), R.drawable.intro3))
    }

    override fun onDestroy() {
        super.onDestroy()
        position = 0
    }
}
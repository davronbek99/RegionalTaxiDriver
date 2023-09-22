package dev.davron.regionaltaxidriver.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.fragment.app.FragmentActivity
import dev.davron.regionaltaxidriver.R
import java.text.SimpleDateFormat
import java.util.Date


fun FragmentActivity.statusBarColor(
    @ColorInt statusBarColor: Int,
    @ColorInt navigationBarColor: Int,
    darkStatusBarTint: Boolean
) {
    val win: Window = (window).also {
        it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        it.statusBarColor = statusBarColor
    }

    val dec = win.decorView
    if (darkStatusBarTint) {
        dec.systemUiVisibility = dec.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        dec.systemUiVisibility = 0
    }
}

@SuppressLint("SimpleDateFormat")
fun FragmentActivity.getAutoNightMode(): String {
    val hour = Date()
    val df = SimpleDateFormat("HH")
    val string = df.format(hour).toInt()

    return if (string < 6 || string > 18) "night" else "day"
}

fun returnSelectedDate(context: Context, day: Int, month: Int, year: Int): String {
    return "$day ${(month).toMonthName(context)} $year"
}

fun Int.toMonthName(context: Context): String {
    return when (this) {
        1 -> context.resources.getString(R.string.jan)
        2 -> context.resources.getString(R.string.feb)
        3 -> context.resources.getString(R.string.march)
        4 -> context.resources.getString(R.string.april)
        5 -> context.resources.getString(R.string.may)
        6 -> context.resources.getString(R.string.jun)
        7 -> context.resources.getString(R.string.july)
        8 -> context.resources.getString(R.string.aug)
        9 -> context.resources.getString(R.string.sep)
        10 -> context.resources.getString(R.string.oct)
        11 -> context.resources.getString(R.string.nov)
        12 -> context.resources.getString(R.string.dec)
        else -> {
            ""
        }
    }
}
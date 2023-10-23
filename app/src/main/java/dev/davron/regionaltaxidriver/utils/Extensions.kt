package dev.davron.regionaltaxidriver.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.platform.MaterialSharedAxis
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

fun View.showKeyboard() {
    this.requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
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

fun Fragment.setAnimations() {
    val exitTransation = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
    exitTransation.duration = 500

    exitTransition = exitTransation
    enterTransition = exitTransition


    val reenterTransation = MaterialSharedAxis(MaterialSharedAxis.X, false)
    reenterTransation.duration = 500

    reenterTransition = reenterTransation
    returnTransition = reenterTransition
}


fun isOnline(context: Context): Boolean {
    val connectionManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activityNetworkInfo = connectionManager.activeNetworkInfo

    return activityNetworkInfo != null && activityNetworkInfo.isConnected
}


fun String.editLikePhoneNumber(): String {
    return this.replace(" ", "").replace("(", "").replace(")", "").replace("-", "")
}


fun Fragment.setBottomAnimations() {
    val exitTransaction = MaterialSharedAxis(MaterialSharedAxis.Y, /* forward= */ true)
    exitTransaction.duration = 500

    exitTransition = exitTransaction
    enterTransition = exitTransition


    val reenterTransaction = MaterialSharedAxis(MaterialSharedAxis.Y, false)
    reenterTransaction.duration = 500

    reenterTransition = reenterTransaction
    returnTransition = reenterTransition
}

fun <T> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = false) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
    if (doBack) findNavController().popBackStack()
}


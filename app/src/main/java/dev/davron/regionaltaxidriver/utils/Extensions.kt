package dev.davron.regionaltaxidriver.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.os.Build
import android.util.Base64
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.platform.MaterialSharedAxis
import dev.davron.regionaltaxidriver.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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


fun getRotationRightBitmap(file: File): Bitmap {
    var bitmap: Bitmap = BitmapFactory.decodeFile(file.path)

    val ei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ExifInterface(file.absolutePath)
    } else {
        ExifInterface(file.absolutePath)
    }
    val orientation = ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )

    var rotatedBitmap: Bitmap? = null
    rotatedBitmap = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotatedImage(bitmap, 90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotatedImage(bitmap, 180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotatedImage(bitmap, 270f)
        ExifInterface.ORIENTATION_NORMAL -> bitmap
        else -> bitmap
    }

    return rotatedBitmap
}

fun rotatedImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}

fun Bitmap.flipHorizontal(): Bitmap {
    val matrix = Matrix()
    matrix.postScale(-1f, 1f, width / 2.0f, height / 2.0f)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun <T> Fragment.getBackStackData(
    key: String, singleCall: Boolean = true, result: (T) -> (Unit)
) {
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
        ?.observe(viewLifecycleOwner) {
            result(it)
            if (singleCall) findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(
                key
            )
        }
}


fun File.getExtension(): String {
    val encoded: String = try {
        URLEncoder.encode(name, "UTF-8").replace("+", "%20")
    } catch (e: Exception) {
        name
    }

    return MimeTypeMap.getFileExtensionFromUrl(encoded).toLowerCase(Locale.getDefault())
}


fun File.imageFileToBase64(): String {
    var bitmap = getRotationRightBitmap(this)
//    if (Common.currentCameraType == 0) {
//        bitmap = rotatedImage(bitmap, -90f)
//    } else {
//        bitmap = rotatedImage(bitmap, 0f)
//    }
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
    val byteArray: ByteArray = stream.toByteArray()
    var encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)

    encodedString = "data:image/jpeg;base64,$encodedString"

    return encodedString
}
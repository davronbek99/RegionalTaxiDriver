package dev.davron.regionaltaxidriver.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import androidx.lifecycle.MutableLiveData

@SuppressLint("StaticFieldLeak")
object Common {

    var state: String = ""

    var myLocation: Location? = null

    var mutableLocation: MutableLiveData<Location> = MutableLiveData()

    var todayAllPrice = 0
    var allNewsCounter = 1
    var isFirst = true

    var rideId = -1

    var isOpenedMap = false

    var districtId = -1

    ///global variables

    var currentCameraType = -1

    lateinit var globalContext: Context

    var packageName = ""
    var accountStatus = "new"

    var tempToken = ""

    //profile editing
    var isPhotoChangingForFragments = false
    var successLiveData: MutableLiveData<Int> = MutableLiveData(-1)
    var fromMutableLiveData: MutableLiveData<String?> = MutableLiveData(null)
    var bitmapMutableLiveData: MutableLiveData<Bitmap?> = MutableLiveData(null)


}
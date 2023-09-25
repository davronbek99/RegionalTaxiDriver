package dev.davron.regionaltaxidriver.utils

import android.location.Location
import android.os.Handler
import android.os.Looper
import dev.davron.regionaltaxidriver.modelApi.socket.CityNewOrder

object Convertor {

    var firsTime = true

    var cityMessageCounter = 0
    var isFirstHomeFragment = true
    var isSoundPlayed = false
    var maxSeekBar: Int = 100
    var seekbarMaxSetted: Boolean = false
    var isDestroyed = true

    var statusDriver = ""
    var hasCityOrder = false

    var isPost = false
    var taxi = false

    var isCancelled = false


    //birinchi map page kirganda
    var firstCityUi = true

    var isPaused = false
    var pause = false

    //socket kelganda order olish
    var clientOrder = false

    //klent oldiga borganda kutub turish
    var waitTime = false

    //client tugatgandan sung
    var finished = false

    //clentga rating berish
    var rating = false

    //klent ORderID
    var orderId = ""

    //yol nuqtalari hisoblanyaptimi
    var isRouteCalculating = false

    //city taxi yol lokatsiyalari listi
    var listRouteLocations = ArrayList<Location>()

    var cityNewOrder: CityNewOrder? = null

    var authorFragment = false

    var isOnline = false

    var nowPage = false

    var location: Location? = null

    var phoneNumber = ""

    var countInterArea = 0

    var countCity = 0

    var isOnlineService = false

    var isOne = false

    var startStopLocationGathering = false

    //wait time
    var waitTimeDuration = 0

    var diffWaitTime = 0

    var timer = Handler(Looper.getMainLooper())
    var timeDuration = 0
    var isTimeCalculating = false

    val runnable = object : Runnable {
        override fun run() {
            if (isTimeCalculating) {
                timeDuration++
                timer.postDelayed(this, 1000)
            }
        }
    }

    var rideTimeDuration = 0

//    fun stopRideTime() {
//        rideTimeDuration += timeDuration
//        isTimeCalculating = false
//        timeDuration = 0
//    }

//    fun stopDiffWaitTime() {
//        diffWaitTime = timeDuration
//        isTimeCalculating = false
//        timeDuration = 0
//    }

//    fun startWaitTime() {
//        isTimeCalculating = true
//        timeDuration = 0
//        timer.postDelayed(runnable, 1000)
//    }

//    fun stopWaitTime() {
//        waitTimeDuration = timeDuration
//        isTimeCalculating = false
//        timeDuration = 0
//    }


    fun startGatherLocation() {
        isRouteCalculating = true
        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                if (startStopLocationGathering) {
                    isRouteCalculating = true
                    handler.postDelayed(this, 15 * 1000)
                }
            }
        }
        handler.postDelayed(runnable, 15 * 1000)

    }

    //locationlar listini olinganda uning masofasini hisoblash
    fun getDistanceInMetres(list: ArrayList<Location>): Double {
        var distance = 0.0

        for (i in 0 until list.size - 1) {

            val distanceOne = (list[i].distanceTo(list[i + 1])).toDouble()

            //agar orasidagi masofa 10metrdan kichik bolsa joyidan qo'zg'almagan
            if (distanceOne >= 10.0) {
                distance += distanceOne
            }
        }

        return distance
    }

    fun formatTextMoney(amount: String): String {
        var formattedString = ""
        if (amount.contains(".")) {
            val length = amount.split(".")[0].length
            val integerPart = amount.split(".")[0]
            if (length <= 3) {
                formattedString = amount
            } else {
                var counter: Int = 0
                val value = StringBuilder()
                for (i in (length - 1) downTo 0) {
                    val emptyString: String = " "
                    counter++
                    if (counter % 3 == 0) {
                        value.append(emptyString)
                        value.append(integerPart[i])
                    } else {
                        value.append(integerPart[i])
                    }
                }
                formattedString = value.toString().reversed() + "." + amount.split(".")[1]
            }
        } else {
            val length = amount.length
            if (length <= 3) {
                formattedString = amount
            } else {
                var counter: Int = 0
                val value = StringBuilder()
                for (i in (length - 1) downTo 0) {

                    val emptyString: String = " "
                    counter++
                    if (counter % 3 == 0) {
                        value.append(emptyString)
                        value.append(amount[i])
                    } else {
                        value.append(amount[i])
                    }
                }
                formattedString = value.toString().reversed()
            }
        }

        return formattedString
    }

    fun convertNumber(phone: String): String {
        val replace = phone.replace(" ", "")
        val stringBuilder = StringBuilder()
        stringBuilder.append(replace.subSequence(0, 4))
        stringBuilder.append(" ")
        stringBuilder.append(replace.subSequence(4, 6))
        stringBuilder.append(" ")
        stringBuilder.append(replace.subSequence(7, 9))
        stringBuilder.append(" ")
        stringBuilder.append("****")
        return stringBuilder.toString()
    }
}
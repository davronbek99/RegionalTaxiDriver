package dev.davron.regionaltaxidriver.utils

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import java.util.*

object MainFun {

    fun isAppRunning(context: Context, packageName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false

        for (appProcess in appProcesses) {
            if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == packageName) {
                return true
            }
        }
        return false
    }

    fun sortNum(num: Int): String {
        var result = ""
        var sNum = num.toString()

        for (i in 1..sNum.length / 3) {
            result = "${sNum.substring(sNum.length - 3)} $result"
            sNum = sNum.substring(0, sNum.length - 3)
        }

        return if (sNum.isNotEmpty()) "$sNum $result " else "$result"
    }

    fun langPrice(context: Context): String {
        val lang = MySharedPreferences.getLang(context)
        if (lang == "uz") {
            return "so'm"
        } else if (lang == "ru") {
            return "сум"
        } else {
            return "price"
        }
    }


    fun parcelCount(context: Context): String {
        val lang = MySharedPreferences.getLang(context)
        if (lang == "uz") {
            return "dona"
        }
        if (lang == "ru") {
            return "шт"
        } else {
            return "count"
        }
    }

    fun mouthType(context: Context, mouth: String): String {
        val lang = MySharedPreferences.getLang(context)
        var result = ""
        if (lang == "ru") {
            if (mouth == "1") {
                result = "Январь"
            }
            if (mouth == "2") {
                result = "Февраль"
            }
            if (mouth == "3") {
                result = "Март"
            }
            if (mouth == "4") {
                result = "Aпрель"
            }
            if (mouth == "5") {
                result = "Май"
            }
            if (mouth == "6") {
                result = "Июнь"
            }
            if (mouth == "7") {
                result = "Июль"
            }
            if (mouth == "8") {
                result = "Август"
            }
            if (mouth == "9") {
                result = "Сентябрь"
            }
            if (mouth == "10") {
                result = "Октябрь"
            }
            if (mouth == "11") {
                result = "Ноябрь"
            }
            if (mouth == "12") {
                result = "Декабрь"
            }
        }

        if (lang == "uz") {
            if (mouth == "1") {
                result = "Yanvar"
            }
            if (mouth == "2") {
                result = "Fevral"
            }
            if (mouth == "3") {
                result = "Mart"
            }
            if (mouth == "4") {
                result = "Aprel"
            }
            if (mouth == "5") {
                result = "May"
            }
            if (mouth == "6") {
                result = "Iyun"
            }
            if (mouth == "7") {
                result = "Iyul"
            }
            if (mouth == "8") {
                result = "Avgust"
            }
            if (mouth == "9") {
                result = "Setyabr"
            }
            if (mouth == "10") {
                result = "Oktabr"
            }
            if (mouth == "11") {
                result = "Noyabr"
            }
            if (mouth == "12") {
                result = "Dekabr"
            }
        } else if (lang == "en") {
            if (mouth == "1") {
                result = "January"
            }
            if (mouth == "2") {
                result = "February"
            }
            if (mouth == "3") {
                result = "March"
            }
            if (mouth == "4") {
                result = "April"
            }
            if (mouth == "5") {
                result = "May"
            }
            if (mouth == "6") {
                result = "June"
            }
            if (mouth == "7") {
                result = "July"
            }
            if (mouth == "8") {
                result = "August"
            }
            if (mouth == "9") {
                result = "September"
            }
            if (mouth == "10") {
                result = "October"
            }
            if (mouth == "11") {
                result = "November"
            }
            if (mouth == "12") {
                result = "December"
            }
        }

        return result

    }
}
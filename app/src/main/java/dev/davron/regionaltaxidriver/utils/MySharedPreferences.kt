package dev.davron.regionaltaxidriver.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object MySharedPreferences {
    private val NAME = "exam"
    private val MODE = Context.MODE_PRIVATE
    private val settings: SharedPreferences? = null
    private const val keyPurchase = "app_purchased"

    //    token
    @SuppressLint("ApplySharedPref")
    fun addToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token).commit()
    }

    fun getToken(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", "") ?: ""
    }

    /** change theme app */
    @SuppressLint("ApplySharedPref")
    fun setNightMode(context: Context, nightMode: String) {
        val sharedPreferences = context.getSharedPreferences("nightMode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("nightMode", nightMode).commit()
    }

    fun getNightMode(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("nightMode", Context.MODE_PRIVATE)
        return sharedPreferences.getString("nightMode", "auto") ?: "auto"
    }

    //    language
    @SuppressLint("ApplySharedPref")
    fun setLang(context: Context, lang: String) {
        val sharedPreferences = context.getSharedPreferences("lang", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("lang", lang).commit()
    }

    fun getLang(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("lang", Context.MODE_PRIVATE)
        return sharedPreferences.getString("lang", "en") ?: "en"
    }

    fun setIntro(context: Context, isHave: Boolean) {
        val sharedPreferences = context.getSharedPreferences("have", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("have", isHave).apply()
    }

    fun getIntro(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("have", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("have", false)
    }

    @SuppressLint("ApplySharedPref")
    fun setUserType(context: Context, userType: String) {
        val sharedPreferences = context.getSharedPreferences("user_type", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_type", userType).commit()
    }

    fun getUserType(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("user_type", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_type", "") ?: ""
    }

    @SuppressLint("ApplySharedPref")
    fun addData(context: Context, token: String) {
        /** yozish  uchun */
        val sharedrefrence = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putString("token", token).commit()
    }

    fun getData(context: Context): String? {
        val sharedrefrence = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        /** uqish uchun */
        return sharedrefrence.getString("token", "")

    }

    fun setStatus(context: Context, status: String) {
        val shared = context.getSharedPreferences("status", Context.MODE_PRIVATE)
        val editor = shared.edit()
        editor.putString("status", status).commit()
    }

    fun getStatus(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("status", Context.MODE_PRIVATE)
        return sharedPreferences.getString("status", "new") ?: "new"
    }
}
package com.kachalov.weather

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kachalov.weather.constants.Keys
import com.kachalov.weather.constants.Preferences

abstract class BaseActivity : AppCompatActivity() {
    private var preferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initPreferences()
        updateTheme()
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        preferences = null
        super.onDestroy()
    }

    private fun updateTheme() {
        if (isDarkTheme()) {
            setTheme(R.style.AppDarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }
    }

    private fun initPreferences() {
        preferences = getSharedPreferences(Preferences.THEMES, Context.MODE_PRIVATE)
    }

    protected fun isDarkTheme(): Boolean {
        return preferences?.getBoolean(Keys.IS_DARK_THEME, true) ?: false
    }

    protected fun setDarkTheme(isDarkTheme: Boolean) {
        preferences?.edit()
            ?.putBoolean(Keys.IS_DARK_THEME, isDarkTheme)
            ?.apply()
    }
}

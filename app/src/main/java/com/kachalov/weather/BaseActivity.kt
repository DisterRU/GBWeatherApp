package com.kachalov.weather

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kachalov.weather.constants.Keys
import com.kachalov.weather.constants.Preferences

abstract class BaseActivity : AppCompatActivity() {
    private var themePreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initPreferences()
        updateTheme()
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        themePreferences = null
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
        themePreferences = getSharedPreferences(Preferences.THEMES, Context.MODE_PRIVATE)
    }

    protected fun isDarkTheme(): Boolean {
        return themePreferences?.getBoolean(Keys.IS_DARK_THEME, false) ?: false
    }

    protected fun setDarkTheme(isDarkTheme: Boolean) {
        themePreferences?.edit()
            ?.putBoolean(Keys.IS_DARK_THEME, isDarkTheme)
            ?.apply()
    }
}

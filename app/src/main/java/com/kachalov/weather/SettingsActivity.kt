package com.kachalov.weather

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.kachalov.weather.constants.Keys
import com.kachalov.weather.constants.Preferences
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {
    private var pressurePreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initPressurePreferences()
        initSwitch()
        supportActionBar?.title = resources.getString(R.string.action_settings)
    }

    private fun initPressurePreferences() {
        pressurePreferences = getSharedPreferences(Preferences.WEATHER, Context.MODE_PRIVATE)
    }

    private fun initSwitch() {
        darkThemeSwitch.isChecked = isDarkTheme()
        darkThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            setDarkTheme(isChecked)
            recreate()
        }

        pressureSwitch.isChecked = getPressure()
        pressureSwitch.setOnCheckedChangeListener { _, isChecked ->
            setPressure(isChecked)
        }
    }

    private fun setPressure(showPressure: Boolean) {
        pressurePreferences?.edit()
            ?.putBoolean(Keys.PRESSURE, showPressure)
            ?.apply()
    }

    private fun getPressure(): Boolean {
        return pressurePreferences?.getBoolean(Keys.PRESSURE, false) ?: false
    }

    override fun onDestroy() {
        pressurePreferences = null
        super.onDestroy()
    }
}

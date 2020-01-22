package com.kachalov.weather

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initSwitch()
    }

    private fun initSwitch() {
        darkThemeSwitch.isChecked = isDarkTheme()
        darkThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            setDarkTheme(isChecked)
            recreate()
        }
    }
}

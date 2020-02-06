package com.kachalov.weather

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kachalov.weather.constants.Codes
import com.kachalov.weather.constants.Fragments
import com.kachalov.weather.constants.Keys
import com.kachalov.weather.constants.Preferences
import com.kachalov.weather.entities.City
import com.kachalov.weather.livedata.CitiesLiveData
import com.kachalov.weather.livedata.PressureLiveData
import com.kachalov.weather.utils.FragmentChanger
import com.kachalov.weather.utils.FragmentFinder

class MainActivity : BaseActivity(), FragmentChanger {
    private lateinit var fragmentFinder: FragmentFinder

    private val citiesLiveData = CitiesLiveData.CITIES
    private val pressureLiveData = PressureLiveData.PRESSURE
    private var citiesPreferences: SharedPreferences? = null
    private var weatherPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragmentFinder()
        initPreferences()
        initCitiesModel()
        initPressureModel()
        changeFragment(Fragments.CITIES)
    }

    private fun initPressureModel() {
        pressureLiveData.value = weatherPreferences?.getBoolean(Keys.PRESSURE, false) ?: false
    }

    private fun initPreferences() {
        citiesPreferences = getSharedPreferences(Preferences.CITIES, Context.MODE_PRIVATE)
        weatherPreferences = getSharedPreferences(Preferences.WEATHER, Context.MODE_PRIVATE)
    }

    private fun initCitiesModel() {
        citiesLiveData.value = getCities()
    }

    private fun getCities(): MutableList<City> {
        val json = citiesPreferences?.getString(Keys.CITIES, "")
        return if (json.isNullOrBlank()) {
            mutableListOf()
        } else {
            val gson = Gson()
            val type = object : TypeToken<MutableList<City>>() {}.type
            gson.fromJson(json, type)
        }
    }

    private fun clearCities() {
        citiesLiveData.value = mutableListOf()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onDestroy() {
        saveCities()
        clearPreferences()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        saveCities()
        clearPreferences()
        super.onSaveInstanceState(outState)
    }

    private fun clearPreferences() {
        citiesPreferences = null
        weatherPreferences = null
    }

    private fun saveCities() {
        val gson = Gson()
        citiesPreferences?.edit()
            ?.putString(Keys.CITIES, gson.toJson(citiesLiveData.value))
            ?.apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivityForResult(intent, Codes.SETTINGS_CODE)
                true
            }
            R.id.action_clear -> {
                clearCities()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Codes.SETTINGS_CODE) {
                recreate()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initFragmentFinder() {
        fragmentFinder = FragmentFinder(supportFragmentManager)
    }

    override fun changeFragment(tag: String, args: Bundle?, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = fragmentFinder.findFragment(tag, args)
        transaction.replace(R.id.fragment_container, fragment, tag)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commitAllowingStateLoss()
    }

    override fun showDialog(tag: String) {
        val dialog = fragmentFinder.findDialog(tag)
        dialog.show(supportFragmentManager, tag)
    }
}

package com.kachalov.weather

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.kachalov.weather.constants.Codes
import com.kachalov.weather.constants.Fragments
import com.kachalov.weather.constants.Preferences
import com.kachalov.weather.observers.CitiesObserver
import com.kachalov.weather.utils.FragmentChanger
import com.kachalov.weather.utils.FragmentFinder

class MainActivity : BaseActivity(), FragmentChanger {
    private lateinit var fragmentFinder: FragmentFinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragmentFinder()
        changeFragment(Fragments.CITIES)
    }

    private fun clearCities() {
        getSharedPreferences(Preferences.CITIES, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
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
                recreate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Codes.SETTINGS_CODE) {
            recreate()
        }
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

    override fun showDialog(tag: String, observers: List<CitiesObserver>) {
        val dialog = fragmentFinder.showDialog(tag, observers)
        dialog.show(supportFragmentManager, tag)
    }
}

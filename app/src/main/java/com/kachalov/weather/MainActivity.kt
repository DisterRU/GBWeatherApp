package com.kachalov.weather

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.kachalov.weather.constants.Fragments
import com.kachalov.weather.constants.Preferences
import com.kachalov.weather.observers.CitiesObserver
import com.kachalov.weather.utils.FragmentChanger
import com.kachalov.weather.utils.FragmentFinder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragmentChanger {
    private lateinit var fragmentFinder: FragmentFinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
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
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
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

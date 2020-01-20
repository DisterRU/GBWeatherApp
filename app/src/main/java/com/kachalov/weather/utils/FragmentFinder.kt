package com.kachalov.weather.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.kachalov.weather.constants.Fragments

//import com.kachalov.weather.ui.cities.CitiesFragment
//import com.kachalov.weather.ui.cityinfo.CityInfoFragment
//import com.kachalov.weather.ui.settings.SettingsFragment

class FragmentFinder(private val fragmentManager: FragmentManager) {
    fun getFragment(tag: String, args: Bundle?): Fragment {
        val fragment = fragmentManager.findFragmentByTag(tag) ?: when (tag) {
//            Fragments.CITIES -> CitiesFragment()
//            Fragments.CITY_INFO -> CityInfoFragment()
//            Fragments.SETTINGS -> SettingsFragment()
            else -> throw RuntimeException("Invalid fragment tag")
        }
        if (args != null) {
            fragment.arguments = args
        }
        return fragment
    }
}
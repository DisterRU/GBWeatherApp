package com.kachalov.weather.utils

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.kachalov.weather.constants.Dialogs
import com.kachalov.weather.constants.Fragments
import com.kachalov.weather.ui.addcity.AddCityFragment
import com.kachalov.weather.ui.cities.CitiesFragment
import com.kachalov.weather.ui.cityinfo.CityInfoFragment

class FragmentFinder(private val fragmentManager: FragmentManager) {
    fun findFragment(tag: String, args: Bundle?): Fragment {
        val fragment = fragmentManager.findFragmentByTag(tag) ?: when (tag) {
            Fragments.CITIES -> CitiesFragment()
            Fragments.CITY_INFO -> CityInfoFragment()
            else -> throw RuntimeException("Invalid fragment tag")
        }
        if (args != null) {
            fragment.arguments = args
        }
        return fragment
    }

    fun showDialog(tag: String): DialogFragment {
        return when (tag) {
            Dialogs.ADD_CITY -> AddCityFragment()
            else -> throw RuntimeException("Invalid dialog tag")
        }
    }
}
package com.kachalov.weather.utils

import android.os.Bundle
import com.kachalov.weather.observers.CitiesObserver

interface FragmentChanger {
    fun changeFragment(tag: String, args: Bundle? = null, addToBackStack: Boolean = false)
    fun showDialog(tag: String, observers: List<CitiesObserver>)
}
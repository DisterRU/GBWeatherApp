package com.kachalov.weather.utils

import android.os.Bundle

interface FragmentChanger {
    fun changeFragment(tag: String, args: Bundle? = null, addToBackStack: Boolean = false)
    fun showDialog(tag: String)
}
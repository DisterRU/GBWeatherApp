package com.kachalov.weather.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PressureViewModel : ViewModel() {
    val pressure by lazy {
        MutableLiveData<Boolean>()
    }

    companion object {
        val INSTANCE by lazy { PressureViewModel() }
    }
}
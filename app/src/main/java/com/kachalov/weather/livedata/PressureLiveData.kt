package com.kachalov.weather.livedata

import androidx.lifecycle.MutableLiveData

class PressureLiveData {
    companion object {
        val PRESSURE by lazy {
            MutableLiveData<Boolean>()
        }
    }
}
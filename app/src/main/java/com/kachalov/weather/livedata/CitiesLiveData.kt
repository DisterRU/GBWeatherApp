package com.kachalov.weather.livedata

import androidx.lifecycle.MutableLiveData
import com.kachalov.weather.entities.City


class CitiesLiveData {
    companion object {
        val CITIES by lazy {
            MutableLiveData<MutableList<City>>().apply {
                value = mutableListOf()
            }
        }
    }
}
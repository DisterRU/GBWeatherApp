package com.kachalov.weather.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kachalov.weather.entities.City


class CitiesViewModel : ViewModel() {
    val cities by lazy {
        MutableLiveData<List<City>>().apply {
            value = listOf()
        }
    }

    companion object {
        val INSTANCE by lazy { CitiesViewModel() }
    }
}
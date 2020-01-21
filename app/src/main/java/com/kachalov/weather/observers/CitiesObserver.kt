package com.kachalov.weather.observers

import com.kachalov.weather.persistence.City

interface CitiesObserver {
    fun updateCities(cities: List<City>)
}
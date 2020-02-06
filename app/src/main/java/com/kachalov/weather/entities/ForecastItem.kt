package com.kachalov.weather.entities

data class ForecastItem(
    val main: MainWeather,
    val weather: Weather,
    val dt: Long
)

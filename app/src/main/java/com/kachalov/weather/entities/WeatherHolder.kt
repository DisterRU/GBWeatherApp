package com.kachalov.weather.entities

data class WeatherHolder(
    val weather: List<Weather>,
    val main: MainWeather
)
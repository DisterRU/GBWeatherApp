package com.kachalov.weather.entities

data class WeatherHolder(
    val weather: List<WeatherItem>,
    val main: MainWeather
)
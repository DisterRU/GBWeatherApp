package com.kachalov.weather.entities

import com.google.gson.annotations.SerializedName

data class ForecastItem(
    val main: MainWeather,
    val weather: List<WeatherItem>,
    val dt: Long,
    @SerializedName("dt_txt")
    val dtTxt: String
)

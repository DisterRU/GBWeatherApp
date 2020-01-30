package com.kachalov.weather.entities

import java.io.Serializable

data class Forecast(
    val time: String,
    val icon: Int,
    val temp: Int
) : Serializable
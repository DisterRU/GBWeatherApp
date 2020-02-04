package com.kachalov.weather.entities

import java.io.Serializable

data class City(
    val name: String,
    val temp: Int,
    val icon: Int,
    val pressure: Int,
    val forecastList: List<Forecast>
) : Serializable
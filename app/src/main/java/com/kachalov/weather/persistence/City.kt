package com.kachalov.weather.persistence

import java.io.Serializable

data class City(
    val name: String,
    val temp: Int,
    val icon: Int,
    val pressure: Int
) : Serializable
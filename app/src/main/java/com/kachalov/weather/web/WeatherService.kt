package com.kachalov.weather.web

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kachalov.weather.R
import com.kachalov.weather.constants.Urls
import com.kachalov.weather.entities.City
import com.kachalov.weather.entities.Forecast
import com.kachalov.weather.entities.ForecastHolder
import com.kachalov.weather.entities.WeatherHolder
import com.kachalov.weather.livedata.CitiesLiveData
import kotlinx.coroutines.*
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.math.round

object WeatherService {
    private val cities = CitiesLiveData.CITIES
    private val gson by lazy { Gson() }

    fun addCity(cityName: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val city = getCity(cityName)
            cities.value?.add(city)
        }
    }

    fun addForecasts(city: City) {
        GlobalScope.launch(Dispatchers.IO) {
            city.forecastList = getForecasts(city.name)
        }
    }

    private suspend fun getCity(cityName: String): City {
        return withContext(Dispatchers.IO) {
            val weatherDeferred = async { getWeather(cityName) }
            buildCity(cityName, weatherDeferred.await())
        }
    }

    private fun buildCity(name: String, weather: WeatherHolder): City {
        return City(
            name = name,
            icon = parseIcon(weather.weather.first().main),
            temp = parseTemp(weather.main.temp),
            pressure = weather.main.pressure,
            forecastList = null
        )
    }

    private fun getWeather(cityName: String): WeatherHolder {
        val url = Urls.OWM_WEATHER + Urls.OWM_CITY + cityName + Urls.OWM_API_KEY
        val resultJson = getPage(url)
        return parseToWeatherHolder(resultJson)
    }

    private fun parseToWeatherHolder(json: String): WeatherHolder {
        val type = object : TypeToken<WeatherHolder>() {}.type
        return gson.fromJson(json, type)
    }

    private fun getPage(url: String): String {
        val connection = URL(url).openConnection() as HttpsURLConnection
        connection.requestMethod = "GET"
        try {
            connection.connect()
            return connection.inputStream.bufferedReader().readText()
        } finally {
            connection.disconnect()
        }
    }

    private suspend fun getForecasts(cityName: String): List<Forecast> {
        return withContext(Dispatchers.IO) {
            val url = Urls.OWM_WEATHER + Urls.OWM_FORECAST + cityName + Urls.OWM_API_KEY
            val forecastsJson = getPage(url)
            parseToForecasts(forecastsJson)
        }
    }

    private fun parseToForecasts(json: String): List<Forecast> {
        val type = object : TypeToken<ForecastHolder>() {}.type
        val forecastHolder = gson.fromJson<ForecastHolder>(json, type)
        return forecastHolder.list.map {
            Forecast(
                time = parseDate(it.dtTxt),
                temp = parseTemp(it.main.temp),
                icon = parseIcon(it.weather.first().main)
            )
        }
    }

    private fun parseIcon(weather: String): Int {
        return R.drawable.cloud
    }

    private fun parseTemp(temp: Float): Int {
        return round(temp - 273.15).toInt()
    }

    private fun parseDate(date: String): String {
        val dates = date.split("\\D".toRegex())
        val month = dates[1]
        val day = dates[2]
        val hour = dates[3]
        val minutes = dates[4]
        return "$day.$month\n$hour:$minutes"
    }
}

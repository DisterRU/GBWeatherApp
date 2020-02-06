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
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.math.round

object WeatherService {
    private val cities = CitiesLiveData.CITIES
    private val gson by lazy { Gson() }

    suspend fun addCity(cityName: String) {
        val currentWeather = getCurrentWeather(cityName)
        val forecasts = getForecasts(cityName)
        val city = GlobalScope.async {
            City(
                name = cityName,
                icon = parseIcon(currentWeather.weather.first().main),
                temp = parseTemp(currentWeather.main.temp),
                pressure = currentWeather.main.pressure,
                forecastList = forecasts
            )
        }
        cities.value?.add(city.await())
    }

    private suspend fun getCurrentWeather(cityName: String): WeatherHolder {
        val url = Urls.OWM_WEATHER + Urls.OWM_CITY + cityName + Urls.OWM_API_KEY
        val resultJson = getPage(url)
        return parseToWeatherHolder(resultJson)
    }

    private fun parseToWeatherHolder(json: String): WeatherHolder {
        val type = object : TypeToken<WeatherHolder>() {}.type
        return gson.fromJson(json, type)
    }

    private suspend fun getPage(url: String): String {
        return withContext(Dispatchers.IO) {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.readTimeout = 100
            try {
                connection.connect()
                return@withContext connection.inputStream.bufferedReader().readText()
            } catch (e: Exception) {

            } finally {
                connection.disconnect()
            }
            ""
        }
    }

    private fun getForecasts(cityName: String): List<Forecast> {
        val url = Urls.OWM_WEATHER + Urls.OWM_FORECAST + cityName + Urls.OWM_API_KEY
        var resultJson = ""
        GlobalScope.launch {
            resultJson = getPage(url)
        }
        if (resultJson.isBlank()) {
            throw RuntimeException("Result Json is empty")
        }
        return parseToForecasts(resultJson)
    }

    private fun parseToForecasts(json: String): List<Forecast> {
        val type = object : TypeToken<ForecastHolder>() {}.type
        val forecastHolder = gson.fromJson(json, type) as ForecastHolder
        return forecastHolder.list.map {
            Forecast(
                time = parseDate(it.dt),
                temp = parseTemp(it.main.temp),
                icon = parseIcon(it.weather.main)
            )
        }
    }

    private fun parseIcon(weather: String): Int {
        return R.drawable.cloud
    }

    private fun parseTemp(temp: Float): Int {
        return round(temp + 273.15).toInt()
    }

    private fun parseDate(date: Long): String {
        val parsedDate = Date(date)
        val calendar = Calendar.getInstance()
        calendar.time = parsedDate
        return "${calendar.get(Calendar.DAY_OF_MONTH)}.${calendar.get(Calendar.MONTH)}/n" +
                "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
    }
}

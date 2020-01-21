package com.kachalov.weather.observers

interface CitiesChanger {
    fun addObserver(observer: CitiesObserver)
    fun removeObserver(observer: CitiesObserver)
    fun notifyObservers()
}
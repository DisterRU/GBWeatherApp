package com.kachalov.weather.ui.cityinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kachalov.weather.R
import com.kachalov.weather.constants.Keys
import com.kachalov.weather.entities.City
import kotlinx.android.synthetic.main.fragment_city_info.*
import java.util.*

class CityInfoFragment : Fragment() {
    private lateinit var currentCity: City
    private var showPressure: Boolean = false

    private val today = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initCurrentCity(savedInstanceState)
        initPressure(savedInstanceState)
        setViewsData()
    }

    private fun setViewsData() {
        setMainData()
        setForecastData()
    }

    private fun setForecastData() {
        forecastRecycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setMainData() {
        cityName.text = currentCity.name
        cityTemp.text = currentCity.temp.toString()
        cityIcon.setImageResource(currentCity.icon)
        pressure.text = currentCity.pressure.toString()
        setPressureVisibility()
    }

    private fun setPressureVisibility() {
        if (showPressure) {
            setPressureVisibility(View.VISIBLE)
        } else {
            setPressureVisibility(View.GONE)
        }
    }

    private fun setPressureVisibility(visibility: Int) {
        pressureLayout.visibility = visibility
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        initPressure(savedInstanceState)
        initCurrentCity(savedInstanceState)
        super.onViewStateRestored(savedInstanceState)
    }

    private fun initCurrentCity(savedInstanceState: Bundle?) {
        currentCity = if (savedInstanceState == null) {
            arguments?.getSerializable(Keys.CURRENT_CITY) as City
        } else {
            savedInstanceState.getSerializable(Keys.CURRENT_CITY) as City
        }
    }

    private fun initPressure(savedInstanceState: Bundle?) {
        showPressure = savedInstanceState?.getBoolean(Keys.PRESSURE)
            ?: arguments?.getBoolean(Keys.PRESSURE)
                    ?: false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(Keys.CURRENT_CITY, currentCity)
        outState.putBoolean(Keys.PRESSURE, showPressure)
        super.onSaveInstanceState(outState)
    }
}
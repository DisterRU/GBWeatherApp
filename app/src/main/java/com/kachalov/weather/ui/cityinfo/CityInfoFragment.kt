package com.kachalov.weather.ui.cityinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kachalov.weather.R
import com.kachalov.weather.constants.Keys
import com.kachalov.weather.constants.Urls
import com.kachalov.weather.entities.City
import kotlinx.android.synthetic.main.fragment_city_info.*

class CityInfoFragment : Fragment() {
    private lateinit var currentCity: City
    private var showPressure: Boolean = false

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
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        forecastRecycler.adapter = ForecastAdapter(currentCity.forecastList)
        forecastRecycler.setHasFixedSize(true)
        forecastRecycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
    }

    private fun setMainData() {
        cityName.text = currentCity.name
        cityTemp.text = currentCity.temp.toString()
        cityIcon.setImageResource(currentCity.icon)
        pressure.text = currentCity.pressure.toString()
        setPressureVisibility()
        setCityNameOnClickListener()
    }

    private fun setCityNameOnClickListener() {
        val language = context?.resources?.configuration?.locale?.language ?: "en"
        cityName.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    Urls.HTTPS + language + Urls.WIKI + currentCity.name
                )
            )
            startActivity(intent)
        }
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
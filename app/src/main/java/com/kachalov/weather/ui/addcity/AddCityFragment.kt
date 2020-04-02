package com.kachalov.weather.ui.addcity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.kachalov.weather.R
import com.kachalov.weather.constants.Patterns
import com.kachalov.weather.livedata.CitiesLiveData
import com.kachalov.weather.web.WeatherService
import kotlinx.android.synthetic.main.fragment_add_city.*

class AddCityFragment : BottomSheetDialogFragment() {
    private val citiesLiveData = CitiesLiveData.CITIES
    private val cities = citiesLiveData.value?.toMutableList() ?: mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initButton()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initButton() {
        addCityButton.setOnClickListener {
            val cityName = cityNameInput.text.toString()
            val isValid = validateCityName(cityName)

            if (!isValid) {
                return@setOnClickListener
            }

            val isExist = isExistCityName(cityName)

            if (isExist) {
                return@setOnClickListener
            }

            WeatherService.addCity(cityName)
            dismiss()

            activity?.let {
                Snackbar.make(
                    it.findViewById(R.id.fragment_container),
                    cityName + " " + resources.getString(R.string.city_added),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isExistCityName(city: String): Boolean {
        return if (cities.any { it.name == city }) {
            cityNameLayout.error = city + " " + resources.getString(R.string.city_exists)
            true
        } else {
            cityNameLayout.error = null
            false
        }
    }

    private fun validateCityName(city: String): Boolean {
        return if (!Patterns.CITY_NAME.toRegex().matches(city)) {
            cityNameLayout.error = resources.getString(R.string.invalid_city_name)
            false
        } else {
            cityNameLayout.error = null
            true
        }
    }
}

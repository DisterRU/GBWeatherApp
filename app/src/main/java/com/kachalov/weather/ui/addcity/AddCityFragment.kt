package com.kachalov.weather.ui.addcity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.kachalov.weather.R
import com.kachalov.weather.constants.Patterns
import com.kachalov.weather.entities.City
import com.kachalov.weather.entities.Forecast
import com.kachalov.weather.livedata.CitiesViewModel
import kotlinx.android.synthetic.main.fragment_add_city.*
import kotlin.random.Random.Default.nextInt

class AddCityFragment : BottomSheetDialogFragment() {
    private val model = CitiesViewModel.INSTANCE

    private val cities: MutableList<City> by lazy {
        model.cities.value?.toMutableList() ?: mutableListOf()
    }

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

    private fun saveCities() {
        model.cities.value = cities
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

            val city = City(
                name = cityName,
                temp = nextInt(0, 30),
                icon = when (nextInt(0, 3)) {
                    0 -> R.drawable.sun
                    1 -> R.drawable.cloud
                    else -> R.drawable.rain
                },
                pressure = nextInt(730, 760),
                forecastList = listOf(
                    Forecast("12.00", R.drawable.sun, 1),
                    Forecast("15.00", R.drawable.cloud, 2),
                    Forecast("18.00", R.drawable.rain, 3),
                    Forecast("21.00", R.drawable.rain, 4),
                    Forecast("00.00", R.drawable.sun, 5),
                    Forecast("03.00", R.drawable.cloud, 6),
                    Forecast("06.00", R.drawable.cloud, 7),
                    Forecast("09.00", R.drawable.cloud, 8),
                    Forecast("12.00", R.drawable.sun, 9)
                )
            )
            cities.add(city)
            saveCities()
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

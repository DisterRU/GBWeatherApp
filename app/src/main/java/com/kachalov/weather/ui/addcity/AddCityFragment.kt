package com.kachalov.weather.ui.addcity


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kachalov.weather.R
import com.kachalov.weather.constants.Keys
import com.kachalov.weather.constants.Pattern
import com.kachalov.weather.constants.Preferences
import com.kachalov.weather.observers.CitiesChanger
import com.kachalov.weather.observers.CitiesObserver
import com.kachalov.weather.persistence.City
import kotlinx.android.synthetic.main.fragment_add_city.*
import kotlin.random.Random.Default.nextInt

class AddCityFragment : BottomSheetDialogFragment(), CitiesChanger {
    private lateinit var preferences: SharedPreferences
    private lateinit var cities: MutableList<City>

    private val observers: MutableList<CitiesObserver> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
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

    override fun onAttach(context: Context) {
        initCities(context)
        super.onAttach(context)
    }

    override fun onDetach() {
        saveCities()
        super.onDetach()
    }

    private fun initCities(context: Context) {
        preferences = context.getSharedPreferences(Preferences.CITIES, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = preferences.getString(Keys.CITIES, "")
        cities = if (json.isNullOrBlank()) {
            mutableListOf()
        } else {
            val type = object : TypeToken<MutableList<City>>() {}.type
            gson.fromJson(json, type)
        }
    }


    //TODO: REWORK
    private fun saveCities() {
        val gson = Gson()
        val json = gson.toJson(cities)
        preferences.edit()
            .putString(Keys.CITIES, json)
            .apply()
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
                pressure = nextInt(730, 760)
            )
            cities.add(city)
            notifyObservers()
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
        return if (!Pattern.CITY_NAME.toRegex().matches(city)) {
            cityNameLayout.error = resources.getString(R.string.invalid_city_name)
            false
        } else {
            cityNameLayout.error = null
            true
        }
    }

    override fun addObserver(observer: CitiesObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: CitiesObserver) {
        observers.remove(observer)
    }

    override fun notifyObservers() {
        observers.forEach { it.updateCities() }
    }
}

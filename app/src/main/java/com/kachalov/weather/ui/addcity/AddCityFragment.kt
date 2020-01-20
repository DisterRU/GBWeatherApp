package com.kachalov.weather.ui.addcity


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kachalov.weather.R
import com.kachalov.weather.constants.Keys
import com.kachalov.weather.constants.Pattern
import com.kachalov.weather.constants.Preferences
import com.kachalov.weather.persistence.City
import kotlinx.android.synthetic.main.fragment_add_city.*
import kotlin.random.Random.Default.nextInt

class AddCityFragment : BottomSheetDialogFragment() {
    private lateinit var preferences: SharedPreferences
    private lateinit var cities: MutableList<City>

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
        initEditText()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        initCities(context)
        super.onAttach(context)
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

    private fun initButton() {
        cityNameInput.setOnClickListener {
            val cityName = cityNameInput.text.toString()
            if (cities.none { it.name == cityName }) {
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
            } else {
                Toast.makeText(
                    activity,
                    "$cityName " + resources.getString(R.string.city_exists),
                    Toast.LENGTH_LONG
                ).show()
            }
            dismiss()
        }
    }

    private fun initEditText() {
        cityNameInput.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                val text = (view as TextView).text
                if (!Pattern.CITY_NAME.toRegex().matches(text)) {
                    cityNameLayout.error = resources.getString(R.string.invalid_city_name)
                } else {
                    cityNameLayout.error = null
                }
            }
        }
    }
}

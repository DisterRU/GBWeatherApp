package com.kachalov.weather.ui.cities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kachalov.weather.R
import com.kachalov.weather.constants.Dialogs
import com.kachalov.weather.constants.Fragments
import com.kachalov.weather.constants.Keys
import com.kachalov.weather.constants.Preferences
import com.kachalov.weather.persistence.City
import com.kachalov.weather.utils.FragmentChanger
import kotlinx.android.synthetic.main.fragment_cities.*

class CitiesFragment : Fragment() {
    private var fragmentChanger: FragmentChanger? = null
    private lateinit var cities: List<City>
    private lateinit var preferences: SharedPreferences


    private val onListItemClickListener = object : CitiesAdapter.OnListItemClickListener {
        override fun onItemClick(currentCity: City) {
            fragmentChanger?.changeFragment(
                tag = Fragments.CITY_INFO,
                args = Bundle().apply { putSerializable(Keys.CURRENT_CITY, currentCity) },
                addToBackStack = true
            )
        }
    }

    override fun onAttach(context: Context) {
        initFragmentChanger(context)
        initCities(context)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initButton()
        initRecycler()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDetach() {
        fragmentChanger = null
        super.onDetach()
    }

    private fun initButton() {
        addCityFloatingButton.setOnClickListener {
            fragmentChanger?.showDialog(Dialogs.ADD_CITY)
        }
    }

    private fun initCities(context: Context) {
        preferences = context.getSharedPreferences(Preferences.CITIES, Context.MODE_PRIVATE)
        val json = preferences.getString(Keys.CITIES, "")
        cities = if (json.isNullOrBlank()) {
            listOf()
        } else {
            val gson = Gson()
            val type = object : TypeToken<List<City>>() {}.type
            gson.fromJson(json, type)
        }
    }

    private fun initFragmentChanger(context: Context) {
        if (context is FragmentChanger) {
            fragmentChanger = context
        }
    }

    private fun initRecycler() {
        citiesRecycler.adapter = CitiesAdapter(cities, onListItemClickListener)
        citiesRecycler.layoutManager = LinearLayoutManager(context)
        citiesRecycler.addItemDecoration(CitiesDecorator(10))
    }
}
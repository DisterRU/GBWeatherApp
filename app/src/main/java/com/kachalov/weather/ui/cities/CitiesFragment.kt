package com.kachalov.weather.ui.cities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kachalov.weather.R
import com.kachalov.weather.constants.Dialogs
import com.kachalov.weather.constants.Fragments
import com.kachalov.weather.constants.Keys
import com.kachalov.weather.constants.Preferences
import com.kachalov.weather.entities.City
import com.kachalov.weather.livedata.CitiesLiveData
import com.kachalov.weather.utils.FragmentChanger
import kotlinx.android.synthetic.main.fragment_cities.*

class CitiesFragment : Fragment() {
    private lateinit var weatherPreferences: SharedPreferences

    private val cities = CitiesLiveData.CITIES

    private var fragmentChanger: FragmentChanger? = null
    private val adapter by lazy { CitiesAdapter(getCities(), onListItemClickListener) }
    private val onListItemClickListener = object : CitiesAdapter.OnListItemClickListener {
        override fun onItemClick(currentCity: City) {
            fragmentChanger?.changeFragment(
                tag = Fragments.CITY_INFO,
                args = Bundle().apply {
                    putSerializable(Keys.CURRENT_CITY, currentCity)
                    putBoolean(Keys.PRESSURE, weatherPreferences.getBoolean(Keys.PRESSURE, false))
                },
                addToBackStack = true
            )
        }
    }

    override fun onAttach(context: Context) {
        initFragmentChanger(context)
        initPreferences(context)
        initObserver()
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
        initRecycler()
        initButton()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initObserver() {
        val citiesObserver = Observer<List<City>> { cities ->
            adapter.updateCities(cities)
        }
        cities.observeForever(citiesObserver)
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

    private fun initPreferences(context: Context) {
        weatherPreferences = context.getSharedPreferences(Preferences.WEATHER, Context.MODE_PRIVATE)
    }

    private fun initFragmentChanger(context: Context) {
        if (context is FragmentChanger) {
            fragmentChanger = context
        }
    }

    private fun initRecycler() {
        citiesRecycler.adapter = adapter
        citiesRecycler.layoutManager = LinearLayoutManager(context)
        citiesRecycler.addItemDecoration(CitiesDecorator(10))
    }

    private fun getCities(): List<City> {
        return cities.value ?: listOf()
    }
}
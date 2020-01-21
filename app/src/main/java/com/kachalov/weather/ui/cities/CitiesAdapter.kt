package com.kachalov.weather.ui.cities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kachalov.weather.R
import com.kachalov.weather.persistence.City
import kotlinx.android.synthetic.main.item_city.view.*

class CitiesAdapter(
    var cities: List<City>,
    private val listener: OnListItemClickListener
) : RecyclerView.Adapter<CitiesAdapter.CityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CityHolder(inflater.inflate(R.layout.item_city, parent, false))
    }

    override fun getItemCount() = cities.size


    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.bind(cities[position])
    }

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(city: City) {
            itemView.city_name.text = city.name
            itemView.city_temp.text = city.temp.toString()
            itemView.weather_icon.setImageResource(city.icon)
            itemView.weather_icon.tag = city.icon
            bindItemView(city)
        }

        private fun bindItemView(city: City) {
            itemView.setOnClickListener {
                listener.onItemClick(city)
            }
        }

    }

    interface OnListItemClickListener {
        fun onItemClick(currentCity: City)
    }
}
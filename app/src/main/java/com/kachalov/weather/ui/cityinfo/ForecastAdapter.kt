package com.kachalov.weather.ui.cityinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kachalov.weather.R
import com.kachalov.weather.entities.Forecast
import kotlinx.android.synthetic.main.item_forecast.view.*

class ForecastAdapter(private val forecasts: List<Forecast>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ForecastHolder(inflater.inflate(R.layout.item_forecast, parent, false))
    }

    override fun getItemCount() = forecasts.size

    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {
        holder.bind(forecasts[position])
    }

    inner class ForecastHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(forecast: Forecast) {
            itemView.forecastTime.text = forecast.time
            itemView.forecastTemp.text = forecast.temp.toString()
            itemView.forecastIcon.setImageResource(forecast.icon)
        }
    }
}
package com.ikiugu.weather.ui.home


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ikiugu.weather.R
import com.ikiugu.weather.databinding.ForecastListItemBinding
import com.ikiugu.weather.domain.ScreenForecast

/**
 * Created by Alfred Ikiugu on 12/06/2021
 */

class ForecastAdapter(private val context: Context) :
    ListAdapter<ScreenForecast, ForecastAdapter.ScreenForecastViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenForecastViewHolder {
        val binding =
            ForecastListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScreenForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScreenForecastViewHolder, position: Int) {
        val currentForecast = getItem(position)
        holder.bind(currentForecast)
    }


    inner class ScreenForecastViewHolder(private val binding: ForecastListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(screenForecast: ScreenForecast) {
            binding.apply {
                dayOfTheWeek.text = screenForecast.dayOfTheWeek
                forecastIcon.setImageResource(
                    when (screenForecast.weatherId) {
                        in 200L..299L -> {
                            R.drawable.rain
                        }
                        in 300L..399L -> {
                            R.drawable.rain
                        }
                        in 500L..599L -> {
                            R.drawable.rain
                        }
                        in 600L..699L -> {
                            R.drawable.partlysunny
                        }
                        in 700L..799L -> {
                            R.drawable.partlysunny
                        }
                        800L -> {
                            R.drawable.clear
                        }
                        in 801L..804L -> {
                            R.drawable.partlysunny
                        }
                        else -> {
                            R.drawable.clear
                        }
                    }
                )
                forecastTemperature.text = String.format(
                    context.getString(R.string.degree_text),
                    screenForecast.temp.toString()
                )
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ScreenForecast>() {
        override fun areItemsTheSame(oldItem: ScreenForecast, newItem: ScreenForecast): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: ScreenForecast, newItem: ScreenForecast): Boolean {
            return oldItem == newItem
        }
    }

}
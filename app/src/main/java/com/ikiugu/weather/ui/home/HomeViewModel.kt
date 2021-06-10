package com.ikiugu.weather.ui.home

/**
 * Created by Alfred Ikiugu on 09/06/2021
 */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.ikiugu.weather.R
import com.ikiugu.weather.database.getDatabase
import com.ikiugu.weather.repository.WeatherRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val weatherRepository = WeatherRepository(database)

    val currentWeatherTemp = weatherRepository.currWeather

    val weatherIcon: LiveData<Int> = Transformations.map(currentWeatherTemp) {
        when (it.description) {
            "Clouds" -> R.drawable.sea_cloudy
            else -> R.drawable.sea_sunnypng
        }
    }

    val weatherText: LiveData<String> = Transformations.map(currentWeatherTemp) {
        when (it.description) {
            "Clouds" -> "Cloudy".uppercase()
            else -> "Sunny".uppercase()
        }
    }

    val appColors: LiveData<Int> = Transformations.map(weatherText) {
        application.applicationContext.resources.getColor(
            when (it) {
                "Cloudy".uppercase() -> R.color.cloudy
                else -> R.color.sunny
            }
        )
    }

    init {
        viewModelScope.launch {
            weatherRepository.getCurrentWeather()
        }
    }

}
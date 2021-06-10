package com.ikiugu.weather.ui.home

/**
 * Created by Alfred Ikiugu on 09/06/2021
 */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ikiugu.weather.database.getDatabase
import com.ikiugu.weather.repository.WeatherRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val weatherRepository = WeatherRepository(database)

    val currentWeatherTemp = weatherRepository.currWeather

    init {
        viewModelScope.launch {
            weatherRepository.getCurrentWeather()
        }
    }

}
package com.ikiugu.weather.ui.home

/**
 * Created by Alfred Ikiugu on 09/06/2021
 */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ikiugu.weather.database.getDatabase
import com.ikiugu.weather.repository.WeatherRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val weatherRepository = WeatherRepository(database)

    val currentWeatherTemp = weatherRepository.currWeather

    /*init {
        viewModelScope.launch {
            weatherRepository.getCurrentWeather()
        }
    }*/

}
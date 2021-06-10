package com.ikiugu.weather.ui.home

/**
 * Created by Alfred Ikiugu on 09/06/2021
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikiugu.weather.repository.WeatherRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository()

    init {
        viewModelScope.launch {
            weatherRepository.getWeatherForecast()
        }
    }

}
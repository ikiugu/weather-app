package com.ikiugu.weather.ui.home

/**
 * Created by Alfred Ikiugu on 09/06/2021
 */

import android.app.Application
import androidx.lifecycle.*
import com.ikiugu.weather.R
import com.ikiugu.weather.database.getDatabase
import com.ikiugu.weather.repository.WeatherRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class Location(val latitude: Double, val longitude: Double)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val weatherRepository = WeatherRepository(database)

    val currentWeatherTemp = weatherRepository.currWeather

    val weatherIcon: LiveData<Int> = Transformations.map(currentWeatherTemp) {
        when (it?.weatherId) {
            in 200L..299L -> {
                R.drawable.sea_rainy
            }
            in 300L..399L -> {
                R.drawable.sea_rainy
            }
            in 500L..599L -> {
                R.drawable.sea_rainy
            }
            in 600L..699L -> {
                R.drawable.sea_cloudy
            }
            in 700L..799L -> {
                R.drawable.sea_cloudy
            }
            800L -> {
                R.drawable.sea_sunnypng
            }
            in 801L..804L -> {
                R.drawable.sea_cloudy
            }
            else -> {
                R.drawable.sea_sunnypng
            }
        }
    }

    val weatherText: LiveData<String> = Transformations.map(currentWeatherTemp) {
        when (it?.weatherId) {
            in 200L..299L -> {
                "Rainy".uppercase()
            }
            in 300L..399L -> {
                "Rainy".uppercase()
            }
            in 500L..599L -> {
                "Rainy".uppercase()
            }
            in 600L..699L -> {
                "Cloudy".uppercase()
            }
            in 700L..799L -> {
                "Cloudy".uppercase()
            }
            800L -> {
                "Sunny".uppercase()
            }
            in 801L..804L -> {
                "Cloudy".uppercase()
            }
            else -> {
                "Sunny".uppercase()
            }
        }
    }

    val appColors: LiveData<Int> = Transformations.map(weatherText) {
        application.applicationContext.resources.getColor(
            when (it) {
                "Cloudy".uppercase() -> R.color.cloudy
                "Rainy".uppercase() -> R.color.rainy
                "Sunny".uppercase() -> R.color.sunny
                else -> R.color.sunny
            }
        )
    }

    var location = MutableLiveData<Location>()

    /*init {
        viewModelScope.launch {
            weatherRepository.getCurrentWeather()
        }
    }*/

    init {
        viewModelScope.launch {
            location.asFlow().collect { location ->
                weatherRepository.getCurrentWeather(location.latitude, location.longitude)
            }
        }
    }


    fun refreshWeather() {
        viewModelScope.launch {
            weatherRepository.getCurrentWeather(location.value?.latitude, location.value?.longitude)
        }
    }

}
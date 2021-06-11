package com.ikiugu.weather.ui.favorites

/**
 * Created by Alfred Ikiugu on 09/06/2021
 */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ikiugu.weather.database.getDatabase
import com.ikiugu.weather.repository.WeatherRepository

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val weatherRepository = WeatherRepository(database)


    val favoriteWeather = weatherRepository.currWeatherFavorites
}
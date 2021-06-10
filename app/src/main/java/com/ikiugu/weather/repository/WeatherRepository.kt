package com.ikiugu.weather.repository

import com.ikiugu.weather.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Alfred Ikiugu on 09/06/2021
 */

class WeatherRepository {

    suspend fun getCurrentWeather() {
        withContext(Dispatchers.IO) {
            val weather = Network.weather.getCurrentWeather().await()
        }
    }

    suspend fun getWeatherForecast() {
        withContext(Dispatchers.IO) {
            val forecast = Network.weather.getWeatherForecast().await()
        }
    }

}
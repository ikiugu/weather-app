package com.ikiugu.weather.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ikiugu.weather.database.CurrentWeather.Companion.asDomainModel
import com.ikiugu.weather.database.WeatherDatabase
import com.ikiugu.weather.domain.ScreenWeather
import com.ikiugu.weather.network.CurrentWeatherDTO.Companion.asDatabaseModel
import com.ikiugu.weather.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Alfred Ikiugu on 09/06/2021
 */

class WeatherRepository(private val weatherDatabase: WeatherDatabase) {

    val currWeather: LiveData<ScreenWeather> = Transformations
        .map(weatherDatabase.weatherDao.getCurrentWeather()) {
            it?.asDomainModel()
        }

    private var _finishedLoading = MutableLiveData<Boolean>()
    val finishedLoading: LiveData<Boolean> get() = _finishedLoading

    private var _startedLoading = MutableLiveData<Boolean>()
    val startedLoading: LiveData<Boolean> get() = _startedLoading

    suspend fun getCurrentWeather(lat: Double?, lon: Double?) {
        _startedLoading.value = true

        withContext(Dispatchers.IO) {
            val weather =
                Network.weather.getCurrentWeather(lat, lon).await()
            weatherDatabase.weatherDao.insertWeather(weather.asDatabaseModel())
        }

        _finishedLoading.value = true
    }

    suspend fun getWeatherForecast() {
        withContext(Dispatchers.IO) {
            val forecast = Network.weather.getWeatherForecast().await()
            _finishedLoading.value = true
        }
    }

    suspend fun updateWeather() {
        withContext(Dispatchers.IO) {
            val weather = currWeather.value?.let {
                weatherDatabase.weatherDao.getCurrentWeatherWithId(
                    it.id
                )
            }

            weather?.let { weatherDatabase.weatherDao.updateCurrentWeather(it.copy(favorite = !weather.favorite)) }
        }
    }

}
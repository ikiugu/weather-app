package com.ikiugu.weather.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ikiugu.weather.database.CurrentWeather.Companion.asDomainModel
import com.ikiugu.weather.database.WeatherDatabase
import com.ikiugu.weather.database.asDomainModel
import com.ikiugu.weather.database.asOtherDomainModel
import com.ikiugu.weather.domain.ScreenForecast
import com.ikiugu.weather.domain.ScreenWeather
import com.ikiugu.weather.network.CurrentWeatherDTO.Companion.asDatabaseModel
import com.ikiugu.weather.network.ForecastDTO.Companion.asDatabaseModel
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

    val weatherForecast: LiveData<List<ScreenForecast>> =
        Transformations.map(weatherDatabase.weatherDao.getAllForecasts()) {
            it.asDomainModel()
        }

    val currWeatherFavorites: LiveData<List<ScreenWeather>> = Transformations
        .map(weatherDatabase.weatherDao.getAllFavorites()) {
            it.asOtherDomainModel()
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
            weatherDatabase.weatherDao.upsert(weather.asDatabaseModel())
        }

        _finishedLoading.value = true
    }

    suspend fun getWeatherForecast(lat: Double?, lon: Double?) {
        withContext(Dispatchers.IO) {
            val forecast = Network.weather.getWeatherForecast(lat, lon).await()
            weatherDatabase.weatherDao.insertAll(*forecast.asDatabaseModel())
        }
    }

    suspend fun updateWeather() {
        withContext(Dispatchers.IO) {
            val weather = currWeather.value?.let {
                weatherDatabase.weatherDao.getCurrentWeatherWithId(
                    it.id
                )
            }


            if (weather != null) {
                if (weather.favorite == true) {
                    weatherDatabase.weatherDao.updateCurrentWeather(weather.copy(favorite = false))
                } else {
                    weatherDatabase.weatherDao.updateCurrentWeather(weather.copy(favorite = true))
                }

            }
        }
    }

    suspend fun getFavorites() {
        withContext(Dispatchers.IO) {

        }
    }

}


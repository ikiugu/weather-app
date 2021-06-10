package com.ikiugu.weather.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/**
 * Created by Alfred Ikiugu on 09/06/2021
 */

interface WeatherService {

    @GET("weather?q=London&units=metric&appid=f9f3de845b9635080901d5575af1bb27")
    fun getCurrentWeather() : Deferred<CurrentWeatherDTO>

    @GET("forecast/daily?q=London&appid=f9f3de845b9635080901d5575af1bb27&units=metric&cnt=6")
    fun getWeatherForecast() : Deferred<ForecastDTO>


}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org/data/2.5/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val weather: WeatherService = retrofit.create(WeatherService::class.java)
}
package com.ikiugu.weather.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Alfred Ikiugu on 09/06/2021
 */

interface WeatherService {

    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?,
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = "f9f3de845b9635080901d5575af1bb27"
    ): Deferred<CurrentWeatherDTO>

    @GET("data/2.5/forecast/daily")
    fun getWeatherForecast(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?,
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = "f9f3de845b9635080901d5575af1bb27",
        @Query("cnt") count: Int = 5
    ): Deferred<ForecastDTO>


}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val weather: WeatherService = retrofit.create(WeatherService::class.java)
}
package com.ikiugu.weather.network

import com.squareup.moshi.JsonClass

/**
 * Created by Alfred Ikiugu on 10/06/2021
 */


@JsonClass(generateAdapter = true)
data class CurrentWeatherDTO(
    val weather: List<Weather>,
    val main: Main,
    val timezone: Long,
    val name: String,
    val id: Long
)

@JsonClass(generateAdapter = true)
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Long,
    val humidity: Long
)

@JsonClass(generateAdapter = true)
data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)

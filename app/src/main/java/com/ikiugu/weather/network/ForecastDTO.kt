package com.ikiugu.weather.network

import com.squareup.moshi.JsonClass

/**
 * Created by Alfred Ikiugu on 10/06/2021
 */

@JsonClass(generateAdapter = true)
data class ForecastDTO (
    val city: City,
    val cod: String,
    val message: Double,
    val cnt: Long,
    val list: List<ListElement>
)

@JsonClass(generateAdapter = true)
data class City (
    val id: Long,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Long,
    val timezone: Long
)

@JsonClass(generateAdapter = true)
data class Coord (
    val lon: Double,
    val lat: Double
)

@JsonClass(generateAdapter = true)
data class ListElement (
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Temp,
    val feels_like: FeelsLike,
    val pressure: Long,
    val humidity: Long,
    val weather: List<WeatherItem>,
    val speed: Double,
    val deg: Long,
    val gust: Double,
    val clouds: Long,
    val pop: Double,
    val rain: Double? = null
)

@JsonClass(generateAdapter = true)
data class FeelsLike (
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

@JsonClass(generateAdapter = true)
data class Temp (
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

@JsonClass(generateAdapter = true)
data class WeatherItem (
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)

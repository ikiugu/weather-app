package com.ikiugu.weather.domain

import com.ikiugu.weather.utils.toDate

/**
 * Created by Alfred Ikiugu on 10/06/2021
 */

data class ScreenWeather(
    val id: Long,
    val name: String,
    val minTemp: Double,
    val temp: Double,
    val maxTemp: Double,
    val weatherId: Long,
    val favorite: Boolean?
)

data class ScreenForecast(
    val dt: Long,
    val temp: Double,
    val weatherId: Long
) {
    val dayOfTheWeek: String
        get() = dt.toDate()
}
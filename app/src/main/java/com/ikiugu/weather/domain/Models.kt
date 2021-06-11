package com.ikiugu.weather.domain

/**
 * Created by Alfred Ikiugu on 10/06/2021
 */

data class ScreenWeather(
    val minTemp : Double,
    val temp : Double,
    val maxTemp : Double,
    val weatherId: Long
)
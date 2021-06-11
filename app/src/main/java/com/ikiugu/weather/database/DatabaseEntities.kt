package com.ikiugu.weather.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ikiugu.weather.domain.ScreenWeather

/**
 * Created by Alfred Ikiugu on 10/06/2021
 */

@Entity(tableName = "currentWeather")
data class CurrentWeather constructor(
    @PrimaryKey
    val id: Long,
    val name: String,
    val minimumTemp: Double,
    val currentTemp: Double,
    val maximumTemp: Double,
    val weatherId: Long,
    val latitude: Double,
    val longitude: Double,
    val created: Long = System.currentTimeMillis(),
) {
    companion object {
        fun CurrentWeather.asDomainModel(): ScreenWeather {
            return ScreenWeather(
                minTemp = this.minimumTemp,
                temp = this.currentTemp,
                maxTemp = this.maximumTemp,
                weatherId = this.weatherId
            )
        }
    }
}


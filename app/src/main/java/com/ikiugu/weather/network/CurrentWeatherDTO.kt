package com.ikiugu.weather.network

import com.squareup.moshi.JsonClass

/**
 * Created by Alfred Ikiugu on 10/06/2021
 */

/*@JsonClass(generateAdapter = true)
data class CurrentWeather(val current: CurrentWeatherDTO)*/

@JsonClass(generateAdapter = true)
data class CurrentWeatherDTO(
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val timezone: Long,
    val name: String,
    val id: Long
) {
    companion object {
        fun CurrentWeatherDTO.asDatabaseModel(): com.ikiugu.weather.database.CurrentWeather {
            return com.ikiugu.weather.database.CurrentWeather(
                id = this.id,
                name = this.name,
                minimumTemp = this.main.temp_min,
                currentTemp = this.main.temp,
                maximumTemp = this.main.temp_max,
                weatherId = this.weather[0].id,
                latitude = this.coord.lat,
                longitude = this.coord.lon
            )
        }
    }
}

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

data class Coord(
    val lon: Double,
    val lat: Double
)
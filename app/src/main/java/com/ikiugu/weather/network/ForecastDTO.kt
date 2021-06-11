package com.ikiugu.weather.network

import com.ikiugu.weather.database.Forecast
import com.squareup.moshi.JsonClass

/**
 * Created by Alfred Ikiugu on 10/06/2021
 */

@JsonClass(generateAdapter = true)
data class ForecastDTO(
    val city: City,
    val list: List<ListElement>
) {
    companion object {
        fun ForecastDTO.asDatabaseModel(): Array<Forecast> {
            return list.map {
                Forecast(
                    dt = it.dt,
                    temp = it.temp.day,
                    weatherId = it.weather[0].id,
                    cityName = city.name
                )
            }.toTypedArray()
        }
    }
}

@JsonClass(generateAdapter = true)
data class City(
    val name: String,
)


@JsonClass(generateAdapter = true)
data class ListElement(
    val dt: Long,
    val temp: Temp,
    val weather: List<WeatherItem>
)


@JsonClass(generateAdapter = true)
data class Temp(
    val day: Double
)

@JsonClass(generateAdapter = true)
data class WeatherItem(
    val id: Long
)



package com.ikiugu.weather.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


/**
 * Created by Alfred Ikiugu on 10/06/2021
 */

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(currentWeather: CurrentWeather): Long

    @Query("select * from currentWeather order by created desc Limit 1")
    fun getCurrentWeather(): LiveData<CurrentWeather>

    @Update
    fun updateCurrentWeather(weather: CurrentWeather)

    @Query("select * from currentWeather where id == :id")
    fun getCurrentWeatherWithId(id: Long): CurrentWeather

    @Transaction
    fun upsert(entity: CurrentWeather) {
        val weather = getCurrentWeatherWithId(entity.id)
        if (weather != null) {
            entity.favorite = weather.favorite
            updateCurrentWeather(entity)
        } else {
            insertWeather(entity)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg weatherItems: Forecast)
}


@Database(entities = [CurrentWeather::class, Forecast::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val weatherDao: WeatherDao
}

private lateinit var INSTANCE: WeatherDatabase

fun getDatabase(context: Context): WeatherDatabase {
    synchronized(WeatherDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                "weather"
            ).build()
        }
    }
    return INSTANCE
}
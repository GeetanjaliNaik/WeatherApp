package com.geeta.weatherapp.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.geeta.weatherapp.data.weather.WeatherModel

@Dao
interface WeatherDataDao {

    @Query("SELECT * from weatherData")
    fun getAll(): LiveData<WeatherModel>

    @Insert(onConflict = REPLACE)
    fun insert(weatherData: WeatherModel)

    @Query("DELETE from weatherData")
    fun deleteAll()
}
package com.geeta.weatherapp.database


import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.geeta.weatherapp.data.weather.WeatherModel
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface WeatherDataDao {

    @Query("SELECT * from weatherData")
    fun getWeather(): Observable<WeatherModel>

    @Query("SELECT dt from weatherData")
    fun getDate():Observable<List<Int>>

    @Query("SELECT dt from weatherData")
    fun getWeatherDataDT():Int

    @Insert(onConflict = REPLACE)
    fun insertWeather(weatherData: WeatherModel)

    @Query("DELETE from weatherData")
    fun deleteAll()
}
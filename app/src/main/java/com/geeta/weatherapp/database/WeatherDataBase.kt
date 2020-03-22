package com.geeta.weatherapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.geeta.weatherapp.data.weather.LocationModel
import com.geeta.weatherapp.data.weather.WeatherModel
import com.geeta.weatherapp.database.DataConverter
import com.geeta.weatherapp.database.WeatherDataDao
import com.geeta.weatherapp.database.repositry.LocationDataDao
import javax.inject.Inject

@Database(entities = arrayOf(WeatherModel::class,LocationModel::class), version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class WeatherDataBase :RoomDatabase() {
    abstract fun weatherDataDao(): WeatherDataDao
    abstract fun locationDataDao():LocationDataDao
}
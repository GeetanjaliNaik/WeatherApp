package com.geeta.weatherapp.database.repositry

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.geeta.weatherapp.data.weather.WeatherModel
import com.geeta.weatherapp.database.WeatherDataBase
import com.geeta.weatherapp.database.WeatherDataDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherDbRepository @Inject constructor(val weatherDataDao: WeatherDataDao) :
    WeatherDbRepositoryLis {

    companion object {
        @Volatile
        private var weatherDataBase: WeatherDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = weatherDataBase ?: synchronized(LOCK) {
            weatherDataBase ?: buildDatabase(context).also { weatherDataBase = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            WeatherDataBase::class.java, "weather_db"
        ).build()
    }

    override fun getWeatherDetail(): LiveData<WeatherModel> {
        return weatherDataDao.getAll()
    }

    override fun deletWeatherDetail() {
        weatherDataDao.deleteAll()
    }

    override fun insertWeatherDatail(weatherModel: WeatherModel) {
        weatherDataDao.insert(weatherModel)
    }
}
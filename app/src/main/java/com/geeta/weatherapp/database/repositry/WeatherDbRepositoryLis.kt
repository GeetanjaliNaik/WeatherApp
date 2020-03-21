package com.geeta.weatherapp.database.repositry

import androidx.lifecycle.LiveData
import com.geeta.weatherapp.data.weather.WeatherModel
import com.geeta.weatherapp.database.WeatherDataDao

interface WeatherDbRepositoryLis{
    fun getWeatherDetail(): LiveData<WeatherModel>
    fun deletWeatherDetail()
    fun insertWeatherDatail(weatherModel: WeatherModel)

}
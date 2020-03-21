package com.geeta.weatherapp.database.repositry

import androidx.lifecycle.LiveData
import com.geeta.weatherapp.data.weather.WeatherModel
import com.geeta.weatherapp.database.WeatherDataDao
import io.reactivex.Observable

interface WeatherDbRepositoryLis{
    fun getWeatherDetail(): Observable<WeatherModel>
    fun deletWeatherDetail()
    fun insertWeatherDatail(weatherModel: WeatherModel)
    fun getDateAndTime():Observable<Int>

}
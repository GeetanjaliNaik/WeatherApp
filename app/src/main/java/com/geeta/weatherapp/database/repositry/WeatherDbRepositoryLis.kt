package com.geeta.weatherapp.database.repositry

import androidx.lifecycle.LiveData
import com.geeta.weatherapp.data.weather.LocationModel
import com.geeta.weatherapp.data.weather.WeatherModel
import com.geeta.weatherapp.database.WeatherDataDao
import io.reactivex.Observable

interface WeatherDbRepositoryLis{
    fun getWeatherDetail(): Observable<WeatherModel>
    fun deletWeatherDetail()
    fun insertWeatherDatail(weatherModel: WeatherModel)
    fun getDateAndTime():Observable<List<Int>>

    fun getLocation(): Observable<List<LocationModel>>
    fun deletLocationDetail()
    fun insertLocation(locationModel: LocationModel)

}
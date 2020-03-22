package com.geeta.weatherapp.worker

import com.geeta.weatherapp.BuildConfig
import com.geeta.weatherapp.api.services.WeatherService
import com.geeta.weatherapp.data.weather.WeatherModel
import io.reactivex.Observable

class WorkerRepository(val weatherService: WeatherService) {
    fun updateWeather(lat:Double,lon:Double): Observable<WeatherModel>
    {
        return weatherService.getCurrentWeather(lat,lon,BuildConfig.APP_ID)
    }
}

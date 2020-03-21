package com.geeta.weatherapp.api.services

import com.geeta.weatherapp.api.APIRestConstant
import com.geeta.weatherapp.data.weather.WeatherModel
import io.reactivex.Observable
import retrofit2.http.*

interface WeatherService {
//    @GET(APIRestConstant.GET_WEATHER)
//    abstract fun getCurrentWeatherAddress(@Query("q") lat: String, @Query("appid") appKey: String): Observable<WeatherModel>
    @GET(APIRestConstant.GET_WEATHER)
    abstract fun getCurrentWeather(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") appKey: String): Observable<WeatherModel>
}
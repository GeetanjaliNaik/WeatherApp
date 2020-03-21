package com.geeta.weatherapp.api.datamanager

import android.app.Application
import com.geeta.weatherapp.BuildConfig
import com.geeta.weatherapp.api.BaseApiManager
import com.geeta.weatherapp.data.weather.WeatherModel
import com.geeta.weatherapp.database.WeatherDataBase
import com.geeta.weatherapp.database.repositry.WeatherDbRepository
import com.geeta.weatherapp.utils.AppData
import io.reactivex.Observable
import javax.inject.Inject

class WeatherDataManager @Inject constructor(var mBaseApiManager: BaseApiManager/*,var weatherDbRepository: WeatherDbRepository */) {

@Inject
lateinit var appData: AppData
    @Inject
    lateinit var application: Application
    fun updateWeather(lat :Double,lon:Double): Observable<WeatherModel>
    {

        return mBaseApiManager.weatherService.getCurrentWeather(lat,lon,BuildConfig.APP_ID)
    }


}
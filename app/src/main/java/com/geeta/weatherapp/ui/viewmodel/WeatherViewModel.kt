package com.geeta.weatherapp.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geeta.weatherapp.R
import com.geeta.weatherapp.WeatherApplication
import com.geeta.weatherapp.api.datamanager.WeatherDataManager
import com.geeta.weatherapp.data.weather.LocationModel
import com.geeta.weatherapp.data.weather.WeatherModel
import com.geeta.weatherapp.database.repositry.WeatherDbRepository
import com.geeta.weatherapp.utils.AppData
import com.geeta.weatherapp.utils.CommonResponseParser
import com.geeta.weatherapp.utils.unixTimestampToTimeString
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WeatherViewModel @Inject constructor(val application: Application) : ViewModel() {
    @Inject
    lateinit var weatherDataManager: WeatherDataManager
    @Inject
    lateinit var appData: AppData

    val progressBarLiveData = MutableLiveData<Boolean>()
    var weatherData= MutableLiveData<WeatherModel>()
    val weatherInfoFailureLiveData = MutableLiveData<String>()
    val locationDetail=MutableLiveData<LocationModel>()

    fun getWeather()
    {
        getWeatherData()
    }
    @SuppressLint("CheckResult")
    private fun getWeatherData(){
        progressBarLiveData.postValue(true)
        try{
            WeatherDbRepository.invoke(application).weatherDataDao().getDate()
                 .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {result->

                        if(result==null|| result.isEmpty()){
                            getCurrentweather()
                            progressBarLiveData.postValue(false)
                        }

                        else{
                            progressBarLiveData.postValue(false)
                        val times=result[0].toLong()*1000
                        if(((System.currentTimeMillis()-times)/3600000)>2)
                            getCurrentweather()
                        else
                            getWeatherFromDB()
                        }

                    }
                    , {error->
                        weatherInfoFailureLiveData.postValue(  application.getString(R.string.unable_to_get_weather_update))
                        progressBarLiveData.postValue(false)
                    })
        }
        catch (e:Exception)
                {
                    weatherInfoFailureLiveData.postValue(  application.getString(R.string.unable_to_get_weather_update))
                    progressBarLiveData.postValue(false)
                }

    }
    @SuppressLint("CheckResult")
    private fun getWeatherFromDB()
    {
        progressBarLiveData.postValue(true)
        WeatherDbRepository.invoke(application).weatherDataDao().getWeather()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result->
                    weatherData.postValue(result)
                    progressBarLiveData.postValue(false)
                }
                , {error->
                    weatherInfoFailureLiveData.postValue(  CommonResponseParser.ErrorParser.parseError(error, true))

                    progressBarLiveData.postValue(false)
                })


    }
    @SuppressLint("CheckResult")
    fun getCurrentweather()
    {
        progressBarLiveData.postValue(true)
        WeatherDbRepository.invoke(application).locationDataDao().getLocation()
            .subscribeOn(Schedulers.newThread())
            .flatMap { result->
                if(result==null|| result.isEmpty())
                    throw Throwable(application.getString(R.string.unable_fatch_data))
                else
                    return@flatMap weatherDataManager.updateWeather(result[0].latitude,result[0].longitude)
            }
            .map{result->
                var weatherDataDao=WeatherDbRepository.invoke(application).weatherDataDao()
                weatherDataDao.deleteAll()
                weatherDataDao.insertWeather(result)
                return@map result
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result->
                    //                    WeatherDbRepository.invoke(application).weatherDataDao().insert(result)
                    weatherData.postValue(result)
                    progressBarLiveData.postValue(false)
                }
                , {error->
                    weatherInfoFailureLiveData.postValue(  CommonResponseParser.ErrorParser.parseError(error, true))

                    progressBarLiveData.postValue(false)
                })
    }

}
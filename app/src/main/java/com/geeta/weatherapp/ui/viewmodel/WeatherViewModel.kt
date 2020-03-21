package com.geeta.weatherapp.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.location.Location
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
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
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
    var timerInterval:Number=0

   private var fusedLocationClient :FusedLocationProviderClient?=null
    var locationModel:LocationModel?=null
    private fun getCurrentlocathion()
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application/*WeatherApplication.appContext!!*/)
        fusedLocationClient?.lastLocation
            ?.addOnSuccessListener { location: Location? ->
                location?.also {
                    //                    location.altitude

                    locationModel=LocationModel(
                        longitude = location.longitude,
                        latitude = location.latitude
                    )
    //                    locationDetail.postValue(locationModel)
                    appData.locationModel=locationModel
                    locationDetail.value=locationModel
                }
            }?.addOnFailureListener {
                if(appData.locationModel!=null)
                    getCurrentweather()
                else {
                    weatherInfoFailureLiveData.postValue(application.resources.getString(R.string.unable_to_get_current_location))
                    progressBarLiveData.postValue(false)
                }

            }
    }
    fun getWeather()
    {
        progressBarLiveData.postValue(true)
        getWeatherData()
       /* getCurrentlocathion()
        getWeatherFromDB()*/
    }
    @SuppressLint("CheckResult")
    private fun getWeatherData(){
        WeatherDbRepository.invoke(application).weatherDataDao().getDate()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result->
                    val times=result.toLong()*1000
                    if(((System.currentTimeMillis()-times)/3600000)>2)
                        getCurrentlocathion()
                    else
                        getWeatherFromDB()
                }
                , {error->
                    weatherInfoFailureLiveData.postValue(  application.getString(R.string.unable_to_get_weather_update))
                    progressBarLiveData.postValue(false)
                })
    }
    @SuppressLint("CheckResult")
    private fun getWeatherFromDB()
    {
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
    fun getCurrentweather()
    {
        appData.locationModel?.let { weatherDataManager.updateWeather(it.latitude,it.longitude)
            .subscribeOn(Schedulers.newThread())
            .map { result->
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
                })  }
    }

}
package com.geeta.weatherapp.worker

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.geeta.weatherapp.api.BaseApiManager
import com.geeta.weatherapp.api.datamanager.WeatherDataManager
import com.geeta.weatherapp.api.services.WeatherService
import com.geeta.weatherapp.data.weather.LocationModel
import com.geeta.weatherapp.database.repositry.WeatherDbRepository
import com.geeta.weatherapp.utils.AppUtils
import com.google.android.gms.location.LocationServices
import javax.inject.Inject

class WeatherWork @Inject constructor( context: Context, workerParameters: WorkerParameters) : Worker(context,workerParameters) {


   var weatherDataManager: WeatherDataManager? =null

    override fun doWork(): Result {
            Log.i("WEATHERAPP","Inside work")
        if(AppUtils.isWifiNetworkAvailable(applicationContext))
            getCurrentlocathion()
            return Result.success()

    }
    fun getCurrentlocathion()
    {
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.applicationContext/*WeatherApplication.appContext!!*/)
        fusedLocationClient?.lastLocation
            ?.addOnSuccessListener { location: Location? ->
                location?.also {
                    //                    location.altitude

                    var locationModel=LocationModel(
                        longitude = location.longitude,
                        latitude = location.latitude
                    )
                    Log.i("WEATHERAPP","Got Location")
                    getCurrentweather(location)
                }
            }
    }
    @SuppressLint("CheckResult")
    fun getCurrentweather(location: Location)
    {
        Log.i("WEATHERAPP","Start API")
        var serviceweather: WeatherService? = WorkmanagerNetwork.getClient()?.create(WeatherService::class.java)

        Log.i("WEATHERAPP","Start API 111")
        serviceweather?.let {
            WorkerRepository(it).updateWeather(location.latitude,location.longitude).map { result->
                Log.i("WEATHERAPP","Map Result")
                var weatherDataDao=WeatherDbRepository.invoke(applicationContext).weatherDataDao()
                weatherDataDao.deleteAll()
                weatherDataDao.insertWeather(result)
                return@map result
            }
                ?.subscribe(
                    {

                    }
                    , {error->

                    })
        }

    }


}
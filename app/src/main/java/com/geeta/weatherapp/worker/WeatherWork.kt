package com.geeta.weatherapp.worker

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.geeta.weatherapp.R
import com.geeta.weatherapp.api.BaseApiManager
import com.geeta.weatherapp.api.datamanager.WeatherDataManager
import com.geeta.weatherapp.api.services.WeatherService
import com.geeta.weatherapp.data.weather.LocationModel
import com.geeta.weatherapp.database.repositry.WeatherDbRepository
import com.geeta.weatherapp.utils.AppUtils
import com.geeta.weatherapp.utils.CommonResponseParser
import com.google.android.gms.location.LocationServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherWork @Inject constructor( context: Context, workerParameters: WorkerParameters) : Worker(context,workerParameters) {


   var weatherDataManager: WeatherDataManager? =null

    override fun doWork(): Result {
            Log.i("WEATHERAPP","Inside work")
        if(AppUtils.isWifiNetworkAvailable(applicationContext))
            getCurrentweather()
            return Result.success()

    }
    @SuppressLint("CheckResult")
    fun getCurrentweather()
    {
        Log.i("WEATHERAPP","Start API")
        var serviceweather: WeatherService? = WorkmanagerNetwork.getClient()?.create(WeatherService::class.java)

        Log.i("WEATHERAPP","Start API 111")
        WeatherDbRepository.invoke(applicationContext).locationDataDao().getLocation()
            .subscribeOn(Schedulers.newThread())
            .flatMap { result->
                if(result==null|| result.isEmpty())
                    throw Throwable(applicationContext.getString(R.string.unable_fatch_data))
                else
                    return@flatMap serviceweather?.let {
                        WorkerRepository(it).updateWeather(result[0].latitude,result[0].longitude)}
            }
            .map{result->
                var weatherDataDao=WeatherDbRepository.invoke(applicationContext).weatherDataDao()
                weatherDataDao.deleteAll()
                weatherDataDao.insertWeather(result)
                return@map result
            }
            .subscribe()

    }


}
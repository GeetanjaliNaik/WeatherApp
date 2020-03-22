package com.geeta.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.cafecraft.aps.ui.base.BaseActivity
import com.geeta.weatherapp.R
import com.geeta.weatherapp.data.weather.LocationModel
import com.geeta.weatherapp.data.weather.WeatherModel
import com.geeta.weatherapp.databinding.ActivityWeatherBinding
import com.geeta.weatherapp.ui.viewmodel.LocationViewModel
import com.geeta.weatherapp.ui.viewmodel.WeatherViewModel
import com.geeta.weatherapp.utils.*
import com.geeta.weatherapp.worker.WeatherWork
import kotlinx.android.synthetic.main.activity_weather.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class WeatherActivity : BaseActivity() {
    @Inject
    lateinit var weatherViewModel: WeatherViewModel
    @Inject
    lateinit var locationViewModel: LocationViewModel

    var PERMISSION_ID = 44
//    var mFusedLocationClient: FusedLocationProviderClient? = null
    lateinit var locationData:LocationModel
    @Inject
    lateinit var weatherViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appData: AppData
    private var mPeriodicWorkRequest: PeriodicWorkRequest? = null
    lateinit var activityWeatherBinding: ActivityWeatherBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_weather)
        activityWeatherBinding = DataBindingUtil.setContentView(this, R.layout.activity_weather);

        ViewModelProvider(this,weatherViewModelFactory).get(LocationViewModel::class.java)
        ViewModelProvider(this,weatherViewModelFactory).get(WeatherViewModel::class.java)
        setLiveDataListeners()
//        startLocationUpdate()

    }

    override fun onStart() {
        super.onStart()

//        if(appData.locationModel==null)
             startLocationUpdate()
        if(appData.locationModel!=null)
            weatherViewModel.getWeather()
    }

    private fun startLocationUpdate() {
//        progressBar.visibility = View.VISIBLE
        locationViewModel.getLocationData().observe(this, Observer {
            if(appData.locationModel==null)
                weatherViewModel.getWeather()
            appData.locationModel=it
//            progressBar.visibility = View.GONE

        })
    }
    private fun setLiveDataListeners() {
        /**
         * If ViewModel failed to fetch weather , this LiveData will be triggered.
         * I know it's not good to make separate LiveData both for Success and Failure, but for sake
         * of simplification I did it. We can handle all of our errors from our Activity or Fragment
         * Base classes. Another way is: using a Generic wrapper class where you can set the success
         * or failure status for any types of data model.
         *
         * Here I've used lambda expression to implement Observer interface in second parameter.
         */
       /* weatherViewModel.getLocationData()?.observe(this, Observer { locationData->
            weatherViewModel.getWeather()

        })*/
        weatherViewModel.weatherInfoFailureLiveData.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        })

        /**
         * ProgressBar visibility will be handled by this LiveData. ViewModel decides when Activity
         * should show ProgressBar and when hide.
         *
         * Here I've used lambda expression to implement Observer interface in second parameter.
         */
        weatherViewModel.progressBarLiveData.observe(this, Observer { isShowLoader ->
            if (isShowLoader)
                progressBar.visibility = View.VISIBLE
            else
                progressBar.visibility = View.GONE
        })

        weatherViewModel.locationDetail.observe(this, Observer {
            weatherViewModel.getCurrentweather()
        })
        /**
         * This method will be triggered when ViewModel successfully receive WeatherData from our
         * data source (I mean Model). Activity just observing (subscribing) this LiveData for showing
         * weather information on UI. ViewModel receives Weather data API response from Model via
         * Callback method of Model. Then ViewModel apply some business logic and manipulate data.
         * Finally ViewModel PUSH WeatherData to `weatherInfoLiveData`. After PUSHING into it, below
         * method triggered instantly! Then we set the data on UI.
         *
         * Here I've used lambda expression to implement Observer interface in second parameter.
         */
        weatherViewModel.weatherData.observe(this, Observer { weatherData ->
            setWeatherInfo(weatherData)
        })

        /**
         * If ViewModel faces any error during Weather Info fetching API call by Model, then PUSH the
         * error message into `weatherInfoFailureLiveData`. After that, this method will be triggered.
         * Then we will hide the output view and show error message on UI.
         *
         * Here I've used lambda expression to implement Observer interface in second parameter.
         */
        weatherViewModel.weatherInfoFailureLiveData.observe(this, Observer { errorMessage ->
           showErrorinScreen(errorMessage)
            showRetryAlertForWeather(errorMessage)

        })
    }
    private fun showErrorinScreen(massage:String)
    {
        output_group.visibility = View.GONE
        tv_error_message.visibility = View.VISIBLE
        tv_error_message.text = massage
    }
   /* private fun forecastWeather() {

                weatherViewModel.getWeather()

    }*/


    private fun setWeatherInfo(weatherData: WeatherModel) {
        activityWeatherBinding.weatherdata=weatherData
        output_group.visibility = View.VISIBLE
        tv_error_message.visibility = View.GONE

    }
    fun startPerodicWork()
    {
//        var outData=Data.Builder().put(KEY_WEATHER,WeatherModel())
        mPeriodicWorkRequest = PeriodicWorkRequest.Builder(
            WeatherWork::class.java,
            2, TimeUnit.HOURS
        )
            .addTag("periodicWorkRequest")
            .build()

        WorkManager.getInstance(this).enqueue(mPeriodicWorkRequest!!)
    }
    fun cencelProidicWork()
    {
        WorkManager.getInstance(this).cancelAllWork()
    }
    fun showRetryAlertForWeather(message: String) {
        val builder = AlertDialog.Builder(this, R.style.MaterialAlertDialogStyle)
        builder.setMessage(message).setTitle(R.string.app_name).setCancelable(true)
        builder.setNeutralButton("Ok") { dialog, which ->
//            forecastWeather()
            dialog.dismiss() }
        builder.setNegativeButton(getString(R.string.cencel)){
            dialog, which ->  dialog.dismiss()
        }
        builder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        appData.locationModel=null
    }

}

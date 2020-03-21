package com.geeta.weatherapp.ui

import android.Manifest
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
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.cafecraft.aps.ui.base.BaseActivity
import com.geeta.weatherapp.R
import com.geeta.weatherapp.data.weather.LocationModel
import com.geeta.weatherapp.data.weather.WeatherModel
import com.geeta.weatherapp.databinding.ActivityWeatherBinding
import com.geeta.weatherapp.ui.viewmodel.WeatherViewModel
import com.geeta.weatherapp.utils.kelvinToCelsius
import com.geeta.weatherapp.utils.unixTimestampToDateTimeString
import com.geeta.weatherapp.utils.unixTimestampToTimeString
import com.geeta.weatherapp.worker.WeatherWork
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.layout_sunrise_sunset.*
import kotlinx.android.synthetic.main.layout_weather_additional_view.*
import kotlinx.android.synthetic.main.layout_weather_basic_view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class WeatherActivity : BaseActivity() {
    @Inject
    lateinit var weatherViewModel: WeatherViewModel
    var PERMISSION_ID = 44
//    var mFusedLocationClient: FusedLocationProviderClient? = null
    lateinit var locationData:LocationModel
    @Inject
    lateinit var weatherViewModelFactory: ViewModelProvider.Factory
    private var mPeriodicWorkRequest: PeriodicWorkRequest? = null
    lateinit var activityWeatherBinding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_weather)
        activityWeatherBinding = DataBindingUtil.setContentView(this, R.layout.activity_weather);
        ViewModelProvider(this,weatherViewModelFactory).get(WeatherViewModel::class.java)
        setLiveDataListeners()
        getLastLocation()
    }

    override fun onResume() {
        super.onResume()

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
            output_group.visibility = View.GONE
            tv_error_message.visibility = View.VISIBLE
            tv_error_message.text = errorMessage
        })
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                weatherViewModel.getWeather()
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        return !((ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
                != PackageManager.PERMISSION_GRANTED) &&( ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED ))

    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.PERMISSION_ID && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
        }
    }
    private fun setWeatherInfo(weatherData: WeatherModel) {
        activityWeatherBinding.weatherdata=weatherData
        output_group.visibility = View.VISIBLE
        tv_error_message.visibility = View.GONE
    /*    mPeriodicWorkRequest = PeriodicWorkRequest.Builder(
            WeatherWork::class.java,
            2, TimeUnit.HOURS
        )
            .addTag("periodicWorkRequest")
            .build()

        WorkManager.getInstance(this).enqueue(mPeriodicWorkRequest!!)*/
    }
}

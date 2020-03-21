package com.geeta.weatherapp.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.geeta.weatherapp.data.weather.LocationModel

const val KEY_WEATHER = "KEY_WEATHER"
class AppData{
        var aapkey: String = ""
        var locationModel:LocationModel?=null

}
@BindingAdapter("android:timestamp")
public fun setTimeStamp(view: TextView, time:Int){
        view.text=time.unixTimestampToTimeString()
}
@BindingAdapter("android:dateTime")
public fun setDate(view: TextView, time:Int){
        view.text=time.unixTimestampToDateTimeString()
}
@BindingAdapter("android:visibilitytext")
public fun setVisibilityText(view: TextView, visibility:Int){
        view.text="${visibility?.div(1000.0)} KM"
}
@BindingAdapter("android:temperature")
public fun settemperature(view: TextView, temperature:Double){
        view.text=temperature.kelvinToCelsius().toString()
}


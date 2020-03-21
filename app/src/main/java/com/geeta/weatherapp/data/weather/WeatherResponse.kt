package com.geeta.weatherapp.data.weather


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.geeta.weatherapp.database.DataConverter
import com.google.gson.annotations.SerializedName

data class Coord(@SerializedName("lon")
                 var lon: Double? = 0.0,
                 @SerializedName("lat")
                 var lat: Double? = 0.0)


data class Wind(@SerializedName("deg")
                var deg: Int? = 0,
                @SerializedName("speed")
                var speed: Double? = 0.0,
                @SerializedName("gust")
                var gust: Double? = 0.0)


data class Clouds(@SerializedName("all")
                  var all: Int? = 0)

@Entity
data class WeatherItem(@SerializedName("icon")
                       var icon: String? = "",
                       @SerializedName("description")
                       var description: String? = "",
                       @SerializedName("main")
                       var main: String? = "",
                       @SerializedName("id")
                       @PrimaryKey var weatherItemId: Int? = 0)


data class Sys(@SerializedName("country")
               var country: String? = "",
               @SerializedName("sunrise")
               var sunrise: Int? = 0,
               @SerializedName("sunset")
               var sunset: Int? = 0,
               @SerializedName("id")
               var sysId: Int? = 0,
               @SerializedName("type")
               var type: Int? = 0)


data class Main(@SerializedName("temp")
                var temp: Double? = 0.0,
                @SerializedName("temp_min")
                var tempMin: Double? = 0.0,
                @SerializedName("humidity")
                var humidity: Int? = 0,
                @SerializedName("pressure")
                var pressure: Double? = 0.0,
                @SerializedName("feels_like")
                var feelsLike: Double? = 0.0,
                @SerializedName("temp_max")
                var tempMax: Double? = 0.0)

@Entity(tableName = "WeatherData")
data class WeatherModel(@SerializedName("visibility")
                        var visibility: Int? = 0,
                        @SerializedName("timezone")
                        var timezone: Int? = 0,
                        @SerializedName("main")
                        @Embedded var main: Main?,
                        @SerializedName("clouds")
                        @Embedded var clouds: Clouds?,
                        @SerializedName("sys")
                        @Embedded var sys: Sys?,
                        @SerializedName("dt")
                        var dt: Int? = 0,
                        @SerializedName("coord")
                        @Embedded var coord: Coord?,
                        @TypeConverters(DataConverter::class)
                        @SerializedName("weather")
                        var weather: List<WeatherItem>??,
                        @SerializedName("name")
                        var name: String? = "",
                        @SerializedName("cod")
                        var cod: Int? = 0,
                        @SerializedName("id")
                        @PrimaryKey var weathModelId: Int? = 0,
                        @SerializedName("base")
                        var base: String? = "",
                        @SerializedName("wind")
                        @Embedded var wind: Wind?)



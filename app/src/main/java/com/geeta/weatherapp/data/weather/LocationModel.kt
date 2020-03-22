package com.geeta.weatherapp.data.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locationData")
data class LocationModel(@PrimaryKey
                         var longitude: Double, var latitude: Double)
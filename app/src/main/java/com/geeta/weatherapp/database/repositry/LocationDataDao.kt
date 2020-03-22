package com.geeta.weatherapp.database.repositry

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.geeta.weatherapp.data.weather.LocationModel
import io.reactivex.Observable
@Dao
interface LocationDataDao {
    @Query("SELECT * from locationData")
    fun getLocation(): Observable<List<LocationModel>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(weatherData: LocationModel)

    @Query("DELETE from locationData")
    fun deleteAll()
}
package com.geeta.weatherapp.worker

import android.content.Context
import androidx.work.Configuration
import androidx.work.Worker
import androidx.work.WorkerParameters
import javax.inject.Inject

interface WeatherWorkerFactory {
    fun create(appContext: Context, params: WorkerParameters): Worker
}

package com.geeta.weatherapp.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import javax.inject.Inject


class WorkerSFactory @Inject constructor() : WeatherWorkerFactory{
    override fun create(appContext: Context, params: WorkerParameters): Worker {
        return WeatherWork(appContext,params)
    }
}

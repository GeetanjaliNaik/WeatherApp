package com.geeta.weatherapp

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.geeta.weatherapp.injection.ApplicationComponent
import com.geeta.weatherapp.injection.DaggerApplicationComponent
//import com.geeta.weatherapp.injection.DaggerApplicationComponent
import dagger.android.*
import javax.inject.Inject


open class WeatherApplication: MultiDexApplication() , HasAndroidInjector {
//    lateinit var applicationComponent: DaggerApplicationComponent

   @Inject
   lateinit var anyDispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    //    val appComponent = DaggerApplicationComponent.create()
    override fun onCreate() {
        super.onCreate()
        appContext=this
        buildComponent()
//        setUpStethoInspector()
    }


    open fun buildComponent(){
        DaggerApplicationComponent.builder().application(this)
            .build().inject(this)
    } /*:ApplicationComponent=
        DaggerApplicationComponent.builder().application(this)
            .build()*/



    companion object {
        var appContext: Context? = null
        var applicationComponent:ApplicationComponent?=null
           fun getApplicationComponet():ApplicationComponent?{
               return applicationComponent
           }

        fun getApplicationContext(): Context? {
            return appContext
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return anyDispatchingAndroidInjector
    }


}
package com.geeta.weatherapp.ui

import android.app.Activity
import android.os.Bundle
import com.cafecraft.aps.ui.base.BaseActivity
import com.geeta.weatherapp.R
import dagger.android.support.DaggerAppCompatActivity

class DemoActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
package com.geeta.weatherapp.utils

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.os.StatFs
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.net.Inet4Address
import java.net.NetworkInterface

import java.net.SocketException

/**
 * Created by Geeta on 04/02/19.
 */
class AppUtils {
    companion object {



        fun isNetworkAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }

            }else{
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            result= activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
            return result
        }
        fun isWifiNetworkAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    else -> false
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                result= activeNetworkInfo != null && activeNetworkInfo.isConnected &&(activeNetworkInfo.type==ConnectivityManager.TYPE_WIFI)
            }

            return result
        }

    }


}
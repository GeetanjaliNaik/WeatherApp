package com.cafecraft.aps.ui.base

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.geeta.weatherapp.R
import com.geeta.weatherapp.utils.ProgressDialogHelper
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


/**
 * Created by Geetanjali on 02/08/17.
 */
abstract class BaseActivity : DaggerAppCompatActivity() {
    protected var progressDialog: Dialog? = null
    private var mDisposables: CompositeDisposable? = null

//    abstract fun busInputReceived(busModal: Any?)

   /* @Inject
    lateinit var rxBus: RxBus*/

    lateinit var progressDialogHelper: ProgressDialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initRxBus()
        progressDialogHelper = ProgressDialogHelper(this)
    }

   /* private fun initRxBus() {
        mDisposables = CompositeDisposable()
        val fragemerConnectEmitter = rxBus.asFlowable().publish()
        mDisposables?.add(fragemerConnectEmitter.subscribe({ o ->
            busInputReceived(o)
        }))
        mDisposables?.add(fragemerConnectEmitter.connect())
    }
*/

    override fun onDestroy() {
        super.onDestroy()
        mDisposables?.clear()
    }

    fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this@BaseActivity, R.style.AppTheme)
        builder.setMessage(message).setTitle(R.string.app_name).setCancelable(true).setIcon(R.drawable.ic_launcher_background)
        builder.create().show()
    }

    fun showAlertWithOkButton(message: String) {
        val builder = AlertDialog.Builder(this@BaseActivity, R.style.AppTheme)
        builder.setMessage(message).setTitle(R.string.app_name).setCancelable(true)
        builder.setNeutralButton("Ok") { dialog, which -> dialog.dismiss() }
        builder.create().show()
    }

    fun showHideProgressDialog(show:Boolean){
        if(show) {
            progressDialogHelper?.show()
        }
        else{
            progressDialogHelper?.dismiss()
        }
    }


}

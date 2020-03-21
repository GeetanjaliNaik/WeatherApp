package com.geeta.weatherapp.ui.base


import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.geeta.weatherapp.R
import com.geeta.weatherapp.utils.ProgressDialogHelper
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Created by Geetanjali on 04/01/18.
 */
open class BaseFragment : Fragment() {
    @Inject lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

//    @Inject lateinit var rxBus: RxBus

    fun supportFragmentInjector(): AndroidInjector<Fragment> = childFragmentInjector

    lateinit var mContext: Context

    private var progressDialogHelper: ProgressDialogHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this!!.activity!!
        progressDialogHelper = ProgressDialogHelper(mContext)
    }

    @TargetApi(23)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onAttachToContext(context)
    }



    /*
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        AndroidSupportInjection.inject(this)
    }

    fun showToastAlert(msg: String) {
        if(!TextUtils.isEmpty(msg)){
            Toast.makeText(this@BaseFragment.mContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun showAlert(msg: String) {
        val simpleAlert = AlertDialog.Builder(activity).create()
        simpleAlert.setTitle(getString(R.string.app_name))
        simpleAlert.setMessage(msg)
        simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", {
            dialogInterface, i ->
            simpleAlert.dismiss()
        })
        simpleAlert.show()
    }

    @SuppressLint("ServiceCast")
    fun hideSoftKeyboard(v: View?) {
        if (v != null) {
            val inputMethodManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun showHideProgressDialog(show:Boolean){
        if(show) {
            progressDialogHelper?.show()
        }
        else{
            progressDialogHelper?.dismiss()
        }
    }

    fun showProgressBar(isShow: Boolean) {
    }

    fun showErrorMessage(message: String) {

    }

    fun showErrorToast(message: String) {
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }
}
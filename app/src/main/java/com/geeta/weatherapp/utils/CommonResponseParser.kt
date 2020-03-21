package com.geeta.weatherapp.utils

import com.geeta.weatherapp.data.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException


/**
 * Created by Geeta on 20/08/18.
 */
class CommonResponseParser {
    object CONSTANT {
        val SUCCESS = 1
        val FAILURE = 0
    }

    object ErrorParser {
        fun parseError(throwableError: Throwable, isReadMessage: Boolean): String? {
            val errorMessage: String
            try {

                if (throwableError is HttpException) {
                    errorMessage = throwableError.response()?.errorBody()!!.string()
                    val status = Gson().fromJson(errorMessage, ErrorResponse::class.java)
                    return if (isReadMessage) {
                        status.mDeveloperMessage
                    } else {
                        status.mHttpStatusCode
                    }
                }
                else if(throwableError is HttpException)
                {
                    errorMessage = throwableError.response()?.errorBody()!!.string()
                    val status = Gson().fromJson(errorMessage, ErrorResponse::class.java)
                    return if (isReadMessage) {
                        status.mDeveloperMessage
                    } else {
                        status.mHttpStatusCode
                    }
                }
                return if (throwableError is IOException) {
                    throwableError.message
                } else
                    throwableError.message
            } catch (throwable: Throwable) {
                return "Something went wrong"
            }

        }

    }

}
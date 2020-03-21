package com.geeta.weatherapp.network

import com.geeta.weatherapp.data.ErrorResponse
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request

            request = chain.request()

        val response = chain.proceed(request)

        val contentType = response.body?.contentType()
        if (response.code == 200 || response.code == 201 || response.code == 202 || response.code == 204) {
            return response;
        } else if (response.code == 400) {
            val toJson = Gson().toJson(checkServerResponse(ErrorResponse("", "Internal Server Error", "400", null), response))
            val body = ResponseBody.create(contentType, toJson)
            return response.newBuilder().body(body).build()
        } else if (response.code == 401) {
            val toJson = Gson().toJson(checkServerResponse(ErrorResponse("", "Unauthorized", "401", null), response))
            val body = ResponseBody.create(contentType, toJson)
            return response.newBuilder().body(body).build()
        } else if (response.code == 403) {
            val toJson = Gson().toJson(checkServerResponse(ErrorResponse("", "Forbidden", "403", null), response))
            val body = ResponseBody.create(contentType, toJson)
            return response.newBuilder().body(body).build()
        } else if (response.code == 404) {
            val toJson = Gson().toJson(checkServerResponse(ErrorResponse("", "Not Found", "404", null), response))
            val body = ResponseBody.create(contentType, toJson)
            return response.newBuilder().body(body).build()
        } else if (response.code == 405) {
            val toJson = Gson().toJson(checkServerResponse(ErrorResponse("", "Method Not Allowed", "403", null), response))
            val body = ResponseBody.create(contentType, toJson)
            return response.newBuilder().body(body).build()
        } else if (response.code == 408) {
            val toJson = Gson().toJson(checkServerResponse(ErrorResponse("", "Request Timeout", "408", null), response))
            val body = ResponseBody.create(contentType, toJson)
            return response.newBuilder().body(body).build()
        } else if (response.code == 415) {
            val toJson = Gson().toJson(checkServerResponse(ErrorResponse("", "Unsupported Media Type", "415", null), response))
            val body = ResponseBody.create(contentType, toJson)
            return response.newBuilder().body(body).build()
        } else if (response.code == 429) {
            val toJson = Gson().toJson(checkServerResponse(ErrorResponse("", "Too Many Request, Please Try after some time", "429", null), response))
            val body = ResponseBody.create(contentType, toJson)
            return response.newBuilder().body(body).build()
        } else if (response.code == 443) {
            val toJson = Gson().toJson(checkServerResponse(ErrorResponse("", "Server Has some problem, Please Try after some time", "443", null), response))
            val body = ResponseBody.create(contentType, toJson)
            return response.newBuilder().body(body).build()
        }
        else if (response.code == 500) {
            val toJson = Gson().toJson(checkServerResponse(ErrorResponse("", "Service unavailable", "500", null), response))
            val body = ResponseBody.create(contentType, toJson)
            return response.newBuilder().body(body).build()
        } else if (response.code == 503) {
            val toJson = Gson().toJson(checkServerResponse(ErrorResponse("", "Service unavailable", "503", null), response))
            val body = ResponseBody.create(contentType, toJson)
            return response.newBuilder().body(body).build()
        } else if (response.code == 504) {
            val toJson = Gson().toJson(checkServerResponse(ErrorResponse("", "Gateway Timeout", "504", null), response))
            val body = ResponseBody.create(contentType, toJson)
            return response.newBuilder().body(body).build()
        } else {
            val toJson = Gson().toJson(checkServerResponse(ErrorResponse("", "Something went wrong", "00", null), response))
            val body = ResponseBody.create(contentType, toJson)
            return response.newBuilder().body(body).build()
        }
    }

    private fun checkServerResponse(defaultResponse: ErrorResponse, response: Response): ErrorResponse {
        var status = defaultResponse;
        val errorBodyValue = response.body?.string()
        try {
            val errorObject = JSONObject(errorBodyValue)
            errorObject.let {
                if (it.has("status")) {
                    if (it.get("status") is Int) {
                        status = ErrorResponse("", errorObject.getString("error"), response.code.toString(), "")
                    } else if (it.get("status") is JSONObject) {
                        status = ErrorResponse("", errorObject.getJSONObject("status").getString("messageDescription"), errorObject.getJSONObject("status").getString("errorCode"),"")
                    }

                } else if (it.has("error_description") && it.getString("error_description") != null) {
                    status = ErrorResponse("", it.getString("error_description"), response.code.toString(), "")
                } else if (it.has("defaultUserMessage") && it.getString("defaultUserMessage") != null) {
                    status = ErrorResponse("", it.getString("defaultUserMessage"), response.code.toString(), "")
                }

            }

        } catch (e: JSONException) {
            return defaultResponse
        }
        return status
    }
}
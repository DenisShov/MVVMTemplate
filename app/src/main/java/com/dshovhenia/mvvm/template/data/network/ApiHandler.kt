package com.dshovhenia.mvvm.template.data.network

import com.dshovhenia.mvvm.template.core.extension.deserialize
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

data class ApiError(
    val errorId: String,
    val message: String,
)

class ApiException(val error: ApiError? = null) : IOException()

class ApiErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.isSuccessful.not()) {
            val responseBody = response.body
            when (val errorResponse = responseBody?.string()) {
                null -> throw ApiException()
                else -> {
                    val apiError: ApiError = Gson().deserialize(errorResponse)
                    Timber.e("Faced an apiError: $apiError")
                    throw ApiException(apiError)
                }
            }
        }
        return response
    }
}

package com.dshovhenia.mvvm.template.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideCoinGeckoApi(
    baseUrl: String,
    okHttpClient: OkHttpClient,
    gson: Gson
): CoinGeckoApi {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(CoinGeckoApi::class.java)
}

fun provideHttpClient(
    isLogEnabled: Boolean
): OkHttpClient {
    return OkHttpClient.Builder()
        .addLoggingInterceptor(isLogEnabled)
        .addInterceptor(ApiErrorInterceptor())
        .build()
}

fun provideGson(): Gson {
    return GsonBuilder()
        .create()
}

private fun OkHttpClient.Builder.addLoggingInterceptor(isLogEnabled: Boolean) = apply {
    if (!isLogEnabled) {
        return@apply
    }
    val loggingInterceptor = HttpLoggingInterceptor { message -> println(message) }
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    addInterceptor(loggingInterceptor)
}

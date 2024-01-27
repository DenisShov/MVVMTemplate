package com.dshovhenia.mvvm.template.data.network

import com.dshovhenia.mvvm.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.template.data.entity.CryptoChartData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface CoinGeckoApi {

    @GET("coins/markets")
    suspend fun getCoinsMarkets(
        @Query("vs_currency") currency: String = "usd",
        @Query("page") page: Int = 1,
        @Query("per_page") numCoinsPerPage: Int = 20,
        @Query("order") order: String = "market_cap_desc",
        @Query("sparkline") includeSparkline7dData: Boolean = false,
        @Query("price_change_percentage") priceChangePercentageIntervals: String = "",
        @Query("ids") coinIds: String? = null
    ): List<CoinMarkets>

    @GET("coins/{id}/market_chart")
    suspend fun getCoinChart(
        @Path("id") id: String,
        @QueryMap map: Map<String, String>
    ): CryptoChartData
}

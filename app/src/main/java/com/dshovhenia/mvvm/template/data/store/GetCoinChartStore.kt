package com.dshovhenia.mvvm.template.data.store

import com.dshovhenia.mvvm.template.data.entity.CryptoChartData
import com.dshovhenia.mvvm.template.data.network.CoinGeckoApi

class GetCoinChartStore(private val coinGeckoApi: CoinGeckoApi) {
    suspend fun getCoinChart(
        id: String,
        map: MutableMap<String, String>,
    ): CryptoChartData {
        return coinGeckoApi.getCoinChart(id, map)
    }
}

package com.dshovhenia.mvvm.template.data.store

import com.dshovhenia.mvvm.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.template.data.network.CoinGeckoApi

class GetCoinsMarketsStore(private val coinGeckoApi: CoinGeckoApi) {
    suspend fun getCoinsMarkets(): List<CoinMarkets> {
        return coinGeckoApi.getCoinsMarkets()
    }
}

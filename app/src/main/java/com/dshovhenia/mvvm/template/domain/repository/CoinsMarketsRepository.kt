package com.dshovhenia.mvvm.template.domain.repository

import com.dshovhenia.mvvm.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.template.data.entity.CryptoChartData
import com.dshovhenia.mvvm.template.data.store.GetCoinChartStore
import com.dshovhenia.mvvm.template.data.store.GetCoinsMarketsStore

class CoinsMarketsRepository(
    private val getCoinsMarketsStore: GetCoinsMarketsStore,
    private val getCoinChartStore: GetCoinChartStore
) {

    suspend fun getCoinsMarkets(): List<CoinMarkets> {
        return getCoinsMarketsStore.getCoinsMarkets()
    }

    suspend fun getCoinChart(
        id: String,
        map: MutableMap<String, String>
    ): CryptoChartData {
        return getCoinChartStore.getCoinChart(id, map)
    }


}

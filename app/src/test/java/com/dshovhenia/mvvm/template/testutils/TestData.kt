package com.dshovhenia.mvvm.template.testutils

import com.dshovhenia.mvvm.template.Constants
import com.dshovhenia.mvvm.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.template.data.entity.CryptoChartData
import java.math.BigDecimal

val mockedCoinMarkets =
    CoinMarkets(
        id = "bitcoin",
        symbol = "btc",
        name = "Bitcoin",
        image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1696501400",
        currentPrice = 42355.0,
        marketCap = 830941278718.0,
        marketCapRank = 1,
        fullyDilutedValuation = 889416744973.0,
        totalVolume = 19253276366.0,
        high24h = 43520.0,
        low24h = 42284.0,
        priceChange24h = -567.0283389443211,
        priceChangePercentage24h = -1.32108,
        marketCapChange24h = -9548616604.671509,
        marketCapChangePercentage24h = -1.13608,
        circulatingSupply = 19619337.0,
        totalSupply = 21000000.0,
        maxSupply = 21000000.0,
        ath = 69045.0,
        athChangePercentage = -38.66504,
        athDate = "2021-11-10T14:24:11.849Z",
        atl = 67.81,
        atlChangePercentage = 62352.74957,
        atlDate = "2013-07-06T00:00:00.000Z",
        lastUpdated = "2024-02-05T21:01:53.259Z",
    )

fun getMockedCoinMarketsParameters(): HashMap<String, String> {
    val map = HashMap<String, String>()
    map[Constants.API_PARAM_KEY_SYMBOL] = mockedCoinMarkets.id
    map[Constants.API_PARAM_KEY_VS_CURRENCY] = Constants.API_PARAM_VALUE_USD
    map[Constants.API_PARAM_KEY_DAYS] = Constants.ONE_DAY
    return map
}

val mockedCryptoChartData =
    CryptoChartData(
        listOf(listOf(BigDecimal(100.0))),
        listOf(listOf(BigDecimal(100.0))),
        listOf(listOf(BigDecimal(100.0))),
    )

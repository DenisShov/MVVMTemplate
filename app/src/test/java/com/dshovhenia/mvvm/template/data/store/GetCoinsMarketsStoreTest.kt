package com.dshovhenia.mvvm.template.data.store

import com.dshovhenia.mvvm.template.data.network.CoinGeckoApi
import com.dshovhenia.mvvm.template.testutils.CoroutineTest
import com.dshovhenia.mvvm.template.testutils.mockedCoinMarkets
import com.dshovhenia.mvvm.template.testutils.startKoin
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class GetCoinsMarketsStoreTest : CoroutineTest() {
    private val store: GetCoinsMarketsStore by inject()
    private val coinGeckoApi: CoinGeckoApi = mockk()

    @Before
    fun setUp() {
        clearAllMocks()
        startKoin {
            single { coinGeckoApi }
        }
    }

    @Test
    fun `check repository fetches coins correctly`(): Unit =
        runTest {
            val coinsMarkets = listOf(mockedCoinMarkets)

            coEvery { coinGeckoApi.getCoinsMarkets() } returns coinsMarkets

            store.getCoinsMarkets().shouldBe(coinsMarkets)
        }
}

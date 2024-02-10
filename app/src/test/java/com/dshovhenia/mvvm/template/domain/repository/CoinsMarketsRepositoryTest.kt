package com.dshovhenia.mvvm.template.domain.repository

import com.dshovhenia.mvvm.template.data.store.GetCoinChartStore
import com.dshovhenia.mvvm.template.data.store.GetCoinsMarketsStore
import com.dshovhenia.mvvm.template.testutils.CoroutineTest
import com.dshovhenia.mvvm.template.testutils.getMockedCoinMarketsParameters
import com.dshovhenia.mvvm.template.testutils.mockedCoinMarkets
import com.dshovhenia.mvvm.template.testutils.mockedCryptoChartData
import com.dshovhenia.mvvm.template.testutils.startKoin
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class CoinsMarketsRepositoryTest : CoroutineTest() {
    private val repository: CoinsMarketsRepository by inject()

    private val getCoinsMarketsStore: GetCoinsMarketsStore = mockk()
    private val getCoinChartStore: GetCoinChartStore = mockk()

    @Before
    fun setUp() {
        clearAllMocks()
        startKoin {
            single { getCoinsMarketsStore }
            single { getCoinChartStore }
        }
    }

    @Test
    fun `check repository fetches coins correctly`(): Unit =
        runTest {
            val coins = listOf(mockedCoinMarkets)

            coEvery { getCoinsMarketsStore.getCoinsMarkets() } returns coins

            repository.getCoinsMarkets().shouldBe(coins)
        }

    @Test
    fun `check repository fetches coin chart correctly`(): Unit =
        runTest {
            val coinId = mockedCoinMarkets.id
            val map = getMockedCoinMarketsParameters()

            coEvery { getCoinChartStore.getCoinChart(coinId, map) } returns mockedCryptoChartData

            repository.getCoinChart(coinId, map).shouldBe(mockedCryptoChartData)
        }
}

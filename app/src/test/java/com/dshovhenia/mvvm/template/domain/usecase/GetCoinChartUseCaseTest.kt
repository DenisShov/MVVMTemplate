package com.dshovhenia.mvvm.template.domain.usecase

import com.dshovhenia.mvvm.template.data.entity.CryptoChartData
import com.dshovhenia.mvvm.template.domain.error.AppError
import com.dshovhenia.mvvm.template.domain.repository.CoinsMarketsRepository
import com.dshovhenia.mvvm.template.testutils.CoroutineTest
import com.dshovhenia.mvvm.template.testutils.deserializeFromFile
import com.dshovhenia.mvvm.template.testutils.getMockedCoinMarketsParameters
import com.dshovhenia.mvvm.template.testutils.mockedCoinMarkets
import com.dshovhenia.mvvm.template.testutils.startKoin
import com.dshovhenia.mvvm.template.testutils.testFailure
import com.dshovhenia.mvvm.template.testutils.testSuccess
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class GetCoinChartUseCaseTest : CoroutineTest() {
    private val useCase: GetCoinChartUseCase by inject()

    private val coinsMarketsRepository: CoinsMarketsRepository = mockk()

    @Before
    fun setUp() {
        clearAllMocks()
        startKoin {
            single { coinsMarketsRepository }
        }
    }

    @Test
    fun `check use case gets coin chart`() =
        runTest {
            val response = deserializeFromFile<CryptoChartData>("data/coin-chart.json")

            val coinId = mockedCoinMarkets.id
            val map = getMockedCoinMarketsParameters()

            coEvery { coinsMarketsRepository.getCoinChart(coinId, map) } returns response

            useCase.launch(coinId, map).testSuccess {
                it.shouldBe(response)
            }
        }

    @Test
    fun `check use case handle error`() =
        runTest {
            val coinId = mockedCoinMarkets.id
            val map = getMockedCoinMarketsParameters()

            coEvery { coinsMarketsRepository.getCoinChart(coinId, map) } throws mockk<Exception>()

            useCase.launch(coinId, map).testFailure {
                it.shouldBeInstanceOf(AppError.GeneralError::class.java)
            }
        }
}

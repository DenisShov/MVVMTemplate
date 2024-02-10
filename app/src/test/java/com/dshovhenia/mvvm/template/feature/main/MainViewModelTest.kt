package com.dshovhenia.mvvm.template.feature.main

import com.dshovhenia.mvvm.template.domain.error.AppError
import com.dshovhenia.mvvm.template.domain.functional.DataResult
import com.dshovhenia.mvvm.template.domain.usecase.GetCoinChartUseCase
import com.dshovhenia.mvvm.template.domain.usecase.GetCoinsMarketsUseCase
import com.dshovhenia.mvvm.template.testutils.CoroutineTest
import com.dshovhenia.mvvm.template.testutils.mockedCoinMarkets
import com.dshovhenia.mvvm.template.testutils.mockedCryptoChartData
import com.dshovhenia.mvvm.template.testutils.startKoin
import com.dshovhenia.mvvm.template.testutils.testObserver
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class MainViewModelTest : CoroutineTest() {
    private val viewModel: MainViewModel by inject()

    private val getCoinsMarketsUseCase: GetCoinsMarketsUseCase = mockk()
    private val getCoinChartUseCase: GetCoinChartUseCase = mockk()

    @Before
    fun setUp() {
        clearAllMocks()
        startKoin {
            single { getCoinsMarketsUseCase }
            single { getCoinChartUseCase }
        }
    }

    @Test
    fun `check view model gets coins markets`() {
        val event = viewModel.coins.testObserver()
        val coins = listOf(mockedCoinMarkets)
        coEvery { getCoinsMarketsUseCase.launch() } returns DataResult.success(coins)

        viewModel.getCoinsMarkets()

        event.shouldContainValues(coins)
    }

    @Test
    fun `check view model handles get coins failures`() {
        val event = viewModel.coins.testObserver()
        val failures = viewModel.failure.testObserver()

        coEvery { getCoinsMarketsUseCase.launch() } returns DataResult.failure(AppError.MissingNetworkConnection)

        viewModel.getCoinsMarkets()

        event.shouldBeEmpty()
        failures.shouldContainEvents(AppError.MissingNetworkConnection)
    }

    @Test
    fun `check view model init coin chart for one day`() {
        val chartData = viewModel.cryptoChartLiveData.testObserver()

        val coinId = mockedCoinMarkets.id
        val data = mockedCryptoChartData

        coEvery { getCoinChartUseCase.launch(any(), any()) } returns
            DataResult.success(
                data,
            )

        viewModel.initCryptoChart(coinId)
        chartData.shouldContainValues(data)
    }

    @Test
    fun `check view model handles init coin chart failures`() {
        val event = viewModel.cryptoChartLiveData.testObserver()
        val failures = viewModel.failure.testObserver()

        val coinId = mockedCoinMarkets.id

        coEvery {
            getCoinChartUseCase.launch(any(), any())
        } returns DataResult.failure(AppError.MissingNetworkConnection)

        viewModel.initCryptoChart(coinId)

        event.shouldBeEmpty()
        failures.shouldContainEvents(AppError.MissingNetworkConnection)
    }

    @Test
    fun `check view model opens detail screen`() {
        val event = viewModel.event.testObserver()

        viewModel.openDetailScreen(mockedCoinMarkets)

        event.shouldContainEvents(MainViewModel.Event.ShowDetailScreen(mockedCoinMarkets))
    }
}

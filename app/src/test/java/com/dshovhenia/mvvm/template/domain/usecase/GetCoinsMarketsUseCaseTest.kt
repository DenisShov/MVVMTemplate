package com.dshovhenia.mvvm.template.domain.usecase

import com.dshovhenia.mvvm.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.template.domain.error.AppError
import com.dshovhenia.mvvm.template.domain.repository.CoinsMarketsRepository
import com.dshovhenia.mvvm.template.testutils.CoroutineTest
import com.dshovhenia.mvvm.template.testutils.deserializeFromFile
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

class GetCoinsMarketsUseCaseTest : CoroutineTest() {
    private val useCase: GetCoinsMarketsUseCase by inject()

    private val coinsMarketsRepository: CoinsMarketsRepository = mockk()

    @Before
    fun setUp() {
        clearAllMocks()
        startKoin {
            single { coinsMarketsRepository }
        }
    }

    @Test
    fun `check use case gets coins`() =
        runTest {
            val response = deserializeFromFile<List<CoinMarkets>>("data/coins-response.json")

            coEvery { coinsMarketsRepository.getCoinsMarkets() } returns response

            useCase.launch().testSuccess {
                it.shouldBe(response)
            }
        }

    @Test
    fun `check use case handle error`() =
        runTest {
            coEvery { coinsMarketsRepository.getCoinsMarkets() } throws mockk<Exception>()

            useCase.launch().testFailure {
                it.shouldBeInstanceOf(AppError.GeneralError::class.java)
            }
        }
}

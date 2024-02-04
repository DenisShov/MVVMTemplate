package com.dshovhenia.mvvm.template.domain.usecase

import com.dshovhenia.mvvm.template.domain.error.wrapResult
import com.dshovhenia.mvvm.template.domain.repository.CoinsMarketsRepository

class GetCoinsMarketsUseCase(private val coinsMarketsRepository: CoinsMarketsRepository) {
    suspend fun launch() =
        wrapResult {
            coinsMarketsRepository.getCoinsMarkets()
        }
}

package com.dshovhenia.mvvm.template.domain.usecase

import com.dshovhenia.mvvm.template.domain.error.wrapResult
import com.dshovhenia.mvvm.template.domain.repository.CoinsMarketsRepository

class GetCoinChartUseCase(private val coinsMarketsRepository: CoinsMarketsRepository) {
    suspend fun launch(
        id: String,
        map: MutableMap<String, String>,
    ) = wrapResult {
        coinsMarketsRepository.getCoinChart(id, map)
    }
}

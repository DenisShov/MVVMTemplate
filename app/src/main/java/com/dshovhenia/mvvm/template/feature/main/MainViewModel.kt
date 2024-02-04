package com.dshovhenia.mvvm.template.feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dshovhenia.mvvm.template.Constants
import com.dshovhenia.mvvm.template.core.extension.ConsumableLiveData
import com.dshovhenia.mvvm.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.template.data.entity.CryptoChartData
import com.dshovhenia.mvvm.template.domain.usecase.GetCoinChartUseCase
import com.dshovhenia.mvvm.template.domain.usecase.GetCoinsMarketsUseCase
import com.dshovhenia.mvvm.template.feature.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val getCoinsMarketsUseCase: GetCoinsMarketsUseCase,
    private val getCoinChartUseCase: GetCoinChartUseCase,
) : BaseViewModel() {
    val event = ConsumableLiveData<Event>()
    val coins = MutableLiveData<List<CoinMarkets>>()

    val cryptoChartLiveData = MutableLiveData<CryptoChartData>()

    var paramMap = HashMap<String, String>()

    fun getCoinsMarkets() {
        viewModelScope.launch {
            withContext(dispatcher) {
                getCoinsMarketsUseCase.launch()
            }.success {
                coins.value = it
            }.failure {
                handleFailure(it)
            }
        }
    }

    fun initCryptoChart(coinId: String) {
        initParamMap(coinId)

        getCryptoChart(Constants.ONE_DAY)
    }

    private fun initParamMap(coinId: String) {
        val map: MutableMap<String, String> = HashMap()
        map[Constants.API_PARAM_KEY_SYMBOL] = coinId
        map[Constants.API_PARAM_KEY_VS_CURRENCY] = Constants.API_PARAM_VALUE_USD
        map[Constants.API_PARAM_KEY_DAYS] = Constants.ONE_DAY
        paramMap = map as HashMap<String, String>
    }

    fun getCryptoChart(days: String) {
        paramMap[Constants.API_PARAM_KEY_DAYS] = days

        viewModelScope.launch {
            withContext(dispatcher) {
                getCoinChartUseCase.launch(paramMap[Constants.API_PARAM_KEY_SYMBOL]!!, paramMap)
            }.success {
                cryptoChartLiveData.value = it
            }.failure {
                handleFailure(it)
            }
        }
    }

    fun openDetailScreen(coin: CoinMarkets) {
        event.setConsumableValue(Event.ShowDetailScreen(coin))
    }

    sealed class Event {
//        object ShowLoading : Event()
//        object HideLoading : Event()

        data class ShowDetailScreen(val coin: CoinMarkets) : Event()
    }
}

package com.dshovhenia.mvvm.template.core.di

import com.dshovhenia.mvvm.template.BuildConfig
import com.dshovhenia.mvvm.template.data.network.provideCoinGeckoApi
import com.dshovhenia.mvvm.template.data.network.provideGson
import com.dshovhenia.mvvm.template.data.network.provideHttpClient
import com.dshovhenia.mvvm.template.data.store.GetCoinChartStore
import com.dshovhenia.mvvm.template.data.store.GetCoinsMarketsStore
import com.dshovhenia.mvvm.template.domain.repository.CoinsMarketsRepository
import com.dshovhenia.mvvm.template.domain.usecase.GetCoinChartUseCase
import com.dshovhenia.mvvm.template.domain.usecase.GetCoinsMarketsUseCase
import com.dshovhenia.mvvm.template.feature.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val frameworkModule = module {
    single { Dispatchers.IO }
}

fun networkModule(networkLogging: Boolean) = module {
    single(apiBaseUrlNamed) { BuildConfig.API_BASE_URL }
    single { provideGson() }
    single { provideHttpClient(networkLogging) }
    single {
        provideCoinGeckoApi(
            baseUrl = get(apiBaseUrlNamed), get(), get()
        )
    }
}

val storeModule = module {
    single { GetCoinsMarketsStore(get()) }
    single { GetCoinChartStore(get()) }
}

val repositoryModule = module {
    single { CoinsMarketsRepository(get(), get()) }
}

val useCaseModule = module {
    factory { GetCoinsMarketsUseCase(get()) }
    factory { GetCoinChartUseCase(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get()) }
}

val apiBaseUrlNamed = named("API_BASE_URL")

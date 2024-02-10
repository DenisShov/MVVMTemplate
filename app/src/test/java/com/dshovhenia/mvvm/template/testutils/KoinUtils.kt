package com.dshovhenia.mvvm.template.testutils

import com.dshovhenia.mvvm.template.BuildConfig
import com.dshovhenia.mvvm.template.core.di.frameworkModule
import com.dshovhenia.mvvm.template.core.di.networkModule
import com.dshovhenia.mvvm.template.core.di.repositoryModule
import com.dshovhenia.mvvm.template.core.di.storeModule
import com.dshovhenia.mvvm.template.core.di.useCaseModule
import com.dshovhenia.mvvm.template.core.di.viewModelModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.core.KoinApplication
import org.koin.core.logger.EmptyLogger
import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("EXPERIMENTAL_API_USAGE")
fun startKoin(overridesModuleDeclaration: ModuleDeclaration = {}): KoinApplication =
    org.koin.core.context.startKoin {
        modules(
            frameworkModule,
            networkModule(networkLogging = BuildConfig.DEBUG),
            storeModule,
            repositoryModule,
            useCaseModule,
            viewModelModule,
            module {
                single<CoroutineDispatcher> { UnconfinedTestDispatcher() }
            },
            module(moduleDeclaration = overridesModuleDeclaration),
        )
        logger(EmptyLogger())
    }

package com.dshovhenia.mvvm.template

import android.app.Application
import com.dshovhenia.mvvm.template.core.di.frameworkModule
import com.dshovhenia.mvvm.template.core.di.networkModule
import com.dshovhenia.mvvm.template.core.di.repositoryModule
import com.dshovhenia.mvvm.template.core.di.storeModule
import com.dshovhenia.mvvm.template.core.di.useCaseModule
import com.dshovhenia.mvvm.template.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setupKoin(this)
    }

    private fun setupKoin(application: Application) {
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            androidContext(application)
            modules(
                frameworkModule,
                networkModule(networkLogging = BuildConfig.DEBUG),
                storeModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
            )
        }
    }
}

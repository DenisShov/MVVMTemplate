package com.dshovhenia.mvvm.template.testutils

import android.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.koin.test.junit5.AutoCloseKoinTest

@ExperimentalCoroutinesApi
open class KoinCoroutineTest : AutoCloseKoinTest() {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesMainDispatcherRule()
}
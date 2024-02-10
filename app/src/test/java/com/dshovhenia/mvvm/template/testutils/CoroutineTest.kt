package com.dshovhenia.mvvm.template.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Rule
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

open class CoroutineTest : KoinTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesMainDispatcherRule()

    @After
    fun after() {
        stopKoin()
    }
}

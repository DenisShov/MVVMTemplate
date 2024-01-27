package com.dshovhenia.mvvm.template.feature.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dshovhenia.mvvm.template.core.extension.ConsumableEvent
import com.dshovhenia.mvvm.template.domain.error.AppError

open class BaseViewModel : ViewModel() {
    val failure = MutableLiveData<ConsumableEvent<AppError>>()

    fun handleFailure(error: AppError) {
        this.failure.value = ConsumableEvent(error)
    }
}

package com.dshovhenia.mvvm.template.feature.base

import com.dshovhenia.mvvm.template.R
import com.dshovhenia.mvvm.template.domain.error.AppError

val AppError.stringRes: Int
    get() {
        return when (this) {
            is AppError.MissingNetworkConnection -> R.string.message_check_internet_connection
            else -> R.string.label_something_went_wrong
        }
    }

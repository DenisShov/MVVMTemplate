package com.dshovhenia.mvvm.template.testutils

import com.dshovhenia.mvvm.template.domain.error.AppError
import com.dshovhenia.mvvm.template.domain.functional.DataResult
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Assert

fun failIfError(error: AppError) {
    Assert.fail("Something went wrong. Error callback should not be triggered here. Error : $error")
}

fun failIfSuccess(value: Any?) {
    Assert.fail("Something went wrong. Successful callback should not be triggered here. Value : $value")
}

fun <SuccessType> DataResult<SuccessType, AppError>.testSuccess(onSuccess: (SuccessType) -> Unit) {
    success(onSuccess)
    failure {
        failIfError(it)
    }
}

fun <SuccessType, FailureType> DataResult<SuccessType, FailureType>.testFailure(
    onFailure: (FailureType) -> Unit,
) {
    failure(onFailure)
    success(::failIfSuccess)
}

@SuppressWarnings("UnusedPrivateMember")
fun doNothingForSuccess(value: Any) {
    // Everything is OK. Just skipp.
}

fun shouldBeError(it: Any) {
    it.shouldBeInstanceOf(AppError::class)
}

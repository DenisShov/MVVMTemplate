package com.dshovhenia.mvvm.template.domain.functional

import com.dshovhenia.mvvm.template.domain.error.AppError

/**
 * Represents a value of one of two possible types
 * Instances of [DataResult] are result an instance of [Failure] or [Success].
 * FP Convention dictates that [Failure] is used for "failure"
 * and [Success] is used for "success".
 *
 * @see Failure
 * @see Success
 */
sealed class DataResult<out SuccessType, out FailureType> {
    /** * Represents the failure side of [DataResult] class which by convention is "Failure". */
    data class Failure<out L>(val value: L) : DataResult<Nothing, L>()

    /** * Represents the success side of [DataResult] class which by convention is "Success". */
    data class Success<out R>(val value: R) : DataResult<R, Nothing>()

    val isSuccess get() = this is Success<SuccessType>
    val isFailure get() = this is Failure<FailureType>

    fun fold(onSuccess: (SuccessType) -> Unit, onFailure: (FailureType) -> Unit) {
        when (this) {
            is Failure -> onFailure(value)
            is Success -> onSuccess(value)
        }
    }

    fun success(onSuccess: (SuccessType) -> Unit): DataResult<SuccessType, FailureType> {
        fold(onSuccess, {})

        return this
    }

    fun failure(onFailure: (FailureType) -> Unit): DataResult<SuccessType, FailureType> {
        fold({}, onFailure)

        return this
    }

    fun <L> newFailure(a: L) = Failure(a)
    fun <R> newSuccess(b: R) = Success(b)

    companion object {
        fun <T> success(from: T): DataResult<T, AppError> {
            return Success(from)
        }

        fun <T, Error> failure(from: Error): DataResult<T, Error> {
            return Failure(from)
        }
    }
}

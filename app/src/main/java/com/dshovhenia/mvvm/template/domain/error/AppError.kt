package com.dshovhenia.mvvm.template.domain.error

import com.dshovhenia.mvvm.template.data.network.ApiException
import com.dshovhenia.mvvm.template.domain.functional.DataResult
import java.net.ConnectException
import java.net.UnknownHostException

sealed class AppError {
    object MissingNetworkConnection : AppError()
    data class GeneralError(val exception: Throwable) : AppError()
    data class ApiError(val exception: ApiException) : AppError()
}

@SuppressWarnings("TooGenericExceptionCaught")
inline fun <T> wrapResult(block: () -> T): DataResult<T, AppError> {
    return try {
        DataResult.success(block())
    } catch (exception: UnknownHostException) {
        DataResult.failure(AppError.MissingNetworkConnection)
    } catch (exception: ConnectException) {
        DataResult.failure(AppError.MissingNetworkConnection)
    } catch (exception: ApiException) {
        DataResult.failure(AppError.ApiError(exception))
    } catch (exception: Exception) {
        DataResult.failure(AppError.GeneralError(exception))
    }
}

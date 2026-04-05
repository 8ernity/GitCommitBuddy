package com.gitcommitbuddy.data.api

/**
 * Generic sealed wrapper for all API / repository results.
 * Only two concrete states — Loading belongs in the UI layer (ViewModel).
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
}

val <T> ApiResult<T>.isSuccess get() = this is ApiResult.Success
fun <T> ApiResult<T>.dataOrNull(): T? = (this as? ApiResult.Success)?.data

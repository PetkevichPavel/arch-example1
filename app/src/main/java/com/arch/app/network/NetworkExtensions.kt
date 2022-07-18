package com.arch.app.network

import com.arch.app.common.core.TaskResponse
import com.arch.app.common.core.getMessageViaCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * Executes block in try/catch and returns a TaskResponse.
 */
internal suspend fun <T> executeSafe(block: suspend CoroutineScope.() -> T): TaskResponse<T> =
    try {
        val data = withContext(Dispatchers.Default) { block() }
        TaskResponse.Data(data)
    } catch (e: Throwable) {
        when (e) {
            is IOException -> TaskResponse.NetworkError
            is HttpException -> TaskResponse.Exception(e.getMessageViaCode())
            else -> TaskResponse.Exception()
        }
    }

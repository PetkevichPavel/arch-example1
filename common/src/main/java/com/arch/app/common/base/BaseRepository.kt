package com.arch.app.common.base

import com.arch.app.common.base.BaseConstants.Numbers.I400
import com.arch.app.common.core.TaskResponse
import com.arch.app.common.core.getMessageBasedCode
import com.arch.app.common.core.getMessageViaCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

open class BaseRepository() {
    /**
     * Executes block in try/catch and returns a TaskResponse.
     */
    suspend fun <T> executeSafe(block: suspend CoroutineScope.() -> T): TaskResponse<T> =
        try {
            val data = withContext(Dispatchers.Default) { block() }
            if (data is Response<*>) {
                when {
                    data.code() >= I400 -> TaskResponse.Exception(data.code().getMessageBasedCode())
                    else -> TaskResponse.Data(data)
                }
            } else TaskResponse.Data(data)
        } catch (e: Throwable) {
            when (e) {
                is IOException -> TaskResponse.NetworkError
                is HttpException -> TaskResponse.Exception(e.getMessageViaCode())
                else -> TaskResponse.Exception()
            }
        }

    /**
     * <T> TaskResponse<T> extension function for processing response.
     * @param flow - is represents an entity that accepts values emitted by the [Flow] (more about @see[FlowCollector]) or ignore.
     */
    suspend fun <T> TaskResponse<T>.emitResponse(flow: FlowCollector<TaskResponse<T>>) =
        flow.emit(this@emitResponse)

    /**
     * <T> TaskResponse<T> extension function for processing only non-null success response.
     * @param flow - is represents an entity that accepts values emitted by the [Flow] (more about @see[FlowCollector]) or ignore.
     */
    suspend fun <T> TaskResponse<T>.emitSuccessOnly(flow: FlowCollector<T>) {
        this.data?.let { flow.emit(it) }
    }

}

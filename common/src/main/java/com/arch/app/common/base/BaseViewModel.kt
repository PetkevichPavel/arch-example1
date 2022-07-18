package com.arch.app.common.base

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.arch.app.common.core.ResponseState
import com.arch.app.common.core.ResponseState.LoadingState.*
import com.arch.app.common.core.TaskResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Base class representing ViewModel. It allows to observe [LiveData]s, which is useful
 * for observing ViewState. Observers are automatically removed when ViewModel is
 * no longer used and will be destroyed.
 */
abstract class BaseViewModel<VS : ViewState> : ViewModel(), DefaultLifecycleObserver {

    abstract val viewState: VS

    private val observers = mutableMapOf<Observer<Any>, LiveData<Any>>()

    override fun onCleared() {
        removeObservers()
        super.onCleared()
    }

    private fun removeObservers() {
        observers.forEach { entry -> entry.value.removeObserver(entry.key) }
        observers.clear()
    }

    /**
     * <T> TaskResponse<T> extension function for processing response.
     * @param flow - is represents an entity that accepts values emitted by the [Flow] (more about @see[FlowCollector]).
     * @param doError - provide [ResponseState.Error] for future error processing.
     * @param errorTitleResId - string resource id for error title.
     */
    suspend fun <T> Flow<TaskResponse<T>>.emitViaResponse(
        state: MutableStateFlow<T>,
        @StringRes errorTitleResId: Int,
        doError: suspend (ResponseState.Error) -> Unit
    ) = collect { response ->
        handleError(response) { errorMsgId, msg ->
            response.data?.let {
                state.emit(it)
            } ?: doError(error(errorTitleResId, errorMsgId, msg))
        }
    }

    private suspend fun <T> handleError(
        response: TaskResponse<T>,
        dataInvoke: suspend (Int?, String?) -> Unit
    ) {
        response.errorMsg?.let { msgId ->
            dataInvoke(msgId, null)
        } ?: dataInvoke(null, null)
    }

    /**
     * Function for prepare [ResponseState.Error] object.
     * @param titleResId - title resource id.
     * @param messageResId - message as resource id.
     * @param message - message as string.
     */
    fun error(titleResId: Int? = null, messageResId: Int? = null, message: String? = null) =
        ResponseState.Error(titleResId, messageResId, message = message)

    /**
     * Takes care about rendering of blocking loading state.
     * Render loading start -> execute block -> render loading stop.
     * @param block - suspend lambda function for doing some job between emits.
     */
    suspend fun MutableLiveData<ResponseState>.swipeRefreshLoading(block: suspend () -> Unit) {
        with(this) {
            emit(SwipeRefresh.Start)
            block.invoke()
            emit(SwipeRefresh.Stop)
        }
    }

    /**
     * Flow extension above TaskResponse<T> function for processing response.
     * @param stateFlow - a mutable StateFlow that provides a setter for value.
     */
    suspend fun <T> Flow<TaskResponse<T>>.emitSuccessOnly(stateFlow: MutableStateFlow<T>) {
        collect { response ->
            response.data?.let { stateFlow.emit(it) }
        }
    }

    /**
     * Generic extension function on MutableLiveData, which will emit data into value.
     * @param data - data <T>.
     */
    fun <T> MutableLiveData<T>.emit(data: T?) {
        value = data
    }
}

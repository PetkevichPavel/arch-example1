package com.arch.app.common.core

import com.arch.app.assets.R
import com.arch.app.common.base.BaseConstants.Numbers.I0
import com.arch.app.common.core.ResponseState.Action.sizeStart
import com.arch.app.common.core.ResponseState.Action.sizeStop
import com.arch.app.common.utils.getSizeWith

sealed class ResponseState {

    object Action {
        private const val START = "Start"
        private const val STOP = "Stop"

        val MutableList<ResponseState>.sizeStart
            get() = getSizeWith(START)
        val MutableList<ResponseState>.sizeStop
            get() = getSizeWith(STOP)
    }

    val isLoadingState
        get() = this is LoadingState

    object Init : ResponseState()

    sealed class LoadingState : ResponseState() {
        sealed class SwipeRefresh : LoadingState() {
            object Start : SwipeRefresh()
            object Stop : SwipeRefresh()
        }
    }

    data class Error(
        val titleResId: Int? = null,
        val messageResId: Int? = null,
        val msgParamResId: Int? = null,
        val message: String? = null
    ) {
        val asDialogState
            get() = ErrorState.Dialog(this)
    }

    sealed class ErrorState(val error: Error) : ResponseState() {
        class Dialog(error: Error) : ErrorState(error)
        class Snack(error: Error) : ErrorState(error)
    }
}

/**
 * Extension function on ResponseState.Error.
 * Handle error and return it as a callback with two parameters (Int?, Int, String?).
 * First parameter is TitleResId(it can be null), second parameter is MessageResId and last is message as a String also is optional.
 * Based on `ResponseState.Error` function will choose which params should return into callback.
 */
fun ResponseState.Error.handleError(error: (Int, Int, String?) -> Unit) {
    titleResId?.takeIf { messageResId != null || !message.isNullOrBlank() }?.apply {
        error.invoke(this, messageResId ?: R.string.general_error, message)
    }
}

/**
 * Extension function on ResponseState.Error.
 * Handle error and return it as a callback with two parameters (Int?, Int, String?).
 * First parameter is TitleResId(it can be null), second parameter is MessageResId and last is message as a String also is optional.
 * Based on `ResponseState.Error` function will choose which params should return into callback.
 */
fun ResponseState.Error.handleSimpleError(error: (Int, String?) -> Unit) {
    takeIf { messageResId != null || !message.isNullOrBlank() }?.apply {
        error.invoke(messageResId ?: R.string.general_error, message)
    }
}

/**
 * MutableList<ResponseState> extension function for checking if is loading state or not.
 * Main idea the LoadingState has to be always start and stop, therefor the size of start function has to be same as stop. And based on that we can determinate current state.
 * @param state - callback invoking state of loading.
 * @return Boolean - returning current loading state, true in case of loading is running otherwise false.
 */
fun MutableList<ResponseState>.checkLoading(state: ((Boolean) -> Unit?)? = null): Boolean {
    if (sizeStart == sizeStop || sizeStart == I0 && sizeStop > I0) clear()
    return (sizeStart != sizeStop).also { state?.invoke(it) }
}

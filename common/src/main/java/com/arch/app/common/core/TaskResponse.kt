package com.arch.app.common.core

sealed class TaskResponse<out T>(
    val data: T? = null,
    val errorMsg: Int? = null
) {
    class Data<out T>(data: T?) : TaskResponse<T>(data) {
        companion object {
            fun <T> T.asData() = Data(this)
        }
    }

    class Exception<out T>(errorMsg: Int? = null) : TaskResponse<T>(errorMsg = errorMsg)
    object NetworkError : TaskResponse<Nothing>()
}

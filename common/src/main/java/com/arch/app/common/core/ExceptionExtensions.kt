package com.arch.app.common.core

import com.arch.app.assets.R
import retrofit2.HttpException
import java.net.HttpURLConnection

/**
 * HttpException extension function, get message via http code.
 * @return Int - String Resource Id.
 */
fun HttpException.getMessageViaCode(): Int = code().getMessageBasedCode()

/**
 * Int extension function, get message via http code.
 * @return Int - String Resource Id.
 */
fun Int.getMessageBasedCode(): Int = when (this) {
    HttpURLConnection.HTTP_UNAVAILABLE -> R.string.general_error_server_maintenance
    else -> R.string.general_error
}

/**
 * Throw exception with
 * @param customMessage String
 */
fun throwDeveloperException(customMessage: String) {
    throw Exception(customMessage)
}

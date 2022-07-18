package com.arch.app.network

import com.arch.app.api.auth.AuthApi
import com.arch.app.models.auth.Auth
import com.google.common.net.HttpHeaders
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request.Builder
import java.util.*

class NetworkAuthorizationProvider(private val credentials: String?, private val authApi: AuthApi) {
    companion object {

        private const val BEARER_AUTH = "Bearer %s"

        /**
         * [Interceptor.Chain] extension function in order to proceed with default headers.
         * @return okhttp3.Response.
         */
        fun Interceptor.Chain.proceedWithDefHeader() =
            proceed(
                request().newBuilder().apply {
                    header(
                        HttpHeaders.ACCEPT_LANGUAGE,
                        Locale.getDefault().language ?: Locale.ENGLISH.language
                    )
                }.build()
            )
    }

    /**
     * Auth.RefreshToken.Request? - from the credentials it can be optional in case the refreshToken was not saved!
     */
    val refreshTokenRequest: Auth.RefreshToken.Request?
        get() = credentials?.let { Auth.RefreshToken.Request(it) }

    /**
     * Returns true if accessToken exists.
     */
    val isAuthorize
        get() = credentials.isNullOrBlank().not()

    /**
     * Builder extension function: add "Authorization" header with accessToken into request.
     */
    fun Builder.authorize() {
        header(HttpHeaders.AUTHORIZATION, String.format(BEARER_AUTH, credentials))
    }

    /**
     * Call refresh token with [authApi] based on [refRequest].
     * @param refRequest - Auth.RefreshToken.Request body.
     * @param logout - in case the refresh will end with Error invoke logout block.
     */
    fun makeRefreshToken(refRequest: Auth.RefreshToken.Request, logout: () -> Unit) = runBlocking {
        executeSafe {
            authApi.refreshToken(refRequest)
        }
    }.data?.updateAuthCredentials() ?: logout()

    /**
     * Updating the credentials with new tokens.
     */
    private fun Auth.RefreshToken.Response.updateAuthCredentials() {
        // TODO: Use Credentials ESP/SP for update tokens.
        /*
        authCredentials.accessToken = accessToken
        authCredentials.refreshToken = refreshToken
        */
    }

    /**
     * Builder extension function: add default headers.
     */
    fun Builder.addDefHeaders() = apply {
        header(HttpHeaders.ACCEPT_LANGUAGE, Locale.getDefault().language ?: Locale.ENGLISH.language)
    }
}

package com.arch.app.network

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.arch.app.common.base.BaseConstants.Broadcasts.LOGOUT_INTENT
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

class AuthInterceptor(
    private val authProvider: NetworkAuthorizationProvider,
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(getAuthorizedRequest(chain)).makeRefreshTokenIfNeed(chain)

    private fun getAuthorizedRequest(chain: Interceptor.Chain): Request =
        chain.request().newBuilder().apply {
            with(authProvider) {
                if (isAuthorize) authorize()
                addDefHeaders()
            }
        }.build()

    private fun Response.makeRefreshTokenIfNeed(chain: Interceptor.Chain): Response =
        takeIf { it.code == HttpURLConnection.HTTP_UNAUTHORIZED }?.run {
            with(authProvider) {
                refreshTokenRequest?.let { refRequest ->
                    makeRefreshToken(refRequest, ::sendLogoutBroadcast)
                    chain.proceed(getAuthorizedRequest(chain))
                } ?: run {
                    sendLogoutBroadcast()
                    this@makeRefreshTokenIfNeed
                }
            }
        } ?: this

    private fun sendLogoutBroadcast() =
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(LOGOUT_INTENT))
}

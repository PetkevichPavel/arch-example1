package com.arch.app.api.auth

import com.arch.app.models.auth.Auth
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("token/refresh/route")
    suspend fun refreshToken(@Body refreshTokenRequest: Auth.RefreshToken.Request): Auth.RefreshToken.Response
}

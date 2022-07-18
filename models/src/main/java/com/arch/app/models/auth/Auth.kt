package com.arch.app.models.auth

import com.squareup.moshi.JsonClass

object Auth {
    @JsonClass(generateAdapter = true)
    data class BaseResponse(
        val accessToken: String,
        val expires: String,
        val refreshToken: String
    )

    object RefreshToken {
        @JsonClass(generateAdapter = true)
        data class Request(
            val refreshToken: String
        )

        @JsonClass(generateAdapter = true)
        data class Response(
            val accessToken: String,
            val expires: String,
            val refreshToken: String
        )
    }
}

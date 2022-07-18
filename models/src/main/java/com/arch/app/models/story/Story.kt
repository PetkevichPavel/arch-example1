package com.arch.app.models.story

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

object Story {
    @JsonClass(generateAdapter = true)
    data class Response(
        val modified: String = "",
        val items: List<Item> = emptyList()
    )

    @JsonClass(generateAdapter = true)
    data class Item(
        val title: String,
        val description: String,
        @Json(name = "author_id")
        val authorId: String,
        val author: String,
        val media: Image,
        val tags: String
    )

    @JsonClass(generateAdapter = true)
    data class Image(
        @Json(name = "m")
        val url: String
    )
}
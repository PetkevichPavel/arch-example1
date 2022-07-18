package com.arch.app.api.story

import com.arch.app.models.story.Story
import retrofit2.http.GET
import retrofit2.http.Query

interface StoryApi {
    @GET("photos_public.gne?format=json&nojsoncallback=1")
    suspend fun getStories(@Query("tags") tags: String?): Story.Response
}
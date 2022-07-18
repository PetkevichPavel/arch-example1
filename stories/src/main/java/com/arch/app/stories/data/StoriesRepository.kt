package com.arch.app.stories.data

import com.arch.app.api.story.StoryApi
import com.arch.app.common.base.BaseRepository
import com.arch.app.common.core.TaskResponse
import com.arch.app.models.story.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IStoriesRepository {
    fun fetchStories(query: String? = null): Flow<TaskResponse<Story.Response>>
}

class StoriesRepository(private val storyApi: StoryApi) : IStoriesRepository, BaseRepository() {
    override fun fetchStories(query: String?): Flow<TaskResponse<Story.Response>> = flow {
        executeSafe { storyApi.getStories(query) }.emitResponse(this@flow)
    }
}
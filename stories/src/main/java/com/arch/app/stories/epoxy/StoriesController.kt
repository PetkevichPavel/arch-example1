package com.arch.app.stories.epoxy

import com.airbnb.epoxy.TypedEpoxyController
import com.arch.app.common.utils.htmlAsHuman
import com.arch.app.models.story.Story
import com.arch.app.stories.storyItem

class StoriesController(private val onClick: (Story.Item) -> Unit) :
    TypedEpoxyController<List<Story.Item>>() {
    override fun buildModels(data: List<Story.Item>?) {
        data?.forEach { story ->
            storyItem {
                id(story.author.plus(story.title).hashCode())
                item(story.copy(description = story.description.htmlAsHuman.orEmpty()))
                click {
                    this@StoriesController.onClick(it)
                }
            }
        }
    }
}

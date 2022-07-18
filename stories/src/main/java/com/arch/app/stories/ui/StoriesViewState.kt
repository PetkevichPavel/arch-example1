package com.arch.app.stories.ui

import com.arch.app.common.base.ViewState
import com.arch.app.common.base.livedata.DefaultValueLiveData

interface StoriesView {
    fun refresh()
    fun openDetail(url: String)
}

class StoriesViewState : ViewState {
    val isNoStories = DefaultValueLiveData(true)
}
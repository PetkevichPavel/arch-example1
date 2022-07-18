package com.arch.app.stories.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arch.app.assets.R
import com.arch.app.common.base.BaseViewModel
import com.arch.app.common.core.ResponseState
import com.arch.app.models.story.Story
import com.arch.app.stories.data.IStoriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.arch.app.stories.ui.StoriesViewState as StoriesVS

class StoriesViewModel(
    override val viewState: StoriesVS,
    private val storiesRepository: IStoriesRepository
) : BaseViewModel<StoriesVS>() {

    private val _state = MutableLiveData<ResponseState>(ResponseState.Init)
    val state: LiveData<ResponseState>
        get() = _state

    private val _data = MutableStateFlow(Story.Response())
    val data: StateFlow<Story.Response>
        get() = _data

    init {
        fetchStories()
    }

    fun fetchStories(query: String? = null) = viewModelScope.launch {
        _state.swipeRefreshLoading {
            storiesRepository.fetchStories(query)
                .emitViaResponse(_data, R.string.general_try_again) { error ->
                    _state.emit(error.asDialogState)
                }
        }
    }
}

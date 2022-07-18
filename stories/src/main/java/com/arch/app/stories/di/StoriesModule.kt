package com.arch.app.stories.di

import com.arch.app.stories.data.IStoriesRepository
import com.arch.app.stories.data.StoriesRepository
import com.arch.app.stories.ui.StoriesViewModel
import com.arch.app.stories.ui.StoriesViewState
import com.arch.app.stories.ui.preview.ImagePreviewViewModel
import com.arch.app.stories.ui.preview.ImagePreviewViewState
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val storiesModules = listOf(baseModule, viewModule)

private val baseModule
    get() = module {
        single<IStoriesRepository> {
            StoriesRepository(get())
        }
    }

private val viewModule
    get() = module {
        viewModel { StoriesViewModel(StoriesViewState(), get()) }

        viewModel { (imageUrl: String) ->
            ImagePreviewViewModel(ImagePreviewViewState(imageUrl))
        }
    }

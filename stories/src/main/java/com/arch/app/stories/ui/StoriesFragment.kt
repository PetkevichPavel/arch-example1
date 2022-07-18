package com.arch.app.stories.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arch.app.common.base.BaseConstants.Numbers.I0
import com.arch.app.common.core.ResponseState
import com.arch.app.common.navigation.Navigation
import com.arch.app.common.utils.debounce
import com.arch.app.models.story.Story
import com.arch.app.stories.R
import com.arch.app.stories.epoxy.StoriesController
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.arch.app.stories.databinding.FragmentStoriesBinding as BD
import com.arch.app.stories.ui.BaseStoriesBindingFragment as StoriesBinding
import com.arch.app.stories.ui.StoriesViewModel as StoriesVM
import com.arch.app.stories.ui.StoriesViewState as StoriesVS


class StoriesFragment :
    StoriesBinding<StoriesVM, StoriesVS, BD>(R.layout.fragment_stories), StoriesView {

    override val viewModel by viewModel<StoriesVM>()
    private val navigation: Navigation by inject()

    private val controller: StoriesController? by lazy {
        StoriesController { openDetail(it.media.url) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.epoxyRecyclerView.setView()
    }

    override fun setObservers() {
        super.setObservers()
        lifecycleScope.launch {
            viewModel.data.drop(I0).collect { it.items.updateData() }
        }
        viewModel.state.observe(viewLifecycleOwner) {
            it.handleState()
        }
    }

    private fun EpoxyRecyclerView.setView() {
        setSearchEditText()
        controller?.let { setController(it) }
        binding.swipeRefreshLayout.apply {
            setColorSchemeResources(com.arch.app.assets.R.color.colorAccent)
            setOnRefreshListener { refresh() }
        }
    }

    private fun List<Story.Item>.updateData() {
        controller?.setData(this@updateData)
    }

    private fun ResponseState.handleState() {
        when (this) {
            is ResponseState.ErrorState -> renderError()
            is ResponseState.LoadingState.SwipeRefresh -> {
                binding.swipeRefreshLayout.isRefreshing =
                    this is ResponseState.LoadingState.SwipeRefresh.Start
            }
            else -> println("for now ignore")
        }
    }

    private fun ResponseState.ErrorState.renderError() {
        when (this) {
            is ResponseState.ErrorState.Dialog -> TODO()
            is ResponseState.ErrorState.Snack -> TODO()
        }
    }

    override fun refresh() {
        viewModel.fetchStories()
    }

    private fun setSearchEditText() {
        viewLifecycleOwner.lifecycleScope.debounce(binding.searchEditText) { query ->
            viewModel.fetchStories(query.orEmpty())
        }
    }


    override fun openDetail(url: String) {
        navigation.navigateTo(this, StoriesFragmentDirections.toPreview(url))
    }
}
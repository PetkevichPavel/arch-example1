package com.arch.app.stories.ui

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.arch.app.common.base.BaseBindingFragment
import com.arch.app.common.base.BaseViewModel
import com.arch.app.common.base.ViewState
import com.arch.app.stories.BR


abstract class BaseStoriesBindingFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding>(
    @LayoutRes private val contentLayoutId: Int
) : BaseBindingFragment<VM, VS, B>(contentLayoutId) {

    override val brViewVariableId: Int = BR.view
    override val brViewModelVariableId: Int = BR.viewModel
    override val brViewStateVariableId: Int = BR.viewState
}


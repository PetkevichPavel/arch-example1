package com.arch.app.base

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.arch.app.common.base.BaseBindingActivity
import com.arch.app.common.base.BaseViewModel
import com.arch.app.common.base.ViewState
import com.arch.app.BR

abstract class BaseAppBindingActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding>(
    @LayoutRes private val contentLayoutId: Int
) : BaseBindingActivity<VM, VS, B>(contentLayoutId) {

    override val brViewModelVariableId: Int = BR.viewModel
    override val brViewStateVariableId: Int = BR.viewState
    override val brViewVariableId: Int = BR.view
}

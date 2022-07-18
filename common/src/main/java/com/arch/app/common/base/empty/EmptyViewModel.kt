package com.arch.app.common.base.empty

import com.arch.app.common.base.BaseViewModel

class EmptyViewModel(override val viewState: EmptyViewState = EmptyViewState()) :
    BaseViewModel<EmptyViewState>()
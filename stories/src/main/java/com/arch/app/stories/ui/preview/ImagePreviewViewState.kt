package com.arch.app.stories.ui.preview

import com.arch.app.common.base.ViewState

interface ImagePreviewView {
    fun close()
}

class ImagePreviewViewState(val imageUrl: String) : ViewState
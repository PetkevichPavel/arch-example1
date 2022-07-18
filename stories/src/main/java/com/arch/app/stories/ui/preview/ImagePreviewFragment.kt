package com.arch.app.stories.ui.preview

import androidx.navigation.fragment.findNavController
import com.arch.app.stories.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import com.arch.app.stories.databinding.FragmentImagePreviewBinding as BD
import com.arch.app.stories.ui.BaseStoriesBindingFragment as StoriesBinding
import com.arch.app.stories.ui.preview.ImagePreviewViewModel as ImgPreviewVM
import com.arch.app.stories.ui.preview.ImagePreviewViewState as ImagePreviewVS

class ImagePreviewFragment :
    StoriesBinding<ImgPreviewVM, ImagePreviewVS, BD>(R.layout.fragment_image_preview),
    ImagePreviewView {
    override val viewModel by viewModel<ImgPreviewVM>(
        parameters = {
            arguments?.let {
                parametersOf(ImagePreviewFragmentArgs.fromBundle(it).imageUrl)
            } ?: parametersOf()
        }
    )

    override fun close() {
        findNavController().popBackStack()
    }
}
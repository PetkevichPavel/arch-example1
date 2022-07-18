package com.arch.app.common.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.arch.app.common.utils.contentView
import com.arch.app.common.utils.hideKeyboard
import com.arch.app.common.utils.registerOnBackPressedListener

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {
    val parentActivity get() = activity as? AppCompatActivity
    private var onBackPressedCallback: OnBackPressedCallback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        setBaseListeners()
    }

    private fun setBaseListeners() {
        parentActivity?.apply {
            contentView?.setOnClickListener {
                hideKeyboard()
            }
        }
    }

    open fun setObservers() {}

    fun handleBackPress(fragment: Fragment.() -> Unit) {
        onBackPressedCallback = registerOnBackPressedListener {
            fragment(this)
        }
    }

    override fun onPause() {
        parentActivity?.hideKeyboard()
        super.onPause()
    }

    override fun onDestroyView() {
        onBackPressedCallback?.remove()
        super.onDestroyView()
    }

    override fun onDestroy() {
        onBackPressedCallback?.remove()
        super.onDestroy()
    }
}

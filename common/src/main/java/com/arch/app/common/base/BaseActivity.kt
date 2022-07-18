package com.arch.app.common.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arch.app.common.utils.contentView
import com.arch.app.common.utils.hideKeyboard

abstract class BaseActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        setBaseListeners()
    }

    private fun setBaseListeners() {
        contentView?.setOnClickListener {
            hideKeyboard()
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val view = super.onCreateView(name, context, attrs)
        setObservers()
        return view
    }

    open fun setObservers() {}

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
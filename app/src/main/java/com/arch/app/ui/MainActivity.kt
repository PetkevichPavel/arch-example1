package com.arch.app.ui

import android.os.Bundle
import com.arch.app.R
import com.arch.app.common.base.empty.EmptyView
import com.arch.app.base.BaseAppBindingActivity as Binding
import com.arch.app.common.base.empty.EmptyViewModel as VM
import com.arch.app.common.base.empty.EmptyViewState as VS
import com.arch.app.databinding.ActivityMainBinding as BD

class MainActivity : Binding<VM, VS, BD>(R.layout.activity_main), EmptyView {

    override val viewModel: VM
        get() = VM()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
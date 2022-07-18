package com.arch.app.ui

import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.arch.app.R
import com.arch.app.base.BaseConstants.Duration
import com.arch.app.common.base.BaseActivity
import com.arch.app.common.navigation.Navigation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class RouterActivity : BaseActivity() {

    private val navigation: Navigation by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.addFlags(FLAG_LAYOUT_NO_LIMITS)
        installSplashScreen()
        setContentView(R.layout.activity_router)

        lifecycleScope.launch {
            delay(Duration.SEC)
            navigation.finishSplash(this@RouterActivity)
        }
    }
}
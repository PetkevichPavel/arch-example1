package com.arch.app.common.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

/**
 * Execute f only if the current Android SDK version is version or newer. Do nothing otherwise.
 */
@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
inline fun doFromSdk(version: Int, f: () -> Unit) {
    if (Build.VERSION.SDK_INT >= version) f()
}

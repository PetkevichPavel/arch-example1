package com.arch.app.common.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import com.arch.app.common.base.BaseConstants.Numbers.I0


inline fun <reified T : View> Activity.findOptional(@IdRes id: Int): T? = findViewById(id) as? T

/**
 * Trying to take a content view.
 */
inline val Activity.contentView: View?
    get() = findOptional<ViewGroup>(android.R.id.content)?.getChildAt(0)

/**
 * Activity extension function for safely of hiding the soft key board.s
 * @return Boolean - in case of hided true, and false if the keyboard wasn't open.
 */
fun Activity.hideKeyboard() = contentView?.windowToken?.let { wToken ->
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(wToken, I0)
}
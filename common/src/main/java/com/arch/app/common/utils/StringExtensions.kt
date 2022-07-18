package com.arch.app.common.utils

import android.text.Html
import com.arch.app.common.base.BaseConstants.Strings


/**
 * String extension function to removing [Strings.ENTER] or returns null if the String is null.
 * @return String or NULL.
 */
fun String?.removeEnters() = this?.replace(Strings.ENTER, Strings.EMPTY)

/**
 * String extension function to removing [Strings.TAB] or returns null if the String is null.
 * @return String or NULL.
 */
fun String?.removeTabs() = this?.replace(Strings.TAB, Strings.EMPTY)

/**
 * String Extension Val in order to transform html into human readable string.
 * Returns string or null.
 */
val String.htmlAsHuman: String?
    get() = Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString().removeTabs().removeEnters()
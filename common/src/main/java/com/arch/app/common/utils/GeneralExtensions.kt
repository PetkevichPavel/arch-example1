package com.arch.app.common.utils

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * Executes given block and returns it's return value of null on case of some exception.
 */
fun <T> safe(block: () -> T): T? =
    try {
        block.invoke()
    } catch (e: Exception) {
        Timber.i(e)
        null
    }

/**
 * T generic extension function, which returns T as returned value or returns it in callback [block].
 * @param condition - condition for the if, under which method will returns null & no triggering [block] callback.
 * @param block - returns the T as it is only in case the condition is true.
 * @return T - returning always T if it satisfies the given [condition] otherwise null.
 */
inline fun <T> T.applyIf(condition: Boolean, block: T.() -> Unit) =
    this.takeIf { condition }?.apply { block.invoke(this@applyIf) } ?: this

/**
 * Generic null-check
 * @return boolean value, true if object is null
 */
fun <T> T?.isNull() = this == null

/**
 * Generic null-check
 * @return boolean value, true if object is not null
 */
fun <T> T?.isNotNull() = this != null


/**
 * CoroutineScope extension function, for denouncing.
 * @param textInEd - Input edit text field.
 * @param delayTime - debounce timeOut.
 * @param backBtnAllowed - by default is set on false, in case you want to trigger [magic] on delete button set it on true.
 * @param magic - lambda function for triggering after debounce.
 */
fun CoroutineScope.debounce(
    textInEd: EditText,
    delayTime: Long = 400,
    backBtnAllowed: Boolean = false,
    magic: (String?) -> Unit
) {
    var isBackBtnClicked: Boolean = !backBtnAllowed
    var searchFor = ""
    textInEd.doOnTextChanged { text, _, before, count ->
        text?.takeIf { it.isNotBlank() }?.apply {
            toString().trim().let { searchText ->
                if (!backBtnAllowed) {
                    if (before > count) isBackBtnClicked = true
                    else toList().apply {
                        isBackBtnClicked = get(lastIndex).toString().isBlank()
                    }
                }

                if (searchText == searchFor)
                    return@doOnTextChanged

                searchFor = searchText

                launch {
                    delay(delayTime)
                    if (searchText != searchFor)
                        return@launch
                    if (!isBackBtnClicked) magic.invoke(searchText)
                }
            }
        }
    }
}
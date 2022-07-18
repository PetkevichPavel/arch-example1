package com.arch.app.common.utils

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController

/**
 * Gets result under the key and removes it from savedStateHandle
 * @param key Key under which result is obtained
 * @param callback callback in which result is emitted from savedStateHandle
 */
fun <VALUE> Fragment.getResultOnce(key: String, callback: ((VALUE) -> Unit?)? = null): Unit? {
    val navBackStackEntry = findNavController().currentBackStackEntry ?: return null // ?.takeIf {  }

    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains(key)) {
            val result = navBackStackEntry.savedStateHandle.get<VALUE>(key)
            if (result != null) {
                callback?.invoke(result)
                navBackStackEntry.savedStateHandle.remove<VALUE>(key)
            }
        }
    }
    navBackStackEntry.lifecycle.addObserver(observer)

    viewLifecycleOwner.lifecycle.addObserver(
        LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        }
    )
    return Unit
}

/**
 * Sets result for previousBackStackEntry in navigation
 * @param key Key under which result is obtained with getResultOnce()
 * @param result parcelable value set to the savedStateHandle as result
 */
fun <VALUE> Fragment.setResult(key: String, result: VALUE) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

val Fragment.navController
    get() = safe {
        findNavController()
    }

/**
 * Fragment extension function on registering onBack pressed callback.
 * @param [onBackPressed] - lambda function for overriding callback in calling place.
 * @return [OnBackPressedCallback] - on back pressed callback.
 */
fun Fragment.registerOnBackPressedListener(onBackPressed: () -> Unit): OnBackPressedCallback =
    object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() = onBackPressed.invoke()
    }.apply {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, this)
    }

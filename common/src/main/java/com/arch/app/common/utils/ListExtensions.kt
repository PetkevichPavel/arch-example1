package com.arch.app.common.utils

/**
 * MutableList extension function for getting size of the list based with the specific [value].
 * @return size in Int.
 */
fun <T> MutableList<T>.getSizeWith(value: String) = filter { it.toString().contains(value) }.size

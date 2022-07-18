package com.arch.app.common.base.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Extended [MutableLiveData] class with default value. Default value can't be null.
 */
class DefaultValueLiveData<T>(defaultValue: T & Any) : MutableLiveData<T>() {

    init {
        value = defaultValue
    }

    override fun getValue(): T = super.getValue() ?: throw NullPointerException("Value is null")
}

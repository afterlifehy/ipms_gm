package com.rt.base.base.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.ref.WeakReference

/**
 * Created by zj on 2020/11/20.
 */
class SafeMutableLiveData<T> : MutableLiveData<T>() {
    private var lastLifecycleOwner: WeakReference<LifecycleOwner>? = null

    fun safeObserve(owner: LifecycleOwner, observer: Observer<T>) {
        lastLifecycleOwner?.get()?.let {
            removeObservers(it)
        }
        lastLifecycleOwner = WeakReference(owner)
        observe(owner, observer)
    }
}
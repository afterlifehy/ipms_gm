package com.rt.base.viewbase

import android.content.Context
import android.view.View

interface BaseViewAddManagers {

    fun getRootView(context: Context, mView: View): View

    fun getRootViewId(context: Context, contextId: Int): View

}
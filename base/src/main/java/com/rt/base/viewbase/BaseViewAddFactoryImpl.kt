package com.rt.base.viewbase

import android.content.Context
import android.view.View
import com.rt.base.R

class BaseViewAddFactoryImpl : BaseViewAddFactory {

    override fun getRootView(context: Context): View {
        val mRootView = View.inflate(context, R.layout.base_no_title_layout, null)
        return mRootView
    }
}
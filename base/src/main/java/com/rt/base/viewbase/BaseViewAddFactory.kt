package com.rt.base.viewbase

import android.content.Context
import android.view.View

interface BaseViewAddFactory {
    companion object {
        private val mBaseViewAddManager = BaseViewAddFactoryImpl()
        fun getInstance(): BaseViewAddFactory {
            return mBaseViewAddManager
        }
    }

    /**
     * 获取一个带加载效果，网络异常 暂无数据框
     */
    fun getRootView(context: Context): View

}
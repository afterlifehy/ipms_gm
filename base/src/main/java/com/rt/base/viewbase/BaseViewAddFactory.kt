package com.rt.base.viewbase

import android.content.Context
import android.view.View

interface BaseViewAddFactory {
    companion object {
        private val mBaseViewAddManager = BaseViewAddFactoryImpl()
        fun getInsten(): BaseViewAddFactory {
            return mBaseViewAddManager
        }
    }

    /**
     * 获取一个带加载效果，网络异常 暂无数据框
     */
    fun getRoootView(context: Context): View

    /**
     * 添加一个暂无数据框
     */
    fun getNotDataView(context: Context): View


    /**
     * 添加一个网络错误时的加载框
     */
    fun getNewWorkErrorView(context: Context): View

    /**
     * 添加一个加载效果框
     */
    fun getLoadProgressView(context: Context): View

}
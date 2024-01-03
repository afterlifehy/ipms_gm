package com.rt.base.http

import okhttp3.Interceptor

/**
 * Created by zj on 2021/2/25.
 */
interface OnAddOkhttpInterceptor {
    fun onAddOkHttpInterceptor():List<Interceptor>
}
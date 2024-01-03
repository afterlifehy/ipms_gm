package com.rt.base.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())

        //   if (response.code() == 401) {

//            val url = chain.request().url().toString()
//            if(url.endsWith(Api.REFRESH_TOKEN)){
//                AppCenter.setAuthorization(null)
//                throw TokenOutException()
//            }
//
//            Log.d(Constant.PROJECT_NAME,"token过期")
//            //如果认证过期 去请求刷新接口
//            //同步刷新接口
//            synchronized(AppCenter.instance){
//                val api = RetrofitUtils.getInstance().build()
//                val token = AppCenter.getAuthorization()
//                val call = api.refreshToken(token!!)
//                //请求结果 同步得到结果
//                Log.d(Constant.PROJECT_NAME,"同步刷新token")
//
//                val rRes = call.execute()
//                //保存token
//                Log.d(Constant.PROJECT_NAME,"保存刷新的token")
//
//                AppCenter.setAuthorization(rRes.body()?.result!!)
//                Log.d(Constant.PROJECT_NAME,"用新token重新发起请求")
//
//                val request = chain.request()
//                val newRequest=  request.newBuilder().removeHeader(Constant.TICKETCODE)
//                    .addHeader(Constant.TICKETCODE, rRes.body()?.result?.ticketCode).build()
        // return chain.proceed(response)
        //  }
//
        //   }

        return response
    }

}
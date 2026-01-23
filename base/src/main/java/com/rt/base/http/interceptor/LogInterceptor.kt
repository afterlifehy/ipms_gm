package com.rt.base.http.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LogInterceptor implements Interceptor {
    private boolean isDebug;
    //可以从连几次

    public LogInterceptor(boolean isDebug) {
        this.isDebug = isDebug;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (isDebug) {
            Log.i("HttpRequest:", "okhttp3:" + request.toString());//输出请求前整个url
            //去执行网络请求

        }
        Response response = chain.proceed(request);

        if (isDebug) {
            okhttp3.MediaType mediaType = response.body().contentType();

            String content = response.body().string();
            if (isDebug) {
//                String[] url = response.request().url().url().toString().split("/");
//                String metoh = url[url.length - 1];
//                if (metoh.contains("?")) {
//                    metoh = metoh.split("?")[0];
//                }
//                Log.i("keey", "url:" + metoh);
//                Log.i("metoh:", "request:" + request.toString() + "==" + "response body:" + content);//输出返回信息

                Log.i("HttpResponse:", "request:" + request.toString() + "==" + "response body:" + content);//输出返回信息
            }
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
        return response;

    }
}

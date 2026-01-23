package com.rt.base.http.interceptor;


import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okhttp 重试拦截器
 */
public final class RetryInterceptor implements Interceptor {
    private volatile int maxRetries = 2;
    private volatile long retryInterval = 2000L;

    @Override
    public Response intercept(Chain chain) throws IOException {
        int retryNum = 0;
        Request request = chain.request();
        Response response = chain.proceed(request);
        Log.i("Retry", "num:" + retryNum);
        while (!response.isSuccessful() && retryNum < maxRetries) {
            response.close();
            retryNum++;
            Log.i("Retry", "num:" + retryNum);
            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            response = chain.proceed(request);
        }
        return response;
    }
}

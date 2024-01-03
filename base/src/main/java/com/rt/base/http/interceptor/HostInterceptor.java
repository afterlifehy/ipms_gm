package com.rt.base.http.interceptor;


import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * okhttp 拦截器 实现动态更改HTTP host
 */
public final class HostInterceptor implements Interceptor {
    private volatile String host;
    private volatile int port = -1;
    private volatile String scheme;

    public void setHost(String scheme, String host, int port) {
        this.host = host;
        this.port = port;
        this.scheme = scheme;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String host = this.host;
        int port = this.port;
        String scheme = this.scheme;
        if (host != null && scheme != null && port != -1) {
            HttpUrl newUrl = request.url().newBuilder().scheme(scheme).host(host).port(port).build();
            request = request.newBuilder().url(newUrl).build();
        }
        return chain.proceed(request);
    }
}

package com.rt.base.http;

import com.rt.base.util.Constant;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RetrofitUtils {

    private static OkHttpClient mOkHttpClient;
    private static OkHttpClient mCoroutineOkHttpClient;

    private RetrofitUtils() {
    }

    public static class Hide {
        static RetrofitUtils sRetrofitUtil = new RetrofitUtils();
    }

    public static RetrofitUtils getInstance() {
        return Hide.sRetrofitUtil;
    }


    /**
     * 生成一个
     *
     * @param serviceCls
     * @param baseUrl
     * @param <T>
     * @return
     */
    public <T> T createCoroutineRetrofit(Class<T> serviceCls, String baseUrl) {
        initCoroutineOkhttpClient();
        Retrofit mRetrofit = HttpBuilder.getRetrofit(HttpBuilder.getCoroutineRetrofitBuilder(HttpBuilder.getGson(), mCoroutineOkHttpClient, baseUrl));
        return mRetrofit.create(serviceCls);
    }

    /**
     * 初始化Okhttp
     */
    private void initOkhttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = getOkHttpClient();
        }
    }

    private void initCoroutineOkhttpClient() {
        if (mCoroutineOkHttpClient == null) {
            mCoroutineOkHttpClient = getOkHttpClient();
        }
    }

    private OkHttpClient getOkHttpClient() {
        return HttpBuilder.getOkhttpClient(HttpBuilder.getOkhttpBuilder(HttpBuilder.getAllListInterceptor()));
    }

    //获取这个SSLSocketFactory
    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpsUtils.SSLParams getSSLParams() {
        HttpsUtils.SSLParams sslParams = null;
        try {
            InputStream byteArrayInputStream = new ByteArrayInputStream(Constant.crtStr.getBytes("UTF-8"));
            sslParams = HttpsUtils.getSslSocketFactory(byteArrayInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslParams;
    }

    //获取TrustManager
    private static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {


            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};
        return trustAllCerts;
    }

    //获取HostnameVerifier
    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };

        return hostnameVerifier;
    }
}

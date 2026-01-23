package com.rt.ipms_mg

import android.app.Application
import android.content.Context
import android.net.http.HttpResponseCache
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.rt.base.BaseApplication
import com.rt.base.http.interceptor.*
import com.rt.base.network.NetWorkMonitorManager
import com.rt.common.help.SmartRefreshHelp
import com.rt.ipms_mg.startup.OnAppBaseProxyManager
import io.realm.Realm
import okhttp3.Interceptor
import java.io.File

class AppApplication : BaseApplication() {
    companion object {
        var _context: BaseApplication? = null
        fun instance(): BaseApplication {
            return _context!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        _context = this
        //realm
        Thread {
            Realm.init(this)
            val cacheDir = File(BaseApplication.instance().cacheDir, "http")
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128)
            BaseApplication.instance().setOnAppBaseProxyLinsener(OnAppBaseProxyManager())
            //初始化全局的刷新
            SmartRefreshHelp.initRefHead()
            //初始化网络状态监听
//            regNetWorkState(this)
        }.start()
        UMConfigure.init(this, "658bdedda7208a5af191151b", "android", UMConfigure.DEVICE_TYPE_PHONE, null)
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_MANUAL)
        UMConfigure.setProcessEvent(true)
    }


    /**
     * 注册全局的网络状态广播
     */
    private fun regNetWorkState(application: Application) {
        NetWorkMonitorManager.getInstance().init(application)
    }

    override fun onAddOkHttpInterceptor(): List<Interceptor> {
        val list = ArrayList<Interceptor>()
//        list.add(HeaderInterceptor())
//        list.add(LoginExpiredInterceptor())
//        list.add(HostInterceptor())
//        list.add(TokenInterceptor())
        list.add(ExceptionInterceptor())
        list.add(RetryInterceptor())
        if (BuildConfig.is_debug) {
            list.add(LogInterceptor(BuildConfig.is_debug))
//            val mHttpLoggingInterceptor = HttpLoggingInterceptor("ipms_mg_http")
//            mHttpLoggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
//            list.add(mHttpLoggingInterceptor)
        }
        return list
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

}
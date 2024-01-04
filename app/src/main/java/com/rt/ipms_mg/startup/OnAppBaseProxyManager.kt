package com.rt.ipms_mg.startup

import com.rt.base.proxy.OnAppBaseProxyLinsener
import com.rt.ipms_mg.BuildConfig

class OnAppBaseProxyManager : OnAppBaseProxyLinsener {
    override fun onIsProxy(): Boolean {
        return BuildConfig.is_proxy
    }

    override fun onIsDebug(): Boolean {
        return BuildConfig.is_debug
    }

}
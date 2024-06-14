package com.rt.base.base.mvvm
import com.rt.base.BuildConfig

object UrlManager {
    const val DEV_HOST = "http://114.94.20.110/ipms/service/"
    const val FORMAL_HOST = "http://ipms.csnits.com/ipms/service/"

    fun getServerUrl(): String {
        if (BuildConfig.is_dev) {
            return DEV_HOST
        } else {
            return FORMAL_HOST
        }
    }
}
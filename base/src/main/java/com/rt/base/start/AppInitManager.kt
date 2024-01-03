package com.rt.base.start

import android.app.Application

abstract class AppInitManager {
    //需要在application里面初始化的
    abstract fun applicationInit(application: Application)

    /**
     * 可以延时加载的
     */
    abstract fun delayInit(application: Application)
}
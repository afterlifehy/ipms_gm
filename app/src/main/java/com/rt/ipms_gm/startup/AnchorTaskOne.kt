package com.rt.ipms_gm.startup

import com.rt.base.start.BaseStartUpManager
import com.rt.base.start.StartUpKey
import com.rt.ipms_gm.AppApplication
import com.xj.anchortask.library.AnchorTask

class AnchorTaskOne : AnchorTask(StartUpKey.TASK_NAME_TWO) {
    override fun isRunOnMainThread(): Boolean {
        return false
    }

    override fun run() {
        BaseStartUpManager.instance().delayInit(AppApplication.instance())
        AppStartUpManager.instance().delayInit(AppApplication.instance())
    }
}
package com.rt.ipms_gm.startup

import com.rt.base.start.BaseStartUpManager
import com.rt.base.start.StartUpKey
import com.rt.ipms_gm.AppApplication
import com.xj.anchortask.library.AnchorTask

class AnchorTaskZero : AnchorTask(StartUpKey.TASK_NAME_ONE) {
    override fun isRunOnMainThread(): Boolean {
        return false
    }

    override fun run() {
        BaseStartUpManager.instance().applicationInit(AppApplication.instance())
        AppStartUpManager.instance().applicationInit(AppApplication.instance())
    }
}
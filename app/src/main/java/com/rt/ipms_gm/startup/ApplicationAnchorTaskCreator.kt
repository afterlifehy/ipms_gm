package com.rt.ipms_gm.startup

import com.rt.base.start.StartUpKey
import com.xj.anchortask.library.AnchorTask
import com.xj.anchortask.library.IAnchorTaskCreator

class ApplicationAnchorTaskCreator : IAnchorTaskCreator {
    override fun createTask(taskName: String): AnchorTask? {
        when (taskName) {
            StartUpKey.TASK_NAME_ONE -> {
                return AnchorTaskZero()
            }
            StartUpKey.TASK_NAME_TWO -> {
                return AnchorTaskOne()

            }
        }
        return null
    }
}
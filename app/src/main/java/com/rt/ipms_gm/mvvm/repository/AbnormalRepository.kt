package com.rt.ipms_gm.mvvm.repository

import com.rt.base.base.mvvm.BaseRepository
import com.rt.base.bean.HttpWrapper

class AbnormalRepository : BaseRepository() {
    /**
     * 异常上报
     */
    suspend fun abnormalReport(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<Any> {
        return mServer.abnormalReport(param)
    }
}
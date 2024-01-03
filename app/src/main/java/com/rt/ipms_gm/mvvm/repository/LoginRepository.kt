package com.rt.ipms_gm.mvvm.repository

import com.rt.base.base.mvvm.BaseRepository
import com.rt.base.bean.HttpWrapper
import com.rt.base.bean.LoginBean
import com.rt.base.bean.UpdateBean

class LoginRepository : BaseRepository() {

    /**
     * 登录
     */
    suspend fun login(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<LoginBean> {
        return mServer.login(param)
    }

    /**
     * 版本更新查询
     */
    suspend fun checkUpdate(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<UpdateBean> {
        return mServer.checkUpdate(param)
    }

    /**
     * 考勤排班
     */
    suspend fun checkOnWork(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<Any> {
        return mServer.checkOnWork(param)
    }
}
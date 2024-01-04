package com.rt.ipms_mg.mvvm.repository

import com.rt.base.base.mvvm.BaseRepository
import com.rt.base.bean.HttpWrapper
import com.rt.base.bean.OrderNoBean

class AbnormalRepository : BaseRepository() {
    /**
     * 异常上报
     */
    suspend fun abnormalReport(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<Any> {
        return mServer.abnormalReport(param)
    }

    /**
     * 图片上传
     */
    suspend fun picUpload(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<Any> {
        return mServer.picUpload(param)
    }

    /**
     * 根据parkingNo查询orderNo
     */
    suspend fun inquiryOrderNoByParkingNo(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<OrderNoBean> {
        return mServer.inquiryOrderNoByParkingNo(param)
    }
}
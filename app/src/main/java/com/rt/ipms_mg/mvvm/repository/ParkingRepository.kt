package com.rt.ipms_mg.mvvm.repository

import com.rt.base.base.mvvm.BaseRepository
import com.rt.base.bean.DebtUploadBean
import com.rt.base.bean.HttpWrapper
import com.rt.base.bean.ParkingLotResultBean
import com.rt.base.bean.ParkingSpaceBean
import com.rt.base.bean.PlaceOederResultBean
import com.rt.base.bean.TicketPrintBean
import com.rt.base.bean.TicketPrintResultBean
class ParkingRepository : BaseRepository() {

    /**
     * 停车场泊位列表
     */
    suspend fun getParkingLotList(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<ParkingLotResultBean> {
        return mServer.getParkingLotList(param)
    }

    /**
     * 场内停车费查询
     */
    suspend fun parkingSpace(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<ParkingSpaceBean> {
        return mServer.parkingSpace(param)
    }

    /**
     * 下单
     */
    suspend fun placeOrder(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<PlaceOederResultBean> {
        return mServer.placeOrder(param)
    }

    /**
     * 结单
     */
    suspend fun endOrder(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<Any> {
        return mServer.endOrder(param)
    }

    /**
     * 图片上传
     */
    suspend fun picUpload(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<Any> {
        return mServer.picUpload(param)
    }

    /**
     *  场内支付
     */
    suspend fun ticketPrint(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<TicketPrintBean> {
        return mServer.ticketPrint(param)
    }

    /**
     *  根据订单查交易
     */
    suspend fun inquiryTransactionByOrderNo(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<TicketPrintResultBean> {
        return mServer.inquiryTransactionByOrderNo(param)
    }
}
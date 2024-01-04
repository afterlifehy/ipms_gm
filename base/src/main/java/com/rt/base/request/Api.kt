package com.rt.base.request

import com.rt.base.bean.*
import retrofit2.http.*


interface Api {
    /**
     * 签到
     */
    @POST("S_MG_01")
    suspend fun login(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<LoginBean>

    /**
     * 停车场泊位列表
     */
    @POST("S_VO2_02")
    suspend fun getParkingLotList(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<ParkingLotResultBean>

    /**
     * 下单
     */
    @POST("S_MG_02")
    suspend fun placeOrder(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<PlaceOederResultBean>

    /**
     * 结单
     */
    @POST("S_MG_03")
    suspend fun endOrder(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<Any>

    /**
     * 预支付数据查询
     */
    @POST("S_MG_04")
    suspend fun prePayFeeInquiry(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<PayQRBean>

    /**
     * 支付结果查询
     */
    @POST("S_MG_05")
    suspend fun payResultInquiry(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<PayResultBean>

    /**
     * 欠费上传
     */
    @POST("S_MG_06")
    suspend fun debtUpload(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<DebtUploadBean>

    /**
     * 签退
     */
    @POST("S_MG_07")
    suspend fun logout(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<Any>

    /**
     * 图片上传
     */
    @POST("S_MG_08")
    suspend fun picUpload(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<Any>

    /**
     * 异常上报
     */
    @POST("S_MG_10")
    suspend fun abnormalReport(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<Any>

    /**
     * 订单查询
     */
    @POST("S_MG_11")
    suspend fun orderInquiry(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<OrderResultBean>

    /**
     * 交易查询
     */
    @POST("S_MG_12")
    suspend fun transactionInquiry(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<TransactionResultBean>

    /**
     * 营收盘点
     */
    @POST("S_MG_13")
    suspend fun incomeCounting(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<IncomeCountingBean>

    /**
     * 费率查询
     */
    @POST("S_MG_14")
    suspend fun feeRate(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<FeeRateResultBean>

    /**
     * 版本查询
     */
    @POST("S_MG_15")
    suspend fun checkUpdate(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<UpdateBean>

    /**
     * 泊位订单查询
     */
    @POST("S_MG_16")
    suspend fun parkingSpace(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<ParkingSpaceBean>

    /**
     * 欠费查询
     */
    @POST("S_MG_17")
    suspend fun debtInquiry(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<DebtCollectionResultBean>

    /**
     * 泊位订单查询
     */
    @POST("S_MG_18")
    suspend fun picInquiry(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<PicInquiryBean>

    /**
     * 票据查询
     */
    @POST("S_MG_19")
    suspend fun ticketPrint(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<TicketPrintBean>

    /**
     * 离场订单查询
     */
    @POST("S_MG_20")
    suspend fun endOrderInfo(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<EndOrderInfoBean>

    /**
     * 考勤排班
     */
    @POST("S_VO2_20")
    suspend fun checkOnWork(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<Any>

    /**
     * 根据orderNo查交易
     */
    @POST("S_MG_21")
    suspend fun inquiryTransactionByOrderNo(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<TicketPrintResultBean>

    /**
     * 根据泊位号查询orderNo
     */
    @POST("S_G0_22")
    suspend fun inquiryOrderNoByParkingNo(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<OrderNoBean>

    /**
     * 离场支付二维码
     */
    @POST("S_OR_0112")
    suspend fun endOrderQR(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<PayQRBean>

    /**
     * 追缴二维码
     */
    @POST("S_OR4_0113")
    suspend fun debtPayQr(@Body param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<PayQRBean>
}
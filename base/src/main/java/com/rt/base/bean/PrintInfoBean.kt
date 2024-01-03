package com.rt.base.bean

class PrintInfoBean(
    var roadId: String,
    var plateId: String,
    var payMoney: String,
    var orderId: String,
    var phone: String,
    var startTime: String,
    var leftTime: String,
    var remark: String,
    var company: String,
    var oweCount: Int
) {
    override fun toString(): String = "$orderId,$plateId,$roadId,$startTime,$leftTime,$payMoney,$oweCount,$phone,$remark,$company"
}
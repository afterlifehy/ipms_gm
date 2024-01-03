package com.rt.base.bean

data class ParkingSpaceBean(
    var carLicense: String = "",
    var havePayMoney: String = "",
    var historyCount: Int = 0,
    var historySum: Int = 0,
    var orderNo: String = "",
    var parkingNo: String = "",
    var realtimeMoney: String = "",
    var startTime: String = "",
    var streetNo: String = "",
    var timeOut: String = ""
)
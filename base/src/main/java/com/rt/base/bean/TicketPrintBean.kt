package com.rt.base.bean

data class TicketPrintBean(
    var businessCname: String = "",
    var carLicense: String = "",
    var endTime: String = "",
    var oweCount: Int = 0,
    var payMoney: String = "",
    var phone: String = "",
    var remark: String = "",
    var roadName: String = "",
    var startTime: String = "",
    var tradeNo: String = ""
)

data class TicketPrintResultBean(
    var result: ArrayList<TicketPrintBean>
)
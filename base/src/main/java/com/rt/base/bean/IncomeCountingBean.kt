package com.rt.base.bean

data class IncomeCountingBean(
    var loginName: String = "",
    var range: String = "",
    var payMoney:String = "",
    var qrMoney:String = "",
    var qrCount:String = "",
    var cashMoney:String = "",
    var cashCount:String = "",
    var refundMoney:String = "",
    var refundCount:String = "",
)

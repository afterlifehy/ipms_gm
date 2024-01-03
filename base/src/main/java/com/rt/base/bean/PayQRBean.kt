package com.rt.base.bean

data class PayQRBean(
    var totalAmount: Double = 0.0,
    var qrCode: String = "",
    var tradeNo: String = "",
    var qr_code: String = ""
)
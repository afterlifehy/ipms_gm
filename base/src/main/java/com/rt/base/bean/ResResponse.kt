package com.rt.base.bean

data class ResResponse<out T>(val code: Int, val msg: String, val data: T)
package com.rt.common.util

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.TimeUtils
import com.rt.base.BaseApplication
import com.rt.base.bean.IncomeCountingBean
import com.rt.base.bean.PrintInfoBean
import com.rt.base.ext.i18n
import com.rt.base.help.ActivityCacheManager
import com.rt.base.util.ToastUtil
import org.json.JSONException
import zpCPCLSDK.zpCPCLSDK.PrinterInterface
import zpCPCLSDK.zpCPCLSDK.zp_cpcl_BluetoothPrinter
import java.util.Calendar

class BluePrint() {
    var zpSDK: zp_cpcl_BluetoothPrinter? = null
    private var printResult = 0
    var mAddress: String? = null

    companion object {
        private var bluePrint: BluePrint? = null

        @get:Synchronized
        val instance: BluePrint?
            get() {
                if (bluePrint == null) bluePrint = BluePrint()
                return bluePrint
            }
        val instanceBlock: BluePrint?
            get() {
                if (bluePrint == null) synchronized(BluePrint::class.java) {
                    if (bluePrint == null) bluePrint = BluePrint()
                }
                return bluePrint
            }
    }

    @Throws(JSONException::class)
    fun zkblueprint(content: String) {
        //打印文本
        try {
            printResult = Print1(content)
        } catch (e: Exception) {
            Handler(Looper.getMainLooper()).post {
                ToastUtil.showMiddleToast("打印机状态异常")
            }
        }
        ActivityCacheManager.instance().getCurrentActivity()!!.runOnUiThread {
            if (printResult == 0) {
//                val toast = Toast.makeText(BaseApplication.instance(), "打印完成", Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.CENTER, 0, 0)
//                toast.show()
            } else if (printResult == -1) {
                ToastUtil.showMiddleToast("蓝牙未连接")
            } else if (printResult == -2) {
                ToastUtil.showMiddleToast("路段名称过长...")
            } else {
                ToastUtil.showMiddleToast("打印失败")
            }
        }
    }

    fun connet(address: String): Int {
        //获取设备蓝牙地址
        mAddress = address
        zpSDK = zp_cpcl_BluetoothPrinter(BaseApplication.instance())
        Handler(Looper.getMainLooper()).post {
            ToastUtil.showMiddleToast("打印机开始连接")
        }

        if (!zpSDK!!.connect(mAddress)) {
            Handler(Looper.getMainLooper()).post {
                ToastUtil.showMiddleToast("打印机连接失败")
            }
            printResult = -1
            return printResult
        }
        Handler(Looper.getMainLooper()).post {
            ToastUtil.showMiddleToast("打印机连接成功")
        }
        return 0
    }

    fun disConnect() {
        if (zpSDK != null) {
            zpSDK!!.disconnect()
        }
    }

    val blueToothDevice: MutableList<BluetoothDevice>
        @SuppressLint("MissingPermission")
        get() {
            val mAdapter = BluetoothAdapter.getDefaultAdapter()
            val blueToothDeviceList = mAdapter.bondedDevices.toMutableList()
            var deviceList: MutableList<BluetoothDevice> = ArrayList()
            for (i in blueToothDeviceList) {
                if (i.name.startsWith("CC")) {
                    deviceList.add(i)
                }
            }
            return deviceList
        }

    /**
     * 打印
     *
     * @param printText 打印内容
     * @return
     */
    fun Print1(printText: String?): Int {
        var printText = printText
        var yLocation = 182
        if (printText !== "" && printText != null) {
            val receipt = printText.contains("receipt")
            if (receipt) {
                printText = printText.substring(8)
            }
            var ystart = 10 + 60 + 40
            if (zpSDK == null) {
                return -1
            }
            if (receipt) {
                val incomeCountingBean = JSONObject.parseObject(printText, IncomeCountingBean::class.java)
                var height = 600
                if (incomeCountingBean.list2 != null && incomeCountingBean.list2.size > 0) {
                    height += 200
                }
                zpSDK!!.pageSetup(800, height)
                zpSDK!!.DrawSpecialText(230, 10, PrinterInterface.Textfont.siyuanheiti, 30, "数据打印", 0, 1, 0) //3
                zpSDK!!.DrawSpecialText(
                    20,
                    10 + 60,
                    PrinterInterface.Textfont.siyuanheiti,
                    20,
                    "--------------------------------------------------",
                    0,
                    1,
                    0
                )
                printDrawText("登录账号:", incomeCountingBean.loginName, ystart, 9)
                ystart += 40
                if (incomeCountingBean.list1 != null && incomeCountingBean.list1.size > 0) {
                    val todayIncomeBean = incomeCountingBean.list1[0]
                    printDrawText("今日时间:", TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd"), ystart, 9)
                    ystart += 40
                    if (todayIncomeBean.payMoney.isNotEmpty()) {
                        printDrawText("总收费：", todayIncomeBean.payMoney + " 元", ystart, 9)
                        ystart += 40
                    }
                    if (todayIncomeBean.orderCount != -1) {
                        printDrawText("已下单：", todayIncomeBean.orderCount.toString() + " 笔", ystart, 9)
                        ystart += 40
                    }
                    if (todayIncomeBean.refusePayCount != -1) {
                        printDrawText("拒付费：", todayIncomeBean.refusePayCount.toString() + " 笔", ystart, 9)
                        ystart += 40
                    }
                    if (todayIncomeBean.partPayCount != -1) {
                        printDrawText("部分付费：", todayIncomeBean.partPayCount.toString() + " 笔", ystart, 11)
                        ystart += 40
                    }
                    if (todayIncomeBean.oweCount != -1) {
                        printDrawText("已追缴：", todayIncomeBean.oweCount.toString() + " 笔", ystart, 9)
                        ystart += 40
                    }
                    if (todayIncomeBean.passMoney.isNotEmpty()) {
                        printDrawText("被追缴：", todayIncomeBean.passMoney + " 元", ystart, 9)
                        ystart += 40
                    }
                    if (todayIncomeBean.oweMoney.isNotEmpty()) {
                        printDrawText("追缴他人：", todayIncomeBean.oweMoney + " 元", ystart, 11)
                        ystart += 40
                    }
                    if (todayIncomeBean.onlineMoney.isNotEmpty()) {
                        printDrawText("自主追缴：", todayIncomeBean.onlineMoney + " 元", ystart, 11)
                        ystart += 40
                    }
                }
                if (incomeCountingBean.list2 != null && incomeCountingBean.list2.size > 0) {
                    ystart += 40
                    val rangeIncomeBean = incomeCountingBean.list2[0]
                    printDrawText("统计时间：", incomeCountingBean.range, ystart, 9)
                    ystart += 40
                    printDrawText("总收入：", rangeIncomeBean.payMoney + " 元", ystart, 9)
                    ystart += 40
                    printDrawText("已下单：", rangeIncomeBean.orderCount.toString() + " 笔", ystart, 9)
                    ystart += 40
                }
                zpSDK!!.DrawSpecialText(
                    20,
                    ystart + 40 + 40,
                    PrinterInterface.Textfont.siyuanheiti,
                    20,
                    "--------------------------------------------------",
                    0,
                    1,
                    0
                )
                zpSDK!!.print(0, 0)
                zpSDK!!.printerStatus()
                printGetStatus()
                return 0
            } else {
                val now = Calendar.getInstance()
                val today = now[Calendar.YEAR].toString() + "年" + (now[Calendar.MONTH] + 1) + "月" + now[Calendar.DAY_OF_MONTH] + "日"
                val printInfo = JSONObject.parseObject(printText, PrintInfoBean::class.java)
                zpSDK!!.pageSetup(800, 1400)
                //zpSDK.drawGraphic(0, 0, 0, 0, bmp);
                zpSDK!!.DrawSpecialText(147, 10, PrinterInterface.Textfont.siyuanheiti, 24, "上海市机动车道路停车费", 0, 0, 0) //3
                zpSDK!!.DrawSpecialText(197, 10 + 36, PrinterInterface.Textfont.siyuanheiti, 24, "电子票据告知书", 0, 0, 0) //3
                drawText(10 + 36 + 40, 20, "-----------------------------------------------")
                drawText(10 + 36 + 40 + 32, 20, "停车单号:   " + printInfo.orderId)
                drawText(10 + 36 + 40 + 32 + 32, 20, "车牌号码:   " + printInfo.plateId)
                if (printInfo.roadId.length <= 17) {
                    drawText(yLocation, 20, "停车路段:   " + printInfo.roadId)
                } else if (printInfo.roadId.length > 17 && printInfo.roadId.length <= 34) {
                    drawText(yLocation, 20, "停车路段:   " + printInfo.roadId.substring(0, 17))
                    yLocation += 32
                    drawText(yLocation, 20, "           " + printInfo.roadId.substring(17))
                } else if (printInfo.roadId.length > 34 && printInfo.roadId.length <= 51) {
                    drawText(yLocation, 20, "停车路段:   " + printInfo.roadId.substring(0, 17))
                    yLocation += 32
                    drawText(yLocation, 20, "           " + printInfo.roadId.substring(17, 34))
                    yLocation += 32
                    drawText(yLocation, 20, "           " + printInfo.roadId.substring(34))
                } else {
                    return -2
                }
                drawText(yLocation + 48, 20, "停放时间:   ")
                drawText(yLocation + 32, 20, "            " + printInfo.startTime)
                drawText(yLocation + 64, 20, "            " + printInfo.leftTime)
                yLocation += 96
                drawText(yLocation, 20, "缴费金额:   " + printInfo.payMoney)
                yLocation += 32
                drawText(yLocation, 20, "-----------------------------------------------")
                yLocation += 36
                drawText(yLocation, 20, "----------------电子票据开具方式----------------")
                yLocation += 36
                drawText(yLocation, 20, "1、扫描下载“上海停车”官方APP、小程序(微信、支付宝)")
                yLocation += 36
                zpSDK!!.drawGraphic(
                    65 + 60,
                    yLocation,
                    300,
                    300,
                    BitmapFactory.decodeResource(BaseApplication.instance().resources, com.rt.common.R.mipmap.ic_print_qr)
                )
//                zpSDK!!.drawQrCode(65 + 60, yLocation, "https://shtc.jtcx.sh.cn/union.html", 0, 10, 0)
                yLocation += (300 + 18)
                drawText(yLocation, 20, "2、注册您的“上海停车”账号,绑定车牌。")
                yLocation += 36
                drawText(yLocation, 20, "3、在“停车缴费”---“我要开票”---“道路停车电子缴”")
                yLocation += 36
                drawText(yLocation, 20, "款书(票据)---下载您的道路停车票据")
//                yLocation += 36
//                drawText(yLocation, 20, "提示:")
//                yLocation += 36
//                drawText(yLocation, 20, printInfo.plateId + "的车主(单位)")
//                yLocation += 36
//                drawText(yLocation, 20, "您(单位)在" + today + "之前，累计有 " + printInfo.oweCount + " 笔道路停车欠费记")
//                yLocation += 36
//                drawText(yLocation, 20, "录，请您尽快在本市任一道路停车场补缴。（其中，属智慧道")
//                yLocation += 36
//                drawText(yLocation, 20, "路停车场的欠费，可在智慧道路停车场或者登录“上海停车”")
//                yLocation += 36
//                drawText(yLocation, 20, "官方APP、小程序查询补缴。）")
                yLocation += 36
                drawText(yLocation, 20, "--------------------注意事项-------------------")
                yLocation += 36
                drawText(yLocation, 20, "1、本告知书仅为您(单位)本次停车付费的凭证，不作为电子")
                yLocation += 36
                drawText(yLocation, 20, "   票据。")
                yLocation += 36
                drawText(yLocation, 20, "2、如需要核实有关停车收费情况请致电 " + printInfo.phone + " 。")
                yLocation += 36
                drawText(yLocation, 20, "3、如您需要电子票据的,请在即日起30天内，通过“上海停")
                yLocation += 36
                drawText(yLocation, 20, "   车”官方APP、小程序(微信、支付宝)下载。如您(单位)")
                yLocation += 36
                drawText(yLocation, 20, "   在下载电子票据过程中遇到问题，请将问题描述和您的")
                yLocation += 36
                drawText(yLocation, 20, "   姓名、电话等有效的联系方式反馈至邮箱service@shtc")
                yLocation += 36
                drawText(yLocation, 20, "   xx.com")
                yLocation += 36
                drawText(yLocation, 20, "-----------------------------------------------")
                yLocation += 36
                if (printInfo.remark.length <= 22) {
                    drawText(yLocation, 24, printInfo.remark)
                    yLocation += 36
                } else {
                    drawText(yLocation, 24, printInfo.remark.substring(0, 22))
                    drawText(yLocation + 36, 24, printInfo.remark.substring(22))
                    yLocation += 72
                }
                if (printInfo.company.length <= 22) {
                    drawText(yLocation, 24, printInfo.company)
                } else {
                    drawText(yLocation, 24, printInfo.company.substring(0, 22))
                    drawText(yLocation + 36, 24, printInfo.company.substring(22))
                }
            }
        }
        zpSDK!!.print(0, 0)
        zpSDK!!.printerStatus()
        printGetStatus()
        return 0
    }

    fun printGetStatus() {
        Thread {
            try {
                when (zpSDK?.GetStatus()) {
                    -1 -> {
                        Handler(Looper.getMainLooper()).post {
                            ToastUtil.showMiddleToast(i18n(com.rt.base.R.string.打印机状态异常))
                        }
                        return@Thread
                    }

                    0 -> {

                    }

                    1 -> {
                        Handler(Looper.getMainLooper()).post {
                            ToastUtil.showMiddleToast(i18n(com.rt.base.R.string.打印机缺纸))
                        }
                        return@Thread
                    }

                    2 -> {
                        Handler(Looper.getMainLooper()).post {
                            ToastUtil.showMiddleToast(i18n(com.rt.base.R.string.打印机开盖))
                        }
                        return@Thread
                    }
                }
            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    ToastUtil.showMiddleToast(i18n(com.rt.base.R.string.打印机状态异常))
                }
                return@Thread
            }
        }.start()
    }

    fun printDrawText(text1: String, text2: String, ystart: Int, spaceOffset: Int) {
        var space = ""
        var count = 35 - text2.length - spaceOffset
        for (i in 0..count) {
            space += " "
        }
        zpSDK!!.DrawSpecialText(
            20,
            ystart,
            PrinterInterface.Textfont.siyuanheiti,
            27,
            text1 + space + text2,
            0,
            0,
            0
        )
    }

    fun drawText(y: Int, fontSize: Int, txt: String) {
        zpSDK!!.DrawSpecialText(
            25,
            y,
            PrinterInterface.Textfont.siyuanheiti,
            fontSize,
            txt,
            0,
            0,
            0
        )
    }

}

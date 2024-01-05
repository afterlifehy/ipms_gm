package com.rt.ipms_mg.ui.activity.parking

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.TimeUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import com.zrq.spanbuilder.TextStyle
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.ExitMethodBean
import com.rt.base.bean.ParkingSpaceBean
import com.rt.base.bean.PrintInfoBean
import com.rt.base.bean.TicketPrintBean
import com.rt.base.dialog.DialogHelp
import com.rt.base.ds.PreferencesDataStore
import com.rt.base.ds.PreferencesKeys
import com.rt.base.ext.i18N
import com.rt.base.ext.show
import com.rt.base.ext.startArouter
import com.rt.base.util.ToastUtil
import com.rt.base.viewbase.VbBaseActivity
import com.rt.ipms_mg.R
import com.rt.ipms_mg.databinding.ActivityParkingSpaceBinding
import com.rt.ipms_mg.dialog.ExitMethodDialog
import com.rt.ipms_mg.mvvm.viewmodel.ParkingSpaceViewModel
import com.rt.common.event.EndOrderEvent
import com.rt.common.event.RefreshParkingLotEvent
import com.rt.common.event.RefreshParkingSpaceEvent
import com.rt.common.util.AppUtil
import com.rt.common.util.BluePrint
import com.rt.common.util.FileUtil
import com.rt.common.util.GlideUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Route(path = ARouterMap.PARKING_SPACE)
class ParkingSpaceActivity : VbBaseActivity<ParkingSpaceViewModel, ActivityParkingSpaceBinding>(), OnClickListener {
    val sizes = intArrayOf(19, 19)
    val colors = intArrayOf(com.rt.base.R.color.color_ff666666, com.rt.base.R.color.color_ff1a1a1a)
    val colors2 = intArrayOf(com.rt.base.R.color.color_ff666666, com.rt.base.R.color.color_fff70f0f)
    val styles = arrayOf(TextStyle.NORMAL, TextStyle.BOLD)

    var orderNo = ""
    var carLicense = ""
    var parkingSpaceBean: ParkingSpaceBean? = null

    var picBase64 = ""
    var photoType = 20

    var exitMethodDialog: ExitMethodDialog? = null
    var exitMethodList: MutableList<ExitMethodBean> = ArrayList()
    var currentMethod: ExitMethodBean? = null

    var type = ""
    var simId = ""

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(endOrderEvent: EndOrderEvent) {
        onBackPressedSupport()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(refreshParkingSpaceEvent: RefreshParkingSpaceEvent) {
        parkingSpaceRequest()
    }

    override fun initView() {
        orderNo = intent.getStringExtra(ARouterMap.ORDER_NO).toString()
        carLicense = intent.getStringExtra(ARouterMap.CAR_LICENSE).toString()
        binding.tvPlate.text = carLicense

        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivBack, com.rt.common.R.mipmap.ic_back_white)
        binding.layoutToolbar.tvTitle.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.white))
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivRight, com.rt.common.R.mipmap.ic_pic)
        binding.layoutToolbar.ivRight.show()
    }

    override fun initListener() {
        binding.layoutToolbar.flBack.setOnClickListener(this)
        binding.layoutToolbar.ivRight.setOnClickListener(this)
        binding.rrlArrears.setOnClickListener(this)
        binding.rrlExitMethod.setOnClickListener(this)
        binding.rlCamera.setOnClickListener(this)
        binding.rflNotification.setOnClickListener(this)
        binding.rflReport.setOnClickListener(this)
        binding.rflRenewal.setOnClickListener(this)
        binding.rflFinish.setOnClickListener(this)
    }

    override fun initData() {
        exitMethodList.add(ExitMethodBean("2", i18N(com.rt.base.R.string.收费员不在场欠费驶离)))
        exitMethodList.add(ExitMethodBean("1", i18N(com.rt.base.R.string.正常缴费驶离)))
        exitMethodList.add(ExitMethodBean("3", i18N(com.rt.base.R.string.当面拒绝驶离)))
        exitMethodList.add(ExitMethodBean("0", i18N(com.rt.base.R.string.正常关单)))
        exitMethodList.add(ExitMethodBean("4", i18N(com.rt.base.R.string.强制关单)))
        exitMethodList.add(ExitMethodBean("5", i18N(com.rt.base.R.string.线上支付)))
        exitMethodList.add(ExitMethodBean("9", i18N(com.rt.base.R.string.其它)))

        currentMethod = exitMethodList[3]
        binding.tvExitMethod.text = currentMethod?.name
        runBlocking {
            simId = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.simId)
            parkingSpaceRequest()
        }
    }

    fun parkingSpaceRequest() {
        showProgressDialog(20000)
        val param = HashMap<String, Any>()
        val jsonobject = JSONObject()
        jsonobject["orderNo"] = orderNo
        jsonobject["simId"] = simId
        param["attr"] = jsonobject
        mViewModel.parkingSpace(param)
    }

    @SuppressLint("CheckResult")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }

            R.id.iv_right -> {
                startArouter(ARouterMap.PIC, data = Bundle().apply {
                    putString(ARouterMap.PIC_ORDER_NO, orderNo)
                })
            }

            R.id.rrl_arrears -> {
                if (parkingSpaceBean?.historyCount != 0) {
                    startArouter(ARouterMap.DEBT_COLLECTION, data = Bundle().apply {
                        putString(ARouterMap.DEBT_CAR_LICENSE, carLicense)
                    })
                } else {
                    ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.当前车辆没有欠费记录))
                }
            }

            R.id.rrl_exitMethod -> {
                if (exitMethodDialog == null) {
                    exitMethodDialog = ExitMethodDialog(exitMethodList, currentMethod, object : ExitMethodDialog.ExitMethodCallBack {
                        override fun chooseExitMethod(method: ExitMethodBean) {
                            currentMethod = method
                            binding.tvExitMethod.text = currentMethod?.name
                        }
                    })
                }
                exitMethodDialog?.show()
            }

            R.id.rl_camera -> {
                var rxPermissions = RxPermissions(this@ParkingSpaceActivity)
                rxPermissions.request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).subscribe {
                    if (it) {
                        takePhoto()
                    }
                }
            }

            R.id.rfl_notification -> {
                var rxPermissions = RxPermissions(this@ParkingSpaceActivity)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    rxPermissions.request(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN).subscribe {
                        if (it) {
                            ticketPrintRequest()
                        }
                    }
                } else {
                    ticketPrintRequest()
                }
            }

            R.id.rfl_report -> {
                startArouter(ARouterMap.ABNORMAL_REPORT, data = Bundle().apply {
                    putString(ARouterMap.ABNORMAL_PARKING_NO, parkingSpaceBean?.parkingNo)
                    putString(ARouterMap.ABNORMAL_CARLICENSE, parkingSpaceBean?.carLicense)
                })
            }

            R.id.rfl_renewal -> {
                startArouter(ARouterMap.PREPAID, data = Bundle().apply {
                    if (parkingSpaceBean != null) {
                        if (BigDecimal(parkingSpaceBean!!.havePayMoney).toDouble() > 0.0) {
                            putDouble(ARouterMap.PREPAID_MIN_AMOUNT, 0.5)
                            putString(ARouterMap.PREPAID_CARLICENSE, parkingSpaceBean!!.carLicense)
                            putString(ARouterMap.PREPAID_PARKING_NO, parkingSpaceBean!!.parkingNo)
                            putString(ARouterMap.PREPAID_ORDER_NO, parkingSpaceBean!!.orderNo)
                        } else {
                            putDouble(ARouterMap.PREPAID_MIN_AMOUNT, 1.0)
                            putString(ARouterMap.PREPAID_CARLICENSE, parkingSpaceBean!!.carLicense)
                            putString(ARouterMap.PREPAID_PARKING_NO, parkingSpaceBean!!.parkingNo)
                            putString(ARouterMap.PREPAID_ORDER_NO, parkingSpaceBean!!.orderNo)
                        }
                    } else {
                        putDouble(ARouterMap.PREPAID_MIN_AMOUNT, 1.0)
                        putString(ARouterMap.PREPAID_CARLICENSE, "")
                        putString(ARouterMap.PREPAID_PARKING_NO, "")
                        putString(ARouterMap.PREPAID_ORDER_NO, "")
                    }
                })
            }

            R.id.rfl_finish -> {
                if (currentMethod == null) {
                    ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.请选择离场方式))
                    return
                }
                type = currentMethod!!.id
                DialogHelp.Builder().setTitle(i18N(com.rt.base.R.string.是否确定结束订单))
                    .setLeftMsg(i18N(com.rt.base.R.string.取消))
                    .setRightMsg(i18N(com.rt.base.R.string.确定)).setCancelable(true)
                    .setOnButtonClickLinsener(object : DialogHelp.OnButtonClickLinsener {
                        override fun onLeftClickLinsener(msg: String) {
                        }

                        override fun onRightClickLinsener(msg: String) {
                            showProgressDialog(20000)
                            val param = HashMap<String, Any>()
                            val jsonobject = JSONObject()
                            jsonobject["carLicense"] = carLicense
                            jsonobject["orderNo"] = orderNo
                            jsonobject["parkingNo"] = parkingSpaceBean?.parkingNo
                            jsonobject["leftType"] = type
                            jsonobject["simId"] = simId
                            param["attr"] = jsonobject
                            mViewModel.endOrder(param)
                        }

                    }).build(this@ParkingSpaceActivity).showDailog()
            }
        }
    }

    fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = createImageFile()
        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "com.rt.ipms_mg.fileprovider",
            photoFile!!
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        takePictureLauncher.launch(takePictureIntent)
    }

    val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            var imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
            imageBitmap = ImageUtils.compressBySampleSize(imageBitmap, 10)
            imageBitmap = FileUtil.compressToMaxSize(imageBitmap, 50, false)
            imageBitmap = ImageUtils.addTextWatermark(
                imageBitmap,
                TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"),
                16, Color.RED, 6f, 3f
            )
            imageBitmap = ImageUtils.addTextWatermark(
                imageBitmap,
                parkingSpaceBean?.parkingNo + "   " + carLicense,
                16, Color.RED, 6f, 19f
            )
            ImageUtils.save(imageBitmap, imageFile, Bitmap.CompressFormat.JPEG)
            FileUtils.notifySystemToScan(imageFile)
            val bytes = ConvertUtils.bitmap2Bytes(imageBitmap)
            picBase64 = EncodeUtils.base64Encode2String(bytes)
            uploadImg(parkingSpaceBean!!.orderNo, picBase64, imageFile!!.name)
        }
    }

    var currentPhotoPath = ""
    var imageFile: File? = null
    private fun createImageFile(): File? {
        // 创建图像文件名称
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        imageFile = File.createTempFile(
            "PNG_${timeStamp}_", /* 前缀 */
            ".png", /* 后缀 */
            storageDir /* 目录 */
        )

        currentPhotoPath = imageFile!!.absolutePath
        return imageFile
    }

    fun uploadImg(orderNo: String, photo: String, name: String) {
        val param = HashMap<String, Any>()
        val jsonobject = JSONObject()
        jsonobject["businessId"] = orderNo
        jsonobject["photoName"] = name
        jsonobject["photoType"] = photoType
        jsonobject["photoFormat"] = "png"
        jsonobject["photo"] = photo
        jsonobject["simId"] = simId
        param["attr"] = jsonobject
        mViewModel.picUpload(param)
    }

    fun ticketPrintRequest() {
        showProgressDialog(20000)
        val param = HashMap<String, Any>()
        val jsonobject = JSONObject()
        jsonobject["orderNo"] = orderNo
        jsonobject["simId"] = simId
        param["attr"] = jsonobject
        mViewModel.inquiryTransactionByOrderNo(param)
    }

    @SuppressLint("CheckResult")
    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            parkingSpaceLiveData.observe(this@ParkingSpaceActivity) {
                dismissProgressDialog()
                parkingSpaceBean = it
                binding.layoutToolbar.tvTitle.text = parkingSpaceBean?.parkingNo
                binding.tvPlate.text = parkingSpaceBean?.carLicense

                val strings = arrayOf(i18N(com.rt.base.R.string.开始时间), parkingSpaceBean?.startTime.toString())
                binding.tvStartTime.text = AppUtil.getSpan(strings, sizes, colors)

                val strings2 =
                    arrayOf(
                        i18N(com.rt.base.R.string.预付金额),
                        AppUtil.keepNDecimals(parkingSpaceBean?.havePayMoney.toString(), 2) + "元"
                    )
                binding.tvPrepayAmount.text = AppUtil.getSpan(strings2, sizes, colors)

                val strings3 = arrayOf(i18N(com.rt.base.R.string.超时时长), parkingSpaceBean?.timeOut.toString())
                binding.tvTimeoutDuration.text = AppUtil.getSpan(strings3, sizes, colors)

                val strings4 = arrayOf(i18N(com.rt.base.R.string.待缴费用), "${parkingSpaceBean?.realtimeMoney}元")
                binding.tvPendingFee.text = AppUtil.getSpan(strings4, sizes, colors2, styles)

                binding.tvArrearsNum.text = "${parkingSpaceBean?.historyCount}笔"
                binding.tvArrearsAmount.text = "${parkingSpaceBean?.historySum}元"
            }
            endOrderLiveData.observe(this@ParkingSpaceActivity) {
                dismissProgressDialog()
                if (type == "1" || type == "0") {
                    startArouter(ARouterMap.ORDER_INFO, data = Bundle().apply {
                        putString(ARouterMap.ORDER_INFO_ORDER_NO, orderNo)
                    })
                    finish()
                } else {
                    EventBus.getDefault().post(RefreshParkingLotEvent())
                    onBackPressedSupport()
                }
            }
            picUploadLiveData.observe(this@ParkingSpaceActivity) {
            }
            inquiryTransactionByOrderNoLiveData.observe(this@ParkingSpaceActivity) {
                dismissProgressDialog()
                if (it.result != null && it.result.size > 0) {
                    performPrintTasks(it.result) {

                    }
                }
            }
            errMsg.observe(this@ParkingSpaceActivity) {
                dismissProgressDialog()
                ToastUtil.showMiddleToast(it.msg)
            }
        }
    }

    fun performPrintTasks(printDataList: List<TicketPrintBean>, onComplete: () -> Unit) {
        val iterator = printDataList.iterator()

        fun printNext() {
            if (iterator.hasNext()) {
                val printData = iterator.next()
                startPrint(printData) {
                    // 打印完成后继续下一个打印
                    printNext()
                }
            } else {
                // 所有打印任务完成时调用 onComplete 回调
                onComplete()
            }
        }
        // 开始第一个打印任务
        printNext()
    }

    fun startPrint(it: TicketPrintBean, onComplete: () -> Unit) {
        val payMoney = it.payMoney
        val printInfo = PrintInfoBean(
            roadId = it.roadName,
            plateId = it.carLicense,
            payMoney = String.format("%.2f", payMoney.toFloat()),
            orderId = orderNo,
            phone = it.phone,
            startTime = it.startTime,
            leftTime = it.endTime,
            remark = it.remark,
            company = it.businessCname,
            oweCount = 0
        )
        Thread {
            BluePrint.instance?.zkblueprint(JSONObject.toJSONString(printInfo))
        }.start()
        GlobalScope.launch {
            delay(2000)
            // 执行打印完成后的回调
            onComplete()
        }

    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityParkingSpaceBinding.inflate(layoutInflater)
    }

    override fun isRegEventBus(): Boolean {
        return true
    }

    override fun onReloadData() {
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }

    override fun providerVMClass(): Class<ParkingSpaceViewModel> {
        return ParkingSpaceViewModel::class.java
    }
}
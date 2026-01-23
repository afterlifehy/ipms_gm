package com.rt.ipms_mg.ui.activity.parking

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.widget.PopupWindow
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.TimeUtils
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.Street
import com.rt.base.dialog.DialogHelp
import com.rt.base.ds.PreferencesDataStore
import com.rt.base.ds.PreferencesKeys
import com.rt.base.ext.gone
import com.rt.base.ext.hide
import com.rt.base.ext.i18N
import com.rt.base.ext.i18n
import com.rt.base.ext.show
import com.rt.base.ext.startArouter
import com.rt.base.util.ToastUtil
import com.rt.base.viewbase.VbBaseActivity
import com.rt.ipms_mg.R
import com.rt.ipms_mg.adapter.CollectionPlateColorAdapter
import com.rt.ipms_mg.databinding.ActivityAdmissionTakePhotoBinding
import com.rt.ipms_mg.dialog.PromptDialog
import com.rt.ipms_mg.mvvm.viewmodel.AdmissionTakePhotoViewModel
import com.rt.ipms_mg.pop.MultipleSeatsPop
import com.rt.common.realm.RealmUtil
import com.rt.common.util.AppUtil
import com.rt.common.util.Constant
import com.rt.common.util.FileUtil
import com.rt.common.util.GlideUtils
import com.rt.common.view.keyboard.KeyboardUtil
import com.rt.common.util.ImageCompressor
import com.rt.common.util.ImageUtil
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Route(path = ARouterMap.ADMISSION_TAKE_PHOTO)
class AdmissionTakePhotoActivity : VbBaseActivity<AdmissionTakePhotoViewModel, ActivityAdmissionTakePhotoBinding>(), OnClickListener {
    private lateinit var keyboardUtil: KeyboardUtil
    var collectionPlateColorAdapter: CollectionPlateColorAdapter? = null
    var collectioPlateColorList: MutableList<String> = ArrayList()
    var checkedColor = ""
    val widthType = 2
    var parkingNo = ""
    var parkingAmount = 0

    var multipleSeatsPop: MultipleSeatsPop? = null
    var multipleSeat = ""

    var photoType = 10
    var plateBase64 = ""
    var panoramaBase64 = ""
    var plateImageBitmap: Bitmap? = null
    var panoramaImageBitmap: Bitmap? = null

    var promptDialog: PromptDialog? = null
    var simId = ""
    var vehicleType = "1"
    var extParkingNo = ""
    var loginName = ""
    var street: Street? = null

    override fun initView() {
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivBack, com.rt.common.R.mipmap.ic_back_white)
        binding.layoutToolbar.tvTitle.text = i18N(com.rt.base.R.string.入场拍照)
        binding.layoutToolbar.tvTitle.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.white))

        parkingNo = intent.getStringExtra(ARouterMap.ADMISSION_TAKE_PHOTO_PARKING_NO).toString()
        parkingAmount = intent.getIntExtra(ARouterMap.ADMISSION_TAKE_PHOTO_PARKING_AMOUNT, 0)
        collectioPlateColorList.add(Constant.BLUE)
        collectioPlateColorList.add(Constant.GREEN)
        collectioPlateColorList.add(Constant.YELLOW)
        collectioPlateColorList.add(Constant.YELLOW_GREEN)
        collectioPlateColorList.add(Constant.WHITE)
        collectioPlateColorList.add(Constant.BLACK)
        collectioPlateColorList.add(Constant.OTHERS)

        binding.rvPlateColor.setHasFixedSize(true)
        binding.rvPlateColor.layoutManager = LinearLayoutManager(BaseApplication.instance(), LinearLayoutManager.HORIZONTAL, false)
        collectionPlateColorAdapter = CollectionPlateColorAdapter(widthType, collectioPlateColorList, this)
        binding.rvPlateColor.adapter = collectionPlateColorAdapter

        binding.tvParkingNo.text = parkingNo
        street = RealmUtil.instance?.findCurrentStreet()
        binding.tvStreetName.text = street?.streetName
        binding.pvPlate.setPlateBgAndTxtColor(Constant.BLUE)

        initKeyboard()
    }

    override fun initListener() {
        binding.layoutToolbar.flBack.setOnClickListener(this)
        binding.rflMultipleSeats.setOnClickListener(this)
        binding.ivRecognize.setOnClickListener(this)
        binding.rflTakePhoto.setOnClickListener(this)
        binding.rflTakePhoto2.setOnClickListener(this)
        binding.ivPlateDelete.setOnClickListener(this)
        binding.ivPanoramaDelete.setOnClickListener(this)
        binding.rivPlate.setOnClickListener(this)
        binding.rivPanorama.setOnClickListener(this)
        binding.rflStartBilling.setOnClickListener(this)
        binding.root.setOnClickListener(this)
        binding.layoutToolbar.toolbar.setOnClickListener(this)
    }

    override fun initData() {
        runBlocking {
            simId = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.simId)
            loginName = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.loginName)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initKeyboard() {
        keyboardUtil = KeyboardUtil(binding.kvKeyBoard) {
            binding.pvPlate.requestFocus()
            keyboardUtil.changeKeyboard(true)
        }

        binding.pvPlate.setOnTouchListener { v, p1 ->
            v.requestFocus()
            keyboardUtil.showKeyboard(show = {
                val location = IntArray(2)
                v.getLocationOnScreen(location)
                val editTextPosY = location[1]

                val screenHeight = window!!.windowManager.defaultDisplay.height
                val distanceToBottom: Int = screenHeight - editTextPosY - v.getHeight()

                if (binding.kvKeyBoard.height > distanceToBottom) {
                    // 当键盘高度超过输入框到屏幕底部的距离时，向上移动布局
                    binding.flPlate.translationY = (-(binding.kvKeyBoard.height - distanceToBottom)).toFloat()
                }
            }, hide = {
                binding.flPlate.translationY = 0f
            })
            keyboardUtil.changeKeyboard(true)
            keyboardUtil.setCallBack(object : KeyboardUtil.KeyInputCallBack {
                override fun keyInput(value: String) {
                    binding.pvPlate.setOnePlate(value)
                }

                override fun keyDelete() {
                    binding.pvPlate.keyDelete()
                }
            })
            true
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard()
            } else {
                return super.onKeyDown(keyCode, event)
            }
        }
        return false
    }

    @SuppressLint("NewApi")
    override fun onClick(v: View?) {
        if (keyboardUtil.isShow()) {
            keyboardUtil.hideKeyboard()
        }
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }

            R.id.rfl_multipleSeats -> {
                multipleSeatsPop =
                    MultipleSeatsPop(
                        this@AdmissionTakePhotoActivity,
                        parkingNo.substring(parkingNo.length - 3, parkingNo.length),
                        multipleSeat,
                        parkingAmount,
                        object : MultipleSeatsPop.MultipleSeatsCallback {
                            override fun selecctSeats(seat: String) {
                                multipleSeat = seat
                                if (multipleSeat.isNotEmpty()) {
                                    binding.tvMultipleSeats.text = ""
                                    binding.tvMultipleSeats.gone()
                                    binding.tvParkingNo.text = parkingNo + "-" + AppUtil.fillZero2(multipleSeat)
                                    val list = parkingNo.split("-")
                                    extParkingNo = "${list[0]}-${list[1]}-" + AppUtil.fillZero2(multipleSeat)
                                } else {
                                    binding.tvMultipleSeats.text = i18N(com.rt.base.R.string.一车多位)
                                    binding.tvMultipleSeats.show()
                                    binding.tvParkingNo.text = parkingNo
                                    extParkingNo = ""
                                }
                            }
                        })
                multipleSeatsPop?.showAsDropDown(v, (binding.rflMultipleSeats.width - SizeUtils.dp2px(92f)) / 2, SizeUtils.dp2px(3f))
                GlideUtils.instance?.loadImage(binding.ivArrow, com.rt.common.R.mipmap.ic_multiple_seat_arrow_up)
                multipleSeatsPop?.setOnDismissListener(object : PopupWindow.OnDismissListener {
                    override fun onDismiss() {
                        GlideUtils.instance?.loadImage(binding.ivArrow, com.rt.common.R.mipmap.ic_multiple_seat_arrow_down)
                    }
                })
            }

            R.id.iv_recognize -> {
                ARouter.getInstance().build(ARouterMap.SCAN_PLATE).navigation(this@AdmissionTakePhotoActivity, 1)
            }

            R.id.rfl_takePhoto -> {
                photoType = 10
                takePhoto()
            }

            R.id.rfl_takePhoto2 -> {
                photoType = 11
                takePhoto()
            }

            R.id.rfl_startBilling -> {
                if (binding.pvPlate.getPvTxt().isEmpty()) {
                    ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.请输入车牌号))
                    return
                }
                if (binding.pvPlate.getPvTxt().length != 7 && binding.pvPlate.getPvTxt().length != 8) {
                    ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.车牌长度只能是7位或8位))
                    return
                }
                if (checkedColor.isEmpty()) {
                    ToastUtil.showMiddleToast(i18n(com.rt.base.R.string.请选择车牌颜色))
                    return
                }
                if (plateImageBitmap == null) {
                    ToastUtil.showMiddleToast(i18n(com.rt.base.R.string.请上传车牌照))
                    return
                }
                if (panoramaImageBitmap == null) {
                    ToastUtil.showMiddleToast(i18n(com.rt.base.R.string.请上传全景照))
                    return
                }
                DialogHelp.Builder().setTitle(i18N(com.rt.base.R.string.是否确认下单))
                    .setRightMsg(i18N(com.rt.base.R.string.确定))
                    .setLeftMsg(i18N(com.rt.base.R.string.取消)).setCancelable(true)
                    .setOnButtonClickLinsener(object : DialogHelp.OnButtonClickLinsener {
                        override fun onLeftClickLinsener(msg: String) {
                        }

                        override fun onRightClickLinsener(msg: String) {
                            showProgressDialog(20000)
                            val param = HashMap<String, Any>()
                            val jsonobject = JSONObject()
                            jsonobject["carLicense"] = binding.pvPlate.getPvTxt()
                            jsonobject["parkingNo"] = parkingNo
                            jsonobject["inputter"] = loginName
                            jsonobject["plateColor"] = checkedColor
                            jsonobject["vehicleType"] = vehicleType
                            jsonobject["extParkingNo"] = extParkingNo
                            jsonobject["simId"] = simId
                            param["attr"] = jsonobject
                            mViewModel.placeOrder(param)
                        }

                    }).build(this@AdmissionTakePhotoActivity).showDailog()
            }

            R.id.iv_plateDelete -> {
                binding.rflTakePhoto.show()
                binding.rflPlateImg.gone()
                plateImageBitmap = null
                plateBase64 = ""
            }

            R.id.iv_panoramaDelete -> {
                binding.rflTakePhoto2.show()
                binding.rflPanoramaImg.gone()
                panoramaImageBitmap = null
                panoramaBase64 = ""
            }

            R.id.riv_plate -> {
                photoType = 10
                takePhoto()
            }

            R.id.riv_panorama -> {
                photoType = 11
                takePhoto()
            }

            R.id.fl_color -> {
                checkedColor = v.tag as String
                collectionPlateColorAdapter?.updateColor(checkedColor, collectioPlateColorList.indexOf(checkedColor))
                binding.pvPlate.setPlateBgAndTxtColor(checkedColor)
            }

            R.id.toolbar,
            binding.root.id -> {

            }
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            placeOrderLiveData.observe(this@AdmissionTakePhotoActivity) {
                dismissProgressDialog()

                val plateSavedFile = FileUtil.FileSaveToInside("${it.orderNo}_10.png", plateImageBitmap!!)
                plateBase64 = FileUtil.fileToBase64(plateSavedFile).toString()
                uploadImg(it.orderNo, plateBase64, "${it.orderNo}_10.png", 10)

                val panoramaSavedFile = FileUtil.FileSaveToInside("${it.orderNo}_11.png", panoramaImageBitmap!!)
                panoramaBase64 = FileUtil.fileToBase64(panoramaSavedFile).toString()
                uploadImg(it.orderNo, panoramaBase64, "${it.orderNo}_11.png", 11)
//                promptDialog = PromptDialog(
//                    i18N(com.rt.base.R.string.下单成功当前车辆有欠费记录是否追缴),
//                    i18N(com.rt.base.R.string.是),
//                    i18N(com.rt.base.R.string.否),
//                    object : PromptDialog.PromptCallBack {
//                        override fun leftClick() {
//                            startArouter(ARouterMap.DEBT_COLLECTION, data = Bundle().apply {
//                                putString(ARouterMap.DEBT_CAR_LICENSE, binding.pvPlate.getPvTxt())
//                            })
//                        }
//
//                        override fun rightClick() {
                promptDialog = PromptDialog(
                    i18N(com.rt.base.R.string.下单成功是否预支付),
                    i18N(com.rt.base.R.string.取消),
                    i18N(com.rt.base.R.string.确定),
                    object : PromptDialog.PromptCallBack {
                        override fun leftClick() {
                            onBackPressedSupport()
                        }

                        override fun rightClick() {
                            startArouter(ARouterMap.PREPAID, data = Bundle().apply {
                                putDouble(ARouterMap.PREPAID_MIN_AMOUNT, 1.0)
                                putString(ARouterMap.PREPAID_CARLICENSE, binding.pvPlate.getPvTxt())
                                putString(ARouterMap.PREPAID_PARKING_NO, parkingNo)
                                putString(ARouterMap.PREPAID_ORDER_NO, it.orderNo)
                            })
                            finish()
                        }

                    })
                promptDialog?.show()
//                        }

//                    })
//                promptDialog?.show()
            }
            errMsg.observe(this@AdmissionTakePhotoActivity) {
                try {
                    dismissProgressDialog()
                    ToastUtil.showMiddleToast(it.msg)
                } catch (_: Exception) {

                }
            }
            mException.observe(this@AdmissionTakePhotoActivity) {
                dismissProgressDialog()
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
        takePictureIntent.putExtra("android.intent.extra.quickCapture", true)
        if (photoType == 10) {
            takePictureLauncher10.launch(takePictureIntent)
        } else {
            takePictureLauncher11.launch(takePictureIntent)
        }
    }

    val takePictureLauncher10 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            ImageCompressor.compress(this@AdmissionTakePhotoActivity, imageFile10!!, object : ImageCompressor.CompressResult {
                override fun onSuccess(file: File) {
                    val waterContent1: String = street?.streetName + " " + parkingNo
                    val waterContent2: String =
                        binding.pvPlate.getPvTxt() + " " + TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss")
                    val bitmapCompressed = ImageUtil.getCompressedImage(file.absolutePath, 945f, 1140f)
                    var bitmapWater = ImageUtil.addWaterMark3(
                        bitmapCompressed!!,
                        waterContent1,
                        waterContent2,
                        this@AdmissionTakePhotoActivity
                    )
                    FileUtils.delete(imageFile10)
                    GlideUtils.instance?.loadImage(binding.rivPlate, bitmapWater)
                    binding.rflTakePhoto.hide()
                    binding.rflPlateImg.show()
                    plateImageBitmap = bitmapWater
                }

                override fun onError(e: Throwable) {

                }

            })
        }
    }

    val takePictureLauncher11 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            ImageCompressor.compress(this@AdmissionTakePhotoActivity, imageFile11!!, object : ImageCompressor.CompressResult {
                override fun onSuccess(file: File) {
                    val waterContent1: String = street?.streetName + " " + parkingNo
                    val waterContent2: String =
                        binding.pvPlate.getPvTxt() + " " + TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss")
                    val bitmapCompressed = ImageUtil.getCompressedImage(file.absolutePath, 945f, 1140f)
                    var bitmapWater = ImageUtil.addWaterMark3(
                        bitmapCompressed!!,
                        waterContent1,
                        waterContent2,
                        this@AdmissionTakePhotoActivity
                    )
                    GlideUtils.instance?.loadImage(binding.rivPanorama, bitmapWater)
                    binding.rflTakePhoto2.hide()
                    binding.rflPanoramaImg.show()
                    panoramaImageBitmap = bitmapWater
                    FileUtils.delete(imageFile11)
                }

                override fun onError(e: Throwable) {

                }

            })
        }
    }

    var imageFile10: File? = null
    var imageFile11: File? = null
    private fun createImageFile(): File? {
        // 创建图像文件名称
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (photoType == 10) {
            imageFile10 = File(storageDir, "PNG_${timeStamp}_${photoType}.png")
            return imageFile10
        } else {
            imageFile11 = File(storageDir, "PNG_${timeStamp}_${photoType}.png")
            return imageFile11
        }
    }

    fun uploadImg(orderNo: String, photo: String, name: String, type: Int) {
        val param = HashMap<String, Any>()
        val jsonobject = JSONObject()
        jsonobject["businessId"] = orderNo
        jsonobject["photoName"] = name
        jsonobject["photoType"] = type
        jsonobject["photoFormat"] = "png"
        jsonobject["photo"] = photo
        jsonobject["simId"] = simId
        param["attr"] = jsonobject
        mViewModel.picUpload(param)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val plate = data?.getStringExtra("plate")
                if (!plate.isNullOrEmpty()) {
                    val plateId = if (plate.contains("新能源")) {
                        plate.substring(plate.length - 8, plate.length)
                    } else {
                        plate.substring(plate.length.minus(7) ?: 0, plate.length)
                    }
                    binding.pvPlate.setAllPlate(plateId)
                    if (plate.startsWith("蓝")) {
                        checkedColor = Constant.BLUE
                        collectionPlateColorAdapter?.updateColor(checkedColor, 0)
                        binding.pvPlate.setPlateBgAndTxtColor(checkedColor)
                    } else if (plate.startsWith("绿")) {
                        checkedColor = Constant.GREEN
                        collectionPlateColorAdapter?.updateColor(checkedColor, 1)
                        binding.pvPlate.setPlateBgAndTxtColor(checkedColor)
                    } else if (plate.startsWith("黄")) {
                        checkedColor = Constant.YELLOW
                        collectionPlateColorAdapter?.updateColor(checkedColor, 2)
                        binding.pvPlate.setPlateBgAndTxtColor(checkedColor)
                    } else if (plate.startsWith("黄绿")) {
                        checkedColor = Constant.YELLOW_GREEN
                        collectionPlateColorAdapter?.updateColor(checkedColor, 3)
                        binding.pvPlate.setPlateBgAndTxtColor(checkedColor)
                    } else if (plate.startsWith("白")) {
                        checkedColor = Constant.WHITE
                        collectionPlateColorAdapter?.updateColor(checkedColor, 4)
                        binding.pvPlate.setPlateBgAndTxtColor(checkedColor)
                    } else if (plate.startsWith("黑")) {
                        checkedColor = Constant.BLACK
                        collectionPlateColorAdapter?.updateColor(Constant.BLACK, 5)
                        binding.pvPlate.setPlateBgAndTxtColor(Constant.BLACK)
                    } else {
                        checkedColor = Constant.OTHERS
                        collectionPlateColorAdapter?.updateColor(checkedColor, 6)
                        binding.pvPlate.setPlateBgAndTxtColor(checkedColor)
                    }
                    binding.rflTakePhoto.show()
                    binding.rflPlateImg.gone()
                    plateImageBitmap = null
                    plateBase64 = ""
                    binding.rflTakePhoto2.show()
                    binding.rflPanoramaImg.gone()
                    panoramaImageBitmap = null
                    panoramaBase64 = ""
                }
            }
        }
    }

    override fun providerVMClass(): Class<AdmissionTakePhotoViewModel>? {
        return AdmissionTakePhotoViewModel::class.java
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityAdmissionTakePhotoBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }
}
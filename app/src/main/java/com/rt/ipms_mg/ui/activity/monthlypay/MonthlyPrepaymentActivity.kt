package com.rt.ipms_mg.ui.activity.monthlypay

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.KeyEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.TimeUtils
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.Street
import com.rt.base.ext.i18N
import com.rt.base.ext.i18n
import com.rt.base.ext.show
import com.rt.base.util.ToastUtil
import com.rt.base.viewbase.VbBaseActivity
import com.rt.common.realm.RealmUtil
import com.rt.common.util.Constant
import com.rt.common.util.GlideUtils
import com.rt.common.view.keyboard.KeyboardUtil
import com.rt.ipms_mg.R
import com.rt.ipms_mg.adapter.CollectionPlateColorAdapter
import com.rt.ipms_mg.databinding.ActivityMonthlyPrepaymentBinding
import com.rt.ipms_mg.dialog.PromptDialog
import com.rt.ipms_mg.mvvm.viewmodel.MonthlyPrepayMentViewModel
import com.rt.ipms_mg.pop.DatePop3
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Route(path = ARouterMap.MONTHLY_PREPAYMENT)
class MonthlyPrepaymentActivity : VbBaseActivity<MonthlyPrepayMentViewModel, ActivityMonthlyPrepaymentBinding>(), View.OnClickListener {
    private lateinit var keyboardUtil: KeyboardUtil
    var collectionPlateColorAdapter: CollectionPlateColorAdapter? = null
    var collectioPlateColorList: MutableList<String> = ArrayList()
    var checkedColor = ""
    var startDate = TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd")
    var endDate = ""
    var datePop: DatePop3? = null
    var currentStreet: Street? = null
    var promptDialog: PromptDialog? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        binding.layoutToolbar.tvTitle.text = "包月预付"
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivBack, com.rt.common.R.mipmap.ic_back_white)
        binding.layoutToolbar.tvTitle.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.white))
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivRight, com.rt.common.R.mipmap.ic_base_info)
        binding.layoutToolbar.ivRight.show()

        collectioPlateColorList.add(Constant.BLUE)
        collectioPlateColorList.add(Constant.GREEN)
        collectioPlateColorList.add(Constant.YELLOW)
        collectioPlateColorList.add(Constant.YELLOW_GREEN)
        collectioPlateColorList.add(Constant.WHITE)
        collectioPlateColorList.add(Constant.BLACK)
        collectioPlateColorList.add(Constant.OTHERS)

        val oneMonthLater = LocalDate.now().plusMonths(1)
        endDate = oneMonthLater.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        binding.tvDate.text = "日期：${startDate}~${endDate}"
        currentStreet = RealmUtil.instance?.findCurrentStreet()
        if (currentStreet!!.streetName.indexOf("(") < 0) {
            binding.tvStreetNo.text = currentStreet!!.streetNo + currentStreet!!.streetName
        } else {
            binding.tvStreetNo.text =
                currentStreet!!.streetNo + currentStreet!!.streetName.substring(0, currentStreet!!.streetName.indexOf("("))
        }

        binding.rvPlateColor.setHasFixedSize(true)
        binding.rvPlateColor.layoutManager = LinearLayoutManager(BaseApplication.instance(), LinearLayoutManager.HORIZONTAL, false)
        collectionPlateColorAdapter = CollectionPlateColorAdapter(2, collectioPlateColorList, this)
        binding.rvPlateColor.adapter = collectionPlateColorAdapter

        binding.tvAmount.text = "金额：0元"
        initKeyboard()
    }

    override fun initListener() {
        binding.layoutToolbar.flBack.setOnClickListener(this)
        binding.tvDate.setOnClickListener(this)
        binding.rvPlateColor.setOnClickListener(this)
        binding.rtvSubmit.setOnClickListener(this)
    }

    override fun initData() {
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

    override fun onClick(v: View?) {
        if (keyboardUtil.isShow()) {
            keyboardUtil.hideKeyboard()
        }
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }

            R.id.tv_date -> {
                datePop = DatePop3(BaseApplication.instance(), startDate, endDate, 0, object : DatePop3.DateCallBack {
                    override fun selectDate(startTime: String, endTime: String, type: Int) {
                        startDate = startTime
                        endDate = endTime
                        binding.tvDate.text = "日期：${startDate}~${endDate}"
                    }

                })
                datePop?.showAsDropDown(binding.layoutToolbar.toolbar)

            }

            R.id.fl_color -> {
                checkedColor = v.tag as String
                collectionPlateColorAdapter?.updateColor(checkedColor, collectioPlateColorList.indexOf(checkedColor))
                binding.pvPlate.setPlateBgAndTxtColor(checkedColor)

            }

            R.id.rtv_submit -> {
                if (binding.pvPlate.getPvTxt().isEmpty()) {
                    ToastUtil.showBottomToast("请输入车牌号")
                    return
                }
                if (checkedColor.isEmpty()) {
                    ToastUtil.showBottomToast("请选择车牌颜色")
                    return
                }
                if (binding.etName.text.toString().isEmpty()) {
                    ToastUtil.showBottomToast("请输入姓名")
                    return
                }
                if (binding.etPhone.text.toString().isEmpty()) {
                    ToastUtil.showBottomToast("请输入手机号")
                    return
                }
                promptDialog = PromptDialog("确认提交？", "取消", "确认", object : PromptDialog.PromptCallBack {
                    override fun leftClick() {
                    }

                    override fun rightClick() {
                    }
                })
                promptDialog?.show()
            }
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {

        }
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
                }
            }
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

    override fun getVbBindingView(): ViewBinding {
        return ActivityMonthlyPrepaymentBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun providerVMClass(): Class<MonthlyPrepayMentViewModel> {
        return MonthlyPrepayMentViewModel::class.java
    }

    override fun marginStatusBarView(): View? {
        return binding.layoutToolbar.ablToolbar
    }

}
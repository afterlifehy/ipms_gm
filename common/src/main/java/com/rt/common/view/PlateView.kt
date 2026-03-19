package com.rt.common.view

import android.content.Context
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.rt.base.BaseApplication
import com.rt.common.R
import com.rt.common.databinding.ViewPlateBinding
import com.rt.common.util.Constant

class PlateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), View.OnClickListener {
    var binding: ViewPlateBinding? = null
    var inputPosition = 0
    var plateTxtColorMap: MutableMap<String, Int> = ArrayMap()
    var plateBgColor = Constant.BLUE
    var callbakc: NumClickCallback? = null

    init {
        initView()
        plateTxtColorMap[Constant.BLACK] = com.rt.base.R.color.white
        plateTxtColorMap[Constant.WHITE] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.GREY] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.RED] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.BLUE] = com.rt.base.R.color.white
        plateTxtColorMap[Constant.YELLOW] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.ORANGE] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.BROWN] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.GREEN] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.PURPLE] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.CYAN] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.PINK] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.TRANSPARENT] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.YELLOW_GREEN] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.OTHERS] = com.rt.base.R.color.black
    }

    private fun initView() {
        binding = ViewPlateBinding.inflate(LayoutInflater.from(context), this, true)
        binding!!.tvPlate1.setOnClickListener(this)
        binding!!.tvPlate2.setOnClickListener(this)
        binding!!.tvPlate3.setOnClickListener(this)
        binding!!.tvPlate4.setOnClickListener(this)
        binding!!.tvPlate5.setOnClickListener(this)
        binding!!.tvPlate6.setOnClickListener(this)
        binding!!.tvPlate7.setOnClickListener(this)
        binding!!.tvPlate8.setOnClickListener(this)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            performClick()
        }
        return false
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onClick(v: View?) {
        callbakc?.numberClick()
        stopAnimation()
        var view: View? = null
        if (getPvTxt().isEmpty()) {
            view = binding!!.tvPlate1
        } else {
            view = v
        }
        when (view?.id) {
            R.id.tv_plate1 -> {
                inputPosition = 0
                binding!!.tvPlate1.performSelectAnimation()
            }

            R.id.tv_plate2 -> {
                inputPosition = 1
                binding!!.tvPlate2.performSelectAnimation()
            }

            R.id.tv_plate3 -> {
                inputPosition = 2
                binding!!.tvPlate3.performSelectAnimation()
            }

            R.id.tv_plate4 -> {
                inputPosition = 3
                binding!!.tvPlate4.performSelectAnimation()
            }

            R.id.tv_plate5 -> {
                inputPosition = 4
                binding!!.tvPlate5.performSelectAnimation()
            }

            R.id.tv_plate6 -> {
                inputPosition = 5
                binding!!.tvPlate6.performSelectAnimation()
            }

            R.id.tv_plate7 -> {
                inputPosition = 6
                binding!!.tvPlate7.performSelectAnimation()
            }

            R.id.tv_plate8 -> {
                inputPosition = 7
                binding!!.tvPlate8.performSelectAnimation()
            }
        }
    }

    fun stopAnimation() {
        binding!!.tvPlate1.stopSelectAnimation()
        binding!!.tvPlate2.stopSelectAnimation()
        binding!!.tvPlate3.stopSelectAnimation()
        binding!!.tvPlate4.stopSelectAnimation()
        binding!!.tvPlate5.stopSelectAnimation()
        binding!!.tvPlate6.stopSelectAnimation()
        binding!!.tvPlate7.stopSelectAnimation()
        binding!!.tvPlate8.stopSelectAnimation()
    }

    fun setOnePlate(value: String) {
        if (getPvTxt().isEmpty()) {
            inputPosition = 0
        }
        stopAnimation()
        when (inputPosition) {
            0 -> {
                binding!!.tvPlate1.getPlateView().text = value
                inputPosition = 1
                binding!!.tvPlate2.performSelectAnimation()
            }

            1 -> {
                binding!!.tvPlate2.getPlateView().text = value
                inputPosition = 2
                if (plateBgColor == Constant.WHITE) {
                    whiteWJTxtColor()
                }
                binding!!.tvPlate3.performSelectAnimation()
            }

            2 -> {
                binding!!.tvPlate3.getPlateView().text = value
                inputPosition = 3
                binding!!.tvPlate4.performSelectAnimation()
            }

            3 -> {
                binding!!.tvPlate4.getPlateView().text = value
                inputPosition = 4
                binding!!.tvPlate5.performSelectAnimation()
            }

            4 -> {
                binding!!.tvPlate5.getPlateView().text = value
                inputPosition = 5
                binding!!.tvPlate6.performSelectAnimation()
            }

            5 -> {
                binding!!.tvPlate6.getPlateView().text = value
                inputPosition = 6
                binding!!.tvPlate7.performSelectAnimation()
            }

            6 -> {
                binding!!.tvPlate7.getPlateView().text = value
                inputPosition = 7
                if (plateBgColor == Constant.WHITE) {
                    whiteWJTxtColor()
                }
                binding!!.tvPlate8.performSelectAnimation()
            }

            7 -> {
                if (binding!!.tvPlate7.getPlateView().text == "警" && value == "警") {
                    return
                } else {
                    binding!!.tvPlate8.getPlateView().text = value
                    inputPosition = 7
                }
                if (plateBgColor == Constant.WHITE) {
                    whiteWJTxtColor()
                }
                binding!!.tvPlate8.performSelectAnimation()
            }
        }
    }

    fun setAllPlate(value: String) {
        val plateArray = value.toCharArray()
        binding?.let {
            it.tvPlate1.getPlateView().text = plateArray.getOrNull(0)?.toString() ?: ""
        }
        binding?.let {
            it.tvPlate2.getPlateView().text = plateArray.getOrNull(1)?.toString() ?: ""
        }
        binding?.let {
            it.tvPlate3.getPlateView().text = plateArray.getOrNull(2)?.toString() ?: ""
        }
        binding?.let {
            it.tvPlate4.getPlateView().text = plateArray.getOrNull(3)?.toString() ?: ""
        }
        binding?.let {
            it.tvPlate5.getPlateView().text = plateArray.getOrNull(4)?.toString() ?: ""
        }
        binding?.let {
            it.tvPlate6.getPlateView().text = plateArray.getOrNull(5)?.toString() ?: ""
        }
        binding?.let {
            it.tvPlate7.getPlateView().text = plateArray.getOrNull(6)?.toString() ?: ""
        }
        binding?.let {
            it.tvPlate8.getPlateView().text = plateArray.getOrNull(7)?.toString() ?: ""
        }
        inputPosition = 7
        if (plateBgColor == Constant.WHITE) {
            whiteWJTxtColor()
        }
    }

    fun getPvTxt(): String {
        return "${binding!!.tvPlate1.getPlateView().text}${binding!!.tvPlate2.getPlateView().text}${binding!!.tvPlate3.getPlateView().text}${binding!!.tvPlate4.getPlateView().text}${binding!!.tvPlate5.getPlateView().text}${binding!!.tvPlate6.getPlateView().text}${binding!!.tvPlate7.getPlateView().text}${binding!!.tvPlate8.getPlateView().text}"
    }

    fun isCompliant(): Boolean {
        val plates = listOf(
            binding!!.tvPlate1.getPlateView().text,
            binding!!.tvPlate2.getPlateView().text,
            binding!!.tvPlate3.getPlateView().text,
            binding!!.tvPlate4.getPlateView().text,
            binding!!.tvPlate5.getPlateView().text,
            binding!!.tvPlate6.getPlateView().text,
            binding!!.tvPlate7.getPlateView().text,
            binding!!.tvPlate8.getPlateView().text
        )
        for (i in 0 until plates.size - 1) {
            if (plates[i].isEmpty() && plates[i + 1].isNotEmpty()) {
                return false
            }
        }
        return true
    }

    fun keyDelete() {
        stopAnimation()
        when (inputPosition) {
            0 -> {
                binding!!.tvPlate1.getPlateView().text = ""
                inputPosition = 0
                binding!!.tvPlate1.performSelectAnimation()
            }

            1 -> {
                binding!!.tvPlate2.getPlateView().text = ""
                inputPosition = 0
                binding!!.tvPlate1.performSelectAnimation()
            }

            2 -> {
                binding!!.tvPlate3.getPlateView().text = ""
                inputPosition = 1
                binding!!.tvPlate2.performSelectAnimation()
            }

            3 -> {
                binding!!.tvPlate4.getPlateView().text = ""
                inputPosition = 2
                binding!!.tvPlate3.performSelectAnimation()
            }

            4 -> {
                binding!!.tvPlate5.getPlateView().text = ""
                inputPosition = 3
                binding!!.tvPlate4.performSelectAnimation()
            }

            5 -> {
                binding!!.tvPlate6.getPlateView().text = ""
                inputPosition = 4
                binding!!.tvPlate5.performSelectAnimation()
            }

            6 -> {
                binding!!.tvPlate7.getPlateView().text = ""
                inputPosition = 5
                binding!!.tvPlate6.performSelectAnimation()
            }

            7 -> {
                binding!!.tvPlate8.getPlateView().text = ""
                inputPosition = 6
                binding!!.tvPlate7.performSelectAnimation()
            }

        }
    }

    fun setPlateBgAndTxtColor(color: String) {
        plateBgColor = color
        setTxtColor(plateBgColor)
        when (plateBgColor) {
            Constant.BLUE -> {
                setPlateImgBg(R.mipmap.ic_plate_blue_bg_start, R.mipmap.ic_plate_blue_bg_middle, R.mipmap.ic_plate_blue_bg_end)
            }

            Constant.GREEN -> {
                setPlateImgBg(R.mipmap.ic_plate_green_bg_start, R.mipmap.ic_plate_green_bg_middle, R.mipmap.ic_plate_green_bg_end)
            }

            Constant.YELLOW -> {
                setPlateImgBg(R.mipmap.ic_plate_yellow_bg_start, R.mipmap.ic_plate_yellow_bg_middle, R.mipmap.ic_plate_yellow_bg_end)
            }

            Constant.YELLOW_GREEN -> {
                setPlateImgBg2(
                    R.mipmap.ic_plate_yellow_bg_start,
                    R.mipmap.ic_plate_yellow_bg_middle,
                    R.mipmap.ic_plate_yellow_green_bg_middle,
                    R.mipmap.ic_plate_yellow_green_bg_end
                )
            }

            Constant.WHITE -> {
                whiteWJTxtColor()
                setPlateImgBg(R.mipmap.ic_plate_white_bg_start, R.mipmap.ic_plate_white_bg_middle, R.mipmap.ic_plate_white_bg_end)
            }

            Constant.BLACK -> {
                setPlateImgBg(R.mipmap.ic_plate_black_bg_start, R.mipmap.ic_plate_black_bg_middle, R.mipmap.ic_plate_black_bg_end)
            }

            Constant.OTHERS -> {
                setPlateImgBg(R.mipmap.ic_plate_white_bg_start, R.mipmap.ic_plate_white_bg_middle, R.mipmap.ic_plate_white_bg_end)
            }
        }
    }

    fun whiteWJTxtColor() {
        if (binding!!.tvPlate1.getPlateView().text == "W" && binding!!.tvPlate2.getPlateView().text == "J") {
            binding!!.tvPlate1.getPlateView().setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.color_ffeb0000
                )
            )
            binding!!.tvPlate2.getPlateView().setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.color_ffeb0000
                )
            )
        } else {
            binding!!.tvPlate1.getPlateView().setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.black
                )
            )
            binding!!.tvPlate2.getPlateView().setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.black
                )
            )
        }
        if (binding!!.tvPlate7.getPlateView().text == "警") {
            binding!!.tvPlate7.getPlateView().setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.color_ffeb0000
                )
            )
        } else {
            binding!!.tvPlate7.getPlateView().setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.black
                )
            )
        }

        if (binding!!.tvPlate8.getPlateView().text == "警") {
            binding!!.tvPlate8.getPlateView().setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.color_ffeb0000
                )
            )
        } else {
            binding!!.tvPlate8.getPlateView().setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.black
                )
            )
        }
    }

    fun setTxtColor(color: String) {
        binding!!.tvPlate1.getPlateView().setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate2.getPlateView().setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate3.getPlateView().setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate4.getPlateView().setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate5.getPlateView().setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate6.getPlateView().setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate7.getPlateView().setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate8.getPlateView().setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
    }

    fun setPlateImgBg(startImg: Int, middleImg: Int, endImg: Int) {
        binding!!.tvPlate1.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), startImg)
        binding!!.tvPlate2.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate3.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate4.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate5.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate6.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate7.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate8.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), endImg)
    }

    fun setPlateImgBg2(startImg: Int, middleImg: Int, middleImg2: Int, endImg: Int) {
        binding!!.tvPlate1.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), startImg)
        binding!!.tvPlate2.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate3.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg2)
        binding!!.tvPlate4.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg2)
        binding!!.tvPlate5.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg2)
        binding!!.tvPlate6.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg2)
        binding!!.tvPlate7.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg2)
        binding!!.tvPlate8.getPlateView().background = ContextCompat.getDrawable(BaseApplication.instance(), endImg)
    }

    fun setNumClickCallback(callback: NumClickCallback) {
        this.callbakc = callback
    }

    interface NumClickCallback {
        fun numberClick()
    }

}
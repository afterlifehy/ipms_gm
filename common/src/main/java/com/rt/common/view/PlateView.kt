package com.rt.common.view

import android.content.Context
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.rt.base.BaseApplication
import com.rt.common.R
import com.rt.common.databinding.ViewPlateBinding
import com.rt.common.util.Constant

class PlateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    var binding: ViewPlateBinding? = null
    var emptyPosition = 0
    var plateTxtColorMap: MutableMap<String, Int> = ArrayMap()
    var plateBgColor = Constant.BLUE

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
    }

    fun setOnePlate(value: String) {
        when (emptyPosition) {
            0 -> {
                binding!!.tvPlate1.text = value
                emptyPosition = 1
            }

            1 -> {
                binding!!.tvPlate2.text = value
                emptyPosition = 2
                if (plateBgColor == Constant.WHITE) {
                    whiteWJTxtColor()
                }
            }

            2 -> {
                binding!!.tvPlate3.text = value
                emptyPosition = 3
            }

            3 -> {
                binding!!.tvPlate4.text = value
                emptyPosition = 4
            }

            4 -> {
                binding!!.tvPlate5.text = value
                emptyPosition = 5
            }

            5 -> {
                binding!!.tvPlate6.text = value
                emptyPosition = 6
            }

            6 -> {
                binding!!.tvPlate7.text = value
                emptyPosition = 7
                if (plateBgColor == Constant.WHITE) {
                    whiteWJTxtColor()
                }
            }

            7 -> {
                if (binding!!.tvPlate7.text == "警" && value == "警") {
                    return
                } else {
                    binding!!.tvPlate8.text = value
                    emptyPosition = -1
                }
                if (plateBgColor == Constant.WHITE) {
                    whiteWJTxtColor()
                }
            }

            -1 -> {

            }
        }
    }

    fun setAllPlate(value: String) {
        val plateArray = value.toCharArray()
        binding!!.tvPlate1.text = plateArray[0].toString()
        binding!!.tvPlate2.text = plateArray[1].toString()
        binding!!.tvPlate3.text = plateArray[2].toString()
        binding!!.tvPlate4.text = plateArray[3].toString()
        binding!!.tvPlate5.text = plateArray[4].toString()
        binding!!.tvPlate6.text = plateArray[5].toString()
        binding!!.tvPlate7.text = plateArray[6].toString()
        if (plateArray.size == 8) {
            binding!!.tvPlate8.text = plateArray[7].toString()
        }
        emptyPosition = -1
        if (plateBgColor == Constant.WHITE) {
            whiteWJTxtColor()
        }
    }

    fun getPvTxt(): String {
        return "${binding!!.tvPlate1.text}${binding!!.tvPlate2.text}${binding!!.tvPlate3.text}${binding!!.tvPlate4.text}${binding!!.tvPlate5.text}${binding!!.tvPlate6.text}${binding!!.tvPlate7.text}${binding!!.tvPlate8.text}"
    }

    fun keyDelete() {
        when (emptyPosition) {
            0 -> {

            }

            1 -> {
                binding!!.tvPlate1.text = ""
                emptyPosition = 0
            }

            2 -> {
                binding!!.tvPlate2.text = ""
                emptyPosition = 1
            }

            3 -> {
                binding!!.tvPlate3.text = ""
                emptyPosition = 2
            }

            4 -> {
                binding!!.tvPlate4.text = ""
                emptyPosition = 3
            }

            5 -> {
                binding!!.tvPlate5.text = ""
                emptyPosition = 4
            }

            6 -> {
                binding!!.tvPlate6.text = ""
                emptyPosition = 5
            }

            7 -> {
                binding!!.tvPlate7.text = ""
                emptyPosition = 6
            }

            -1 -> {
                binding!!.tvPlate8.text = ""
                emptyPosition = 7
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
        if (binding!!.tvPlate1.text == "W" && binding!!.tvPlate2.text == "J") {
            binding!!.tvPlate1.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.color_ffeb0000
                )
            )
            binding!!.tvPlate2.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.color_ffeb0000
                )
            )
        } else {
            binding!!.tvPlate1.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.black
                )
            )
            binding!!.tvPlate2.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.black
                )
            )
        }
        if (binding!!.tvPlate7.text == "警") {
            binding!!.tvPlate7.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.color_ffeb0000
                )
            )
        } else {
            binding!!.tvPlate7.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.black
                )
            )
        }

        if (binding!!.tvPlate8.text == "警") {
            binding!!.tvPlate8.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.color_ffeb0000
                )
            )
        } else {
            binding!!.tvPlate8.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.black
                )
            )
        }
    }

    fun setTxtColor(color: String) {
        binding!!.tvPlate1.setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate2.setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate3.setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate4.setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate5.setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate6.setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate7.setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
        binding!!.tvPlate8.setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[color]!!))
    }

    fun setPlateImgBg(startImg: Int, middleImg: Int, endImg: Int) {
        binding!!.tvPlate1.background = ContextCompat.getDrawable(BaseApplication.instance(), startImg)
        binding!!.tvPlate2.background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate3.background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate4.background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate5.background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate6.background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate7.background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate8.background = ContextCompat.getDrawable(BaseApplication.instance(), endImg)
    }

    fun setPlateImgBg2(startImg: Int, middleImg: Int, middleImg2: Int, endImg: Int) {
        binding!!.tvPlate1.background = ContextCompat.getDrawable(BaseApplication.instance(), startImg)
        binding!!.tvPlate2.background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg)
        binding!!.tvPlate3.background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg2)
        binding!!.tvPlate4.background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg2)
        binding!!.tvPlate5.background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg2)
        binding!!.tvPlate6.background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg2)
        binding!!.tvPlate7.background = ContextCompat.getDrawable(BaseApplication.instance(), middleImg2)
        binding!!.tvPlate8.background = ContextCompat.getDrawable(BaseApplication.instance(), endImg)
    }
}
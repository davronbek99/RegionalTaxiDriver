package dev.davron.regionaltaxidriver.utils.customUI

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import dev.davron.regionaltaxidriver.R

@SuppressLint("SetTextI18n")
class TopMenuNavigation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {
    private val paddingAll = R.dimen.margin_padding_16dp
    var leftText = TextView(context)
    val rightText = TextView(context)

    private var itemMenuClickListener: ((Int) -> Unit)? = null
    fun setItemMenuClickListener(f: (item: Int) -> Unit) {
        itemMenuClickListener = f
    }

    init {
        resources.getDimensionPixelOffset(paddingAll)
        orientation = HORIZONTAL
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        leftText.layoutParams = LayoutParams(
            0,
            LayoutParams.WRAP_CONTENT,
            1f
        )

        rightText.layoutParams = LayoutParams(
            0,
            LayoutParams.WRAP_CONTENT,
            1f
        )

        leftText.setPadding(
            resources.getDimensionPixelOffset(paddingAll),
            resources.getDimensionPixelOffset(paddingAll),
            resources.getDimensionPixelOffset(paddingAll),
            resources.getDimensionPixelOffset(paddingAll)
        )
        rightText.setPadding(
            resources.getDimensionPixelOffset(paddingAll),
            resources.getDimensionPixelOffset(paddingAll),
            resources.getDimensionPixelOffset(paddingAll),
            resources.getDimensionPixelOffset(paddingAll)
        )

        leftText.text = "LEFT"
        leftText.setBackgroundResource(R.drawable.bg_left_clicked)
        leftText.gravity = Gravity.CENTER
        leftText.setTextColor(Color.parseColor("#FFFFFF"))
//        leftText.typeface = fontFamily

        rightText.text = "RIGHT"
        rightText.setBackgroundResource(R.drawable.bg_right_not_clicked)
        rightText.gravity = Gravity.CENTER
        rightText.setTextColor(Color.parseColor("#15C657"))
//        rightText.typeface = fontFamily

        addView(leftText)
        addView(rightText)

        addMenu()

    }

    private fun addMenu() {
        leftText.setOnClickListener {
            leftText.setBackgroundResource(R.drawable.bg_left_clicked)
            rightText.setBackgroundResource(R.drawable.bg_right_not_clicked)
            leftText.setTextColor(Color.parseColor("#FFFFFF"))
            rightText.setTextColor(Color.parseColor("#15C657"))
            itemMenuClickListener?.invoke(1)
        }
        rightText.setOnClickListener {
            leftText.setBackgroundResource(R.drawable.bg_left_not_clicked)
            rightText.setBackgroundResource(R.drawable.bg_right_clicked)
            rightText.setTextColor(Color.parseColor("#FFFFFF"))
            leftText.setTextColor(Color.parseColor("#15C657"))
            itemMenuClickListener?.invoke(2)
        }
    }

    fun updateUi(value: Int) {

        if (value == 1) {
            leftText.setBackgroundResource(R.drawable.bg_left_clicked)
            rightText.setBackgroundResource(R.drawable.bg_right_not_clicked)
            leftText.setTextColor(Color.parseColor("#FFFFFF"))
            rightText.setTextColor(Color.parseColor("#15C657"))
        } else if (value == 2) {
            leftText.setBackgroundResource(R.drawable.bg_left_not_clicked)
            rightText.setBackgroundResource(R.drawable.bg_right_clicked)
            rightText.setTextColor(Color.parseColor("#FFFFFF"))
            leftText.setTextColor(Color.parseColor("#15C657"))
        }
    }

}
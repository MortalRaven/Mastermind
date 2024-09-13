package com.mort.mastermind.ui

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children

class ColorRow(context: Context) : ConstraintLayout(context) {

    fun addColorCircle(color: Int, onColorClickListener: (ImageView, Int) -> Unit): ImageView {

        val colorButton = ImageView(context).apply {
            id = View.generateViewId()
            setBackgroundResource(StyleManager.getFieldSelector())
            background.setTint(color)
            setOnClickListener { onColorClickListener(this, color) }
            layoutParams = LayoutParams(
                0,
                0
            ).apply {
                setMargins(5, 0, 5, 0)
                dimensionRatio = "1:1"
            }
        }

        return colorButton

    }

    fun deactivateColors(colors: ConstraintLayout) {
        for (i in colors.children) {
            if (i is ImageView) {
                i.isClickable = false
                i.isEnabled = false
            }
        }
    }

}
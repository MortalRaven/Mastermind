package com.mort.mastermind.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import com.mort.mastermind.R
import kotlin.math.roundToInt

class HintRows(context: Context) : ConstraintLayout(context) {

    fun addHintLayout(
        context: Context,
        attempts: Int,
        columnsAmount: Int
    ): ConstraintLayout {
        val hintLayout = ConstraintLayout(context).apply {
            id = View.generateViewId()
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 100f / attempts
            ).apply {
                setPadding(5)
                setMargins(0, 0, 0, 20)
                gravity = Gravity.CENTER
            }
        }

        val maxHintsWrap: Double = columnsAmount / 2.0
        val hintLayoutFlow = Flow(context).apply {
            id = View.generateViewId()
            setWrapMode(Flow.WRAP_CHAIN)
            setOrientation(Flow.HORIZONTAL)
            setHorizontalStyle(Flow.CHAIN_PACKED)
            setVerticalStyle(Flow.CHAIN_PACKED)
            setHorizontalGap(10)
            setVerticalGap(10)
            setMaxElementsWrap(maxHintsWrap.roundToInt())
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT
            ).apply {
                matchConstraintMaxHeight = 100
                topToTop = hintLayout.id
                bottomToBottom = hintLayout.id
                constrainedHeight = true
            }
        }

        hintLayout.addView(hintLayoutFlow)

        for (i in 0 until columnsAmount) {
            addHintsCircles(hintLayout, hintLayoutFlow)
        }

        return hintLayout
    }

    private fun addHintsCircles(hintsLayout: ConstraintLayout, hintsLayoutFlow: Flow) {

        val hint = ImageView(context).apply {
            id = View.generateViewId()
            layoutParams = LayoutParams(
                0,
                0
            ).apply {
                dimensionRatio = "1:1"
            }
            setBackgroundResource(R.drawable.circle_blank)
        }

        hintsLayout.addView(hint)
        hintsLayoutFlow.addView(hint)
    }
}
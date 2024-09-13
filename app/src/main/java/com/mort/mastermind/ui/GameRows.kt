package com.mort.mastermind.ui

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.mort.mastermind.R

class GameRows(context: Context) : ConstraintLayout(context) {

    fun addRowLayout(
        context: Context,
        rowIndex: Int,
        attempts: Int,
        columnsAmount: Int,
        onCircleClickListener: (ImageView) -> Unit
    ): ConstraintLayout {
        val rowLayout = ConstraintLayout(context).apply {
            id = View.generateViewId()
            if (rowIndex == 0) {
                setBackgroundResource(R.drawable.active_row_border)
            } else {
                setBackgroundResource(R.drawable.inactive_row_border)
            }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 20)
                weight = 100f / attempts
            }
        }

        val rowLayoutFlow = Flow(context).apply {
            id = View.generateViewId()
            setWrapMode(Flow.WRAP_CHAIN)
            setOrientation(Flow.HORIZONTAL)
            setHorizontalStyle(Flow.CHAIN_PACKED)
            setHorizontalGap(10)
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT
            ).apply {
                setPadding(10)
                topToTop = rowLayout.id
                bottomToBottom = rowLayout.id
                matchConstraintMaxHeight = 150
                constrainedHeight = true
            }
        }
        rowLayout.addView(rowLayoutFlow)

        for (i in 0 until columnsAmount) {
            addRowCircles(rowIndex, i, rowLayout, rowLayoutFlow, onCircleClickListener)
        }

        return rowLayout
    }

    private fun addRowCircles(rowIndex: Int, circleIndex: Int, rowLayout: ConstraintLayout, rowLayoutFlow: Flow, onCircleClickListener: (ImageView) -> Unit) {

        val circle = ImageView(context).apply {
            id = View.generateViewId()
            isClickable = rowIndex == 0
            isEnabled = rowIndex == 0
            setBackgroundResource(StyleManager.getFieldBlank())
            setOnClickListener { onCircleClickListener(this) }
            setTag(R.id.circleIndex, circleIndex)
            setTag(R.id.circleColor, 0)
            layoutParams = LayoutParams(
                0,
                0
            ).apply {
                dimensionRatio = "1:1"
            }
        }
        rowLayout.addView(circle)
        rowLayoutFlow.addView(circle)

    }

    fun fillCircle(view: ImageView, color: Int) {
        view.setBackgroundResource(StyleManager.getFieldFull())
        view.background.setTint(color)
        view.setTag(R.id.circleColor, color)
    }

    fun practiceAutofill(code: List<Int>, hintsList: List<String>, nextRow: ConstraintLayout) {
        for (i in hintsList.indices) {
            if (hintsList[i] == "good") {
                val goodField = nextRow.getChildAt(i + 1) as ImageView
                fillCircle(goodField, code[i])
            }
        }
    }

    fun activateRow(row: ConstraintLayout) {
        row.setBackgroundResource(R.drawable.active_row_border)

        for (i in row.children) {
            if (i is ImageView && i.getTag(R.id.circleColor) == 0) {
                i.isEnabled = true
                i.isClickable = true
            }
        }
    }

    fun deactivateRow(row: ConstraintLayout) {
        row.setBackgroundResource(R.drawable.inactive_row_border)

        for (i in row.children) {
            if (i is ImageView) {
                i.isEnabled = false
                i.isClickable = false
            }
        }
    }

}
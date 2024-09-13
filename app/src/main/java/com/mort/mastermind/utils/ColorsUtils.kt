package com.mort.mastermind.utils

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.mort.mastermind.GameActivity
import com.mort.mastermind.ui.GameRows
import java.util.Collections

interface ColorInputMode {
    fun onColorSelect(
        gameActivity: GameActivity,
        activeRow: ConstraintLayout,
        view: ImageView,
        color: Int,
        activeCircle: ImageView
    )
}

class AutoColorInput(private val setActiveCircle: (ImageView) -> Unit) : ColorInputMode {
    override fun onColorSelect(
        gameActivity: GameActivity,
        activeRow: ConstraintLayout,
        view: ImageView,
        color: Int,
        activeCircle: ImageView
    ) {

        GameRows(gameActivity).fillCircle(activeCircle, color)

        gameActivity.gameTimer.startTimer()

        val circleList = activeRow.children.filter { it is ImageView }.toList()
        val indexActive = circleList.indexOf(activeCircle)
        Collections.rotate(circleList, -indexActive - 1)
        val newActiveCircle = circleList.find { it.isEnabled } as ImageView

        setActiveCircle(newActiveCircle)
        AnimationUtil().animateCircle(gameActivity, newActiveCircle, activeCircle)
    }
}

class ManualColorInput(
    private val setSelectedColor: (Int) -> Unit
) : ColorInputMode {
    override fun onColorSelect(
        gameActivity: GameActivity,
        activeRow: ConstraintLayout,
        view: ImageView,
        color: Int,
        activeCircle: ImageView
    ) {
        setSelectedColor(color)
        val current = gameActivity.findCurrentColor()
        current?.isSelected = false
        current?.tag = null
        view.isSelected = true
        view.tag = "current"
    }
}
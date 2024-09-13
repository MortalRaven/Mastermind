package com.mort.mastermind.utils

import android.widget.ImageView
import com.mort.mastermind.GameActivity
import com.mort.mastermind.ui.GameRows

interface CircleClickMode {
    fun onCircleClick(
        gameActivity: GameActivity,
        view: ImageView, color: Int,
        activeCircle: ImageView? = null
    )
}

class AutoInputCircleClick(private val setActiveCircle: (ImageView) -> Unit) : CircleClickMode {
    override fun onCircleClick(
        gameActivity: GameActivity,
        view: ImageView,
        color: Int,
        activeCircle: ImageView?
    ) {
        AnimationUtil().animateCircle(gameActivity, view, activeCircle!!)
        setActiveCircle(view)
    }
}

class ManualInputCircleClick : CircleClickMode {
    override fun onCircleClick(
        gameActivity: GameActivity,
        view: ImageView,
        color: Int,
        activeCircle: ImageView?
    ) {
        GameRows(gameActivity).fillCircle(view, color)
        gameActivity.gameTimer.startTimer()
    }
}
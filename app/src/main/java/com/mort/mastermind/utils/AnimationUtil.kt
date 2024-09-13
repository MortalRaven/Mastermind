package com.mort.mastermind.utils

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.mort.mastermind.ui.StyleManager

class AnimationUtil {

    fun animateCircle(context: Context, active: ImageView, inactive: ImageView) {
        if (active != inactive) {
            stopAnimation(inactive)
        }
        active.foreground = ContextCompat.getDrawable(context, StyleManager.getFieldActive())
        val anim = active.foreground as AnimationDrawable
        anim.setEnterFadeDuration(1000)
        anim.setExitFadeDuration(1000)
        anim.start()
    }

    fun stopAnimation(active: ImageView) {
        val anim = active.foreground as AnimationDrawable
        anim.stop()
        anim.selectDrawable(0)
    }
}
package com.mort.mastermind.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import com.mort.mastermind.R
import com.mort.mastermind.ui.StyleManager

object PopupUtils {

    fun showEndPopup(
        context: Context,
        mainLayout: ConstraintLayout,
        won: Boolean,
        code: List<Int?>,
        columnsAmount: Int,
        onNewGameClicked: () -> Unit
    ) {
        val view = View.inflate(context, R.layout.end_popup_layout, null)
        val wl: TextView = view.findViewById(R.id.tv_winloose)
        val btnNewGame: Button = view.findViewById(R.id.btn_newgame)

        val codeanswer: ConstraintLayout = view.findViewById(R.id.answer_layout)
        val codeanswerflow: Flow = view.findViewById(R.id.answer_flow)

        for (i in 0 until columnsAmount) {
            val acircle = ImageView(context).apply {
                id = View.generateViewId()
                setBackgroundResource(StyleManager.getFieldFull())
                background.setTint(code[i] ?: R.color.m_red)
                layoutParams = ConstraintLayout.LayoutParams(
                    0,
                    0
                ).apply {
                    dimensionRatio = "1:1"
                    minimumHeight = 30
                    maxHeight = 50
                }
            }
            codeanswer.addView(acircle)
            codeanswerflow.addView(acircle)
        }

        val popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            false
        )

        if (won) {
            wl.text = context.getString(R.string.win)
        } else {
            wl.text = context.getString(R.string.loose)
        }

        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0)

        btnNewGame.setOnClickListener {
            popupWindow.dismiss()
            onNewGameClicked()
        }

    }

}
package com.mort.mastermind.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.mort.mastermind.R

class GameTimer(private val context: Context, private val tvTimer: TextView, private val showTimer: Boolean) {

    private var isRunning = false
    private var timerSeconds = 0
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            timerSeconds++
            val hours = timerSeconds / 3600
            val minutes = (timerSeconds % 3600) / 60
            val seconds = timerSeconds % 60

            val time = context.getString(R.string.timer, hours, minutes, seconds)
            tvTimer.text = time

            handler.postDelayed(this, 1000)
        }
    }

    fun resetTimer() {
        stopTimer()

        timerSeconds = 0
        val time = context.getString(R.string.timer, 0, 0, 0)
        tvTimer.text = time
    }

    fun startTimer() {
        if (showTimer && !isRunning) {
            handler.postDelayed(runnable, 1000)
            isRunning = true
        }
    }

    fun stopTimer() {
        if (showTimer && isRunning) {
            handler.removeCallbacks(runnable)
            isRunning = false
        }
    }

}
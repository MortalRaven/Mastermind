package com.mort.mastermind.logics

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.core.view.size
import com.mort.mastermind.R
import com.mort.mastermind.ui.StyleManager

class CodeLogic(context: Context) {

    private val hintsPalette: IntArray = context.resources.getIntArray(StyleManager.getHintsPalette())

    //generowanie kodu do rozwiązania
    fun createCode(
        uniqueColors: Boolean,
        colorsAmount: Int,
        columnsAmount: Int,
        gamePalette: IntArray
    ): List<Int> {
        val nums = if (uniqueColors) {
            (1..colorsAmount).shuffled().take(columnsAmount)
        } else {
            List(columnsAmount) { (1..colorsAmount).random() }
        }
        println("KOD: ${nums.joinToString(", ")}")
        return nums.map { gamePalette[it - 1] }
    }

    //sprawdzanie poprawności wprowadzonego kodu
    fun checkCode(
        practiceMode: Boolean,
        actualHintsLayout: ConstraintLayout,
        gameRow: List<Int>,
        code: List<Int?>
    ): Pair<Boolean, List<String>> {
        val actualRow = gameRow.toMutableList()
        val codeTemp = code.toMutableList()
        val hintsList = MutableList(actualRow.size) { "wrong" }

        //Dobry kolor na dobrym miejscu
        for (i in actualRow.indices) {
            if (codeTemp[i] == actualRow[i]) {
                actualRow[i] = 0
                codeTemp[i] = 0
                hintsList[i] = "good"
            }
        }
        //Dobry kolor na złym miejscu
        for (i in actualRow.indices) {
            if (actualRow[i] != 0) {
                if (codeTemp.contains(actualRow[i])) {
                    val j = codeTemp.indexOfFirst { it == actualRow[i] }
                    codeTemp[j] = 0
                    actualRow[i] = 0
                    hintsList[i] = "misplaced"
                }
            }
        }

        if (!practiceMode) {
            fillHints(actualHintsLayout, hintsList.sorted())
        } else {
            fillHints(actualHintsLayout, hintsList)
        }

        return (hintsList.count { it == "good" } == actualRow.size) to hintsList
    }

    private fun fillHints(
        actualHintsLayout: ConstraintLayout,
        hintsList: List<String>
    ) {
        for (i in 1 until actualHintsLayout.size) {
            actualHintsLayout[i].setBackgroundResource(R.drawable.circle_full)
            when (hintsList[i - 1]) {
                "good" -> actualHintsLayout[i].background.setTint(hintsPalette[0])
                "misplaced" -> actualHintsLayout[i].background.setTint(hintsPalette[1])
                "wrong" -> actualHintsLayout[i].background.setTint(hintsPalette[2])
            }
        }
    }
}
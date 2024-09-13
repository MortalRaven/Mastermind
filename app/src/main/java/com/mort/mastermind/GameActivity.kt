package com.mort.mastermind

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.mort.mastermind.databinding.ActivityGameBinding
import com.mort.mastermind.logics.CodeLogic
import com.mort.mastermind.ui.ColorRow
import com.mort.mastermind.ui.StyleManager
import com.mort.mastermind.ui.GameRows
import com.mort.mastermind.ui.HintRows
import com.mort.mastermind.utils.AnimationUtil
import com.mort.mastermind.utils.AutoColorInput
import com.mort.mastermind.utils.AutoInputCircleClick
import com.mort.mastermind.utils.CircleClickMode
import com.mort.mastermind.utils.ColorInputMode
import com.mort.mastermind.utils.GameTimer
import com.mort.mastermind.utils.ManualColorInput
import com.mort.mastermind.utils.ManualInputCircleClick
import com.mort.mastermind.utils.PopupUtils

class GameActivity : BaseActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var rowsLayout: LinearLayout
    private lateinit var hintsLayout: LinearLayout
    private lateinit var colorsLayout: ConstraintLayout
    private lateinit var colorsLayoutFlow: Flow
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var gamePalette: IntArray
    private lateinit var colorInputMode: ColorInputMode
    private lateinit var circleClickMode: CircleClickMode
    lateinit var gameTimer: GameTimer
    private lateinit var btnApply: Button

    private var code: List<Int> = listOf()
    private var tvTimer: TextView? = null
    private var tvCounter: TextView? = null
    private var colorsAmount = 5
    private var uniqueColors = true
    private var columnsAmount = 4
    private var attempts = 8
    private var inputType = true
    private var showTimer = true
    private var practiceMode = false
    private var practiceModeAutofill = false

    private var attempt = 0
    private var selectedColor: Int = 0
    private lateinit var activeCircle: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        StyleManager.init(this)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        btnApply = binding.btnConfirm
        btnApply.setOnClickListener { onAccept() }

        showTimer = prefs.getBoolean(Constants.SHOW_TIMER, true)
        gamePalette = resources.getIntArray(StyleManager.getColorsPalette())
        colorsAmount = prefs.getInt(Constants.COLOR_AMOUNT, 5)
        uniqueColors = prefs.getBoolean(Constants.UNIQUE_COLORS, true)
        columnsAmount = prefs.getInt(Constants.COL_AMOUNT, 4)
        attempts = prefs.getInt(Constants.ROW_AMOUNT, 8)
        inputType = prefs.getBoolean(Constants.INPUT_TYPE, true)
        practiceMode = prefs.getBoolean(Constants.PRACTICE_MODE, false)
        practiceModeAutofill = if (practiceMode) prefs.getBoolean(Constants.PRACTICE_AUTOFILL, false) else false
        gameTimer = GameTimer(this, binding.gameTimer, showTimer)

        tvTimer = binding.gameTimer
        tvTimer?.isVisible = showTimer
        tvCounter = binding.gameCounter
        tvCounter?.text = getString(R.string.counter, attempt + 1, attempts)
        selectedColor = ContextCompat.getColor(this@GameActivity, R.color.m_red)
        rowsLayout = binding.gameRows
        hintsLayout = binding.hintRows
        colorsLayout = binding.gamePalette
        colorsLayoutFlow = binding.gamePaletteFlow
        mainLayout = binding.main


        gameTimer.resetTimer()
        code = CodeLogic(this).createCode(uniqueColors, colorsAmount, columnsAmount, gamePalette)


        createGameTable()

        setInputMode()

        setCircleMode()

        setActiveFirstCircleInRow(attempt)

        if (inputType) {
            AnimationUtil().animateCircle(this, activeCircle, activeCircle)
        } else {
            val currentColor = findViewById<ImageView>(colorsLayoutFlow.referencedIds[0])
            currentColor.isSelected = true
            currentColor.tag = "current"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.menuNewgame -> this.recreate()
            R.id.menuHowToPlay -> startActivity(Intent(this, TutorialActivity::class.java))
            R.id.menuOptions -> startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createGameTable() {
        for (i in 0 until attempts) {
            val newRow = GameRows(this).addRowLayout(
                this, i, attempts, columnsAmount
            ) { circle ->
                onCircleClick(
                    circle, selectedColor
                )
            }
            rowsLayout.addView(newRow)

            val newHintRow = HintRows(this).addHintLayout(
                this, attempts, columnsAmount
            )
            hintsLayout.addView(newHintRow)
        }

        for (i in 0 until colorsAmount) {
            val colorCircle = ColorRow(this).addColorCircle(gamePalette[i]) { circle, color ->
                onColorSelect(
                    circle,
                    color
                )
            }
            colorsLayout.addView(colorCircle)
            colorsLayoutFlow.addView(colorCircle)
        }
    }

    private fun setInputMode() {
        colorInputMode = if (inputType) {
            AutoColorInput { circle -> activeCircle = circle}
        } else {
            ManualColorInput { color -> selectedColor = color }
        }
    }

    private fun setCircleMode() {
        circleClickMode = if (inputType) {
            AutoInputCircleClick { circle -> activeCircle = circle }
        } else {
            ManualInputCircleClick()
        }
    }

    private fun onCircleClick(view: ImageView, tint: Int) {
        circleClickMode.onCircleClick(this, view, tint, activeCircle)
    }
    private fun onColorSelect(view: ImageView, color: Int) {
        colorInputMode.onColorSelect(this, (rowsLayout[attempt] as ConstraintLayout), view, color, activeCircle)
    }

    fun findCurrentColor(): ImageView? {
        return colorsLayout.findViewWithTag("current")
    }
    private fun setActiveFirstCircleInRow (rowIndex: Int) {
        val activeRow = rowsLayout.getChildAt(rowIndex) as ConstraintLayout
        val firstCircle = activeRow.children.first{it is ImageView && it.isEnabled} as ImageView
        activeCircle = firstCircle
    }

    private fun onAccept() {
        //Komunikat jeśli nie wszystkie pola są wypełnione
        if (!checkAllFieldsFilled()) {
            emptyFieldsWarning()
            return
        }

        val rowColors = getRowColors(rowsLayout[attempt] as ConstraintLayout)
        //Sprawdzenie czy wpisany kod jest rozwiązaniem i dodanie podpowiedzi
        val (wl, hintsList) = checkCodeAndMakeHints(rowColors)

        //Wyświetlenie popupu jeśli wygrana/przegrana lub aktywacja następnego rzędu
        if (wl || attempt + 1 == attempts) {
            showEndGamePopup(wl)
            deactivateGameTable()
            gameTimer.stopTimer()
        } else {
            activateNextRow(hintsList)
        }
    }

    private fun checkAllFieldsFilled(): Boolean {
        val tt = rowsLayout.getChildAt(attempt) as ConstraintLayout
        for (i in tt.children) {
            if (i is ImageView && i.getTag(R.id.circleColor) == 0)
                return false
        }
        return true
    }

    private fun emptyFieldsWarning() {
        Snackbar.make(mainLayout, R.string.empty_fields, Snackbar.LENGTH_SHORT).show()
    }

    private fun getRowColors(activeRow: ConstraintLayout): List<Int> {
        val rowColorsList = mutableListOf<Int>()

        for (i in activeRow.children) {
            if (i is ImageView) {
                rowColorsList += i.getTag(R.id.circleColor) as Int
            }
        }
        return rowColorsList
    }

    private fun checkCodeAndMakeHints(rowColors: List<Int>): Pair<Boolean, List<String>> {
        return CodeLogic(this).checkCode(
            practiceMode,
            hintsLayout[attempt] as ConstraintLayout,
            rowColors,
            code
        )
    }

    private fun showEndGamePopup(wl: Boolean) {
        PopupUtils.showEndPopup(
            context = this,
            mainLayout = mainLayout,
            won = wl,
            code = code,
            columnsAmount = columnsAmount,
            onNewGameClicked = { this.recreate() }
        )
    }

    private fun deactivateGameTable() {
        //Blokowanie pól kolorów
        ColorRow(this).deactivateColors(colorsLayout)

        //Wyłączenie animacji aktywnego pola
        if (inputType) {
            AnimationUtil().stopAnimation(activeCircle)
        }
    }

    private fun activateNextRow(hintsList: List<String>) {
        //Dezaktywacja aktualnego rzędu pól
        GameRows(this).deactivateRow(rowsLayout[attempt] as ConstraintLayout)

        if (practiceModeAutofill) {
            GameRows(this).practiceAutofill(code, hintsList, rowsLayout[attempt + 1] as ConstraintLayout)
        }

        //Aktywacja kolejnego rzędu pól
        GameRows(this).activateRow(rowsLayout[attempt + 1] as ConstraintLayout)

        //Zwiększenie countera
        attempt += 1
        tvCounter?.text = getString(R.string.counter, attempt + 1, attempts)

        //Ustawienie pierwszego pola w zależności od trybu wprowadzania
        if (inputType) {
            val temp = activeCircle
            setActiveFirstCircleInRow(attempt)
            AnimationUtil().animateCircle(this, activeCircle, temp)
        }
    }
}


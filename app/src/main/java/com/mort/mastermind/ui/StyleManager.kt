package com.mort.mastermind.ui

import android.content.Context
import android.content.res.TypedArray
import com.mort.mastermind.R

object StyleManager {
    private lateinit var typedArray: TypedArray
    private var fieldActive: Int = 0
    private var fieldBlank: Int = 0
    private var fieldFull: Int = 0
    private var fieldSelector: Int = 0
    private var colorPalette: Int = 0
    private var hintsPalette: Int = 0

    fun init(context: Context) {
        typedArray = context.theme.obtainStyledAttributes(R.styleable.FieldsStyle)
        fieldActive = typedArray.getResourceId(R.styleable.FieldsStyle_field_active, R.drawable.circle_active)
        fieldBlank = typedArray.getResourceId(R.styleable.FieldsStyle_field_blank, R.drawable.circle_blank)
        fieldFull = typedArray.getResourceId(R.styleable.FieldsStyle_field_full, R.drawable.circle_full)
        fieldSelector = typedArray.getResourceId(R.styleable.FieldsStyle_field_selector, R.drawable.circle_selector)

        typedArray = context.theme.obtainStyledAttributes(R.styleable.ColorsPalette)
        colorPalette = typedArray.getResourceId(R.styleable.ColorsPalette_colorPalette, R.array.game_palette)
        hintsPalette = typedArray.getResourceId(R.styleable.ColorsPalette_hintsPalette, R.array.hints_palette)
    }

    fun getFieldActive(): Int = fieldActive
    fun getFieldBlank(): Int = fieldBlank
    fun getFieldFull(): Int = fieldFull
    fun getFieldSelector(): Int = fieldSelector
    fun getColorsPalette(): Int = colorPalette
    fun getHintsPalette(): Int = hintsPalette
}
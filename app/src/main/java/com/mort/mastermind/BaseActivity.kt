package com.mort.mastermind

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import java.util.Locale

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setAppLocale()
        setAppTheme()
        setFieldsStyle()
        setColorPalette()
        super.onCreate(savedInstanceState)
    }

    override fun onRestart() {
        super.onRestart()
        this.finish()
        startActivity(intent)
    }

    private fun setAppLocale() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val locale = Locale(sharedPrefs.getString(Constants.LOCALE, "en") ?: "en")

        Locale.setDefault(locale)
        val config = baseContext.resources.configuration
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
    }

    private fun setAppTheme() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val themeValue = sharedPrefs.getString(Constants.THEME, "1")?.toInt() ?: 1

        when (themeValue) {
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            3 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

    }

    private fun setFieldsStyle() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val fieldsStyle = when (sharedPrefs.getString(Constants.FIELDS_STYLE, "1")) {
            "1" -> R.style.circleFields
            "2" -> R.style.squareFields
            "3" -> R.style.pandaFields
            else -> R.style.circleFields
        }

        theme.applyStyle(fieldsStyle, true)
    }

    private fun setColorPalette() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val fieldsStyle = when (sharedPrefs.getString(Constants.COLOR_PALETTE, "1")) {
            "1" -> R.style.defaultColorPalette
            "2" -> R.style.pastelColorPalette
            "3" -> R.style.vibrantColorPalette
            else -> R.style.defaultColorPalette
        }

        theme.applyStyle(fieldsStyle, true)
    }

}
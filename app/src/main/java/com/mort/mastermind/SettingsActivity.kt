package com.mort.mastermind

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SeekBarPreference
import com.google.android.material.snackbar.Snackbar

class SettingsActivity : BaseActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPref.registerOnSharedPreferenceChangeListener(this)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            updatePracticeAutofillVisibility()
        }

        fun updateSeekBarPreference(key: String, value: Int) {
            val seekBarPreference = findPreference<SeekBarPreference>(key)
            seekBarPreference?.value = value
        }

        private fun updatePracticeAutofillVisibility() {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val prefsPractice = sharedPref.getBoolean(Constants.PRACTICE_MODE, false)
            val practiceAutofillPref = findPreference<Preference>(Constants.PRACTICE_AUTOFILL)

            practiceAutofillPref?.isVisible = prefsPractice
        }
    }

    override fun onSharedPreferenceChanged(sharedPref: SharedPreferences?, key: String?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.settings) as? SettingsFragment
        val editor = sharedPref?.edit()

        if (key == Constants.COL_AMOUNT || key == Constants.COLOR_AMOUNT || key == Constants.UNIQUE_COLORS) {
            val prefsUnique = sharedPref?.getBoolean(Constants.UNIQUE_COLORS, true)

            if (prefsUnique == true) {
                val prefsColumns = sharedPref.getInt(Constants.COL_AMOUNT, 4)
                val prefsColors = sharedPref.getInt(Constants.COLOR_AMOUNT, 3)

                if (prefsColors < prefsColumns) {
                    editor?.putInt(Constants.COL_AMOUNT, prefsColors)
                    editor?.apply()

                    fragment?.updateSeekBarPreference(Constants.COL_AMOUNT, prefsColors)

                    val snackbar = Snackbar.make(findViewById(R.id.settings), R.string.options_col_warning, Snackbar.LENGTH_LONG)
                    snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 3
                    snackbar.show()
                }

            }

        }

        if (key == Constants.PRACTICE_MODE) {
            val prefsPractice = sharedPref?.getBoolean(Constants.PRACTICE_MODE, false)
            val practiceAutofillPref = fragment?.findPreference<Preference>(Constants.PRACTICE_AUTOFILL)

            if (prefsPractice == false) {
                practiceAutofillPref?.isVisible = false
            } else {
                practiceAutofillPref?.isVisible = true
            }
        }

        if (key == Constants.THEME) {
            val prefs = sharedPref?.getString(key, "1")

            when (prefs?.toInt()) {
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editor?.putString(Constants.THEME, "1")
                    editor?.apply()
                }

                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editor?.putString(Constants.THEME, "2")
                    editor?.apply()
                }

                3 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    editor?.putString(Constants.THEME, "3")
                    editor?.apply()
                }
            }
        }

        if (key == Constants.FIELDS_STYLE) {
            val prefs = sharedPref?.getString(key, "1")

            when(prefs?.toInt()) {
                1 -> {
                    editor?.putString(Constants.FIELDS_STYLE, "1")
                    editor?.apply()
                }
                2 -> {
                    editor?.putString(Constants.FIELDS_STYLE, "2")
                    editor?.apply()
                }
            }
        }

        if (key == Constants.COLOR_PALETTE) {
            val prefs = sharedPref?.getString(key, "1")

            when(prefs?.toInt()) {
                1 -> {
                    editor?.putString(Constants.COLOR_PALETTE, "1")
                    editor?.apply()
                }
                2 -> {
                    editor?.putString(Constants.COLOR_PALETTE, "2")
                    editor?.apply()
                }
                3 -> {
                    editor?.putString(Constants.COLOR_PALETTE, "3")
                    editor?.apply()
                }
            }
        }

        if (key == Constants.LOCALE) {
            val prefs = sharedPref?.getString(key, "en")
            editor?.putString(Constants.LOCALE, prefs)
            editor?.commit()

            this.recreate()
        }
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }
}
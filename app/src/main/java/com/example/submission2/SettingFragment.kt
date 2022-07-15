package com.example.submission2

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import submission2.R

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
class SettingFragment : PreferenceFragmentCompat() {

    private lateinit var themePreference: SwitchPreferenceCompat
    private lateinit var settingViewModel: SettingsViewModel
    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        setPreferencesFromResource(R.xml.setting, s)

        val pref = SettingPreference.getInstance(requireContext().dataStore)
        settingViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SettingsViewModel::class.java]
        themePreference = findPreference(resources.getString(R.string.theme))!!

        themePreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, _ ->
                if (themePreference.isChecked) {
                    settingViewModel.saveThemeSetting(false)
                    themePreference.isChecked = false
                } else {
                    settingViewModel.saveThemeSetting(true)
                    themePreference.isChecked = true
                }
                false
            }
        settingViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })
    }
}


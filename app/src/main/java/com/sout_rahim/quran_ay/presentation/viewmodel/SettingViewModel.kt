package com.sout_rahim.quran_ay.presentation.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sout_rahim.quran_ay.data.model.Language
import com.sout_rahim.quran_ay.domain.usecase.GetAvailableLanguagesUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetCurrentLanguageUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetDarkModeUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetFontSizeUseCase
import com.sout_rahim.quran_ay.domain.usecase.SaveDarkModeUseCase
import com.sout_rahim.quran_ay.domain.usecase.SaveFontSizeUseCase
import com.sout_rahim.quran_ay.domain.usecase.SaveLanguageUseCase
import kotlinx.coroutines.flow.*

class SettingViewModel(
    private val getAvailableLanguagesUseCase: GetAvailableLanguagesUseCase,
    private val getCurrentLanguageUseCase: GetCurrentLanguageUseCase,
    private val saveLanguageUseCase: SaveLanguageUseCase,
    private val getFontSizeUseCase: GetFontSizeUseCase,
    private val saveFontSizeUseCase: SaveFontSizeUseCase,
    private val saveDarkModeUseCase: SaveDarkModeUseCase,
    private val getDarkModeUseCase: GetDarkModeUseCase
) : ViewModel() {

    // LiveData for available languages
    private val _availableLanguages = MutableLiveData<List<Language>>()
    val availableLanguages: LiveData<List<Language>> = _availableLanguages

    // LiveData for the current language
    private val _currentLanguage = MutableLiveData<String>()
    val currentLanguage: LiveData<String> = _currentLanguage

    // LiveData for font size
    private val _fontSize = MutableLiveData<Float>()
    val fontSize: LiveData<Float> = _fontSize

    // LiveData for dark mode setting
    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> = _isDarkMode


    // Initialization: load all settings
    init {
        loadSettings()
    }

    // Load settings using the use cases
    fun loadSettings() {
        // Load available languages
        _availableLanguages.value = getAvailableLanguagesUseCase()

        // Load current language
        _currentLanguage.value = getCurrentLanguageUseCase()

        // Load font size
        _fontSize.value = getFontSizeUseCase()

        // Load dark mode setting
        _isDarkMode.value = getDarkModeUseCase()
    }

    // Toggle Dark Mode and save preference
    fun toggleDarkMode(isChecked: Boolean) {
        saveDarkModeUseCase(isChecked)
        _isDarkMode.value = isChecked
        // Apply theme based on the state
        AppCompatDelegate.setDefaultNightMode(
            if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }


    // Set the language (and update LiveData)
    fun setLanguage(langCode: String) {
        saveLanguageUseCase(langCode)  // Save the selected language
        _currentLanguage.value = langCode  // Update the current language LiveData
    }

    // Set the font size (and update LiveData)
    fun setFontSize(fontSize: Float) {
        saveFontSizeUseCase(fontSize)  // Save the selected font size
        _fontSize.value = fontSize  // Update the font size LiveData
    }

    // Set the dark mode (and update LiveData)
    fun setDarkMode(isDarkMode: Boolean) {
        saveDarkModeUseCase(isDarkMode)  // Save the dark mode preference
        _isDarkMode.value = isDarkMode  // Update the dark mode LiveData
    }

    // Add a function to directly get available languages (e.g., for a different use case)
    fun getAvailableLanguages(): List<Language> {
        return getAvailableLanguagesUseCase()
    }

    // Add a function to directly get the current language (e.g., for a different use case)
    fun getCurrentLanguage(): String {
        return getCurrentLanguageUseCase()
    }

    // Add a function to directly get the font size (e.g., for a different use case)
    fun getFontSize(): Float {
        return getFontSizeUseCase()
    }

    // Optional: You can expose these directly for UI consumption
    fun getCurrentLanguageAsFlow(): Flow<String> = flow {
        emit(getCurrentLanguageUseCase())
    }

    fun getFontSizeAsFlow(): Flow<Float> = flow {
        emit(getFontSizeUseCase())
    }

    // Optional: Add functions for updating all settings at once if needed
    fun updateLanguageAndFontSize(langCode: String, fontSize: Float) {
        setLanguage(langCode)
        setFontSize(fontSize)
    }

    // Optional: You can also add methods for resetting settings if required
    fun resetSettingsToDefault() {
        // Example reset logic: you can provide default values or call use cases to reset
        setLanguage("en") // Default to English
        setFontSize(22f)  // Default font size
        setDarkMode(false) // Default to light mode
    }

    fun getDarkMode(): Boolean {
        return getDarkModeUseCase()
    }

    // Optional: You can expose dark mode as a Flow for better handling in the UI layer
    fun getDarkModeAsFlow(): Flow<Boolean> = flow {
        emit(getDarkModeUseCase())
    }
}

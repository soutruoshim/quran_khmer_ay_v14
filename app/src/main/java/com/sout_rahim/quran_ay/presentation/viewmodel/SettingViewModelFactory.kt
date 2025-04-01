package com.sout_rahim.quran_ay.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sout_rahim.quran_ay.domain.usecase.GetAvailableLanguagesUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetCurrentLanguageUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetDarkModeUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetFontSizeUseCase
import com.sout_rahim.quran_ay.domain.usecase.SaveDarkModeUseCase
import com.sout_rahim.quran_ay.domain.usecase.SaveFontSizeUseCase
import com.sout_rahim.quran_ay.domain.usecase.SaveLanguageUseCase

class SettingViewModelFactory(
    private val getAvailableLanguagesUseCase: GetAvailableLanguagesUseCase,
    private val getCurrentLanguageUseCase: GetCurrentLanguageUseCase,
    private val saveLanguageUseCase: SaveLanguageUseCase,
    private val getFontSizeUseCase: GetFontSizeUseCase,
    private val saveFontSizeUseCase: SaveFontSizeUseCase,
    private val saveDarkModeUseCase: SaveDarkModeUseCase,
    private val getDarkModeUseCase: GetDarkModeUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(
                getAvailableLanguagesUseCase,
                getCurrentLanguageUseCase,
                saveLanguageUseCase,
                getFontSizeUseCase,
                saveFontSizeUseCase,
                saveDarkModeUseCase,
                getDarkModeUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
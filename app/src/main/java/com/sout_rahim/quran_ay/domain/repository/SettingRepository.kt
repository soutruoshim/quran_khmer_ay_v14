package com.sout_rahim.quran_ay.domain.repository

import com.sout_rahim.quran_ay.data.model.Language

interface SettingRepository {
    fun getLanguages(): List<Language>
    fun saveLanguage(lang: String)
    fun getCurrentLanguage(): String
    fun saveFontSize(fontSize: Float)
    fun getFontSize(): Float
    fun saveDarkMode(isDarkMode: Boolean)
    fun getDarkMode(): Boolean
}
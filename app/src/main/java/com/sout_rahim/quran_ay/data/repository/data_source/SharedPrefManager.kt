package com.sout_rahim.quran_ay.data.repository.data_source
import com.sout_rahim.quran_ay.data.model.Language

interface SharedPrefManager {
    fun getLanguages(): List<Language>
    fun saveLanguage(lang: String)
    fun getCurrentLanguage(): String
    fun saveFontSize(fontSize: Float)
    fun getFontSize(): Float
    fun saveDarkMode(isDarkMode: Boolean)
    fun getDarkMode(): Boolean
}
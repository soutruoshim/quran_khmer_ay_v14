package com.sout_rahim.quran_ay.data.repository
import com.sout_rahim.quran_ay.data.model.Language
import com.sout_rahim.quran_ay.data.repository.data_source.SharedPrefManager
import com.sout_rahim.quran_ay.domain.repository.SettingRepository

class SettingRepositoryImpl(
    private val sharedPrefManager: SharedPrefManager
) : SettingRepository {

    override fun getLanguages(): List<Language> {
        // Delegate the call to SharedPrefManager to fetch available languages
        return sharedPrefManager.getLanguages()
    }

    override fun saveLanguage(lang: String) {
        // Delegate the call to SharedPrefManager to save the language
        sharedPrefManager.saveLanguage(lang)
    }

    override fun getCurrentLanguage(): String {
        // Delegate the call to SharedPrefManager to get the current language
        return sharedPrefManager.getCurrentLanguage()
    }

    override fun saveFontSize(fontSize: Float) {
        // Delegate the call to SharedPrefManager to save the font size
        sharedPrefManager.saveFontSize(fontSize)
    }

    override fun getFontSize(): Float {
        // Delegate the call to SharedPrefManager to get the font size
        return sharedPrefManager.getFontSize()
    }

    // New methods for dark mode
    override fun saveDarkMode(isDarkMode: Boolean) {
        // Delegate the call to SharedPrefManager to save the dark mode setting
        sharedPrefManager.saveDarkMode(isDarkMode)
    }

    override fun getDarkMode(): Boolean {
        // Delegate the call to SharedPrefManager to get the current dark mode setting
        return sharedPrefManager.getDarkMode()
    }
}

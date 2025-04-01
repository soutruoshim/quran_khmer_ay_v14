package com.sout_rahim.quran_ay.data.repository.data_source_impl

import android.content.Context
import android.content.SharedPreferences
import com.sout_rahim.quran_ay.data.model.Language
import com.sout_rahim.quran_ay.data.repository.data_source.SharedPrefManager

class SharedPrefManagerImpl(context: Context) : SharedPrefManager {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    override fun getLanguages(): List<Language> {
        // You can customize the list of available languages as needed
        return listOf(
            Language("en", "English"),
            Language("ar", "العربية"),
            Language("km", "ភាសាខ្មែរ")
        )
    }

    override fun saveLanguage(lang: String) {
        prefs.edit().putString("language", lang).apply()
    }

    override fun getCurrentLanguage(): String {
        return prefs.getString("language", "en") ?: "en"  // Default to "en"
    }

    override fun saveFontSize(fontSize: Float) {
        prefs.edit().putFloat("font_size", fontSize).apply()
    }

    override fun getFontSize(): Float {
        return prefs.getFloat("font_size", 22f) // Default font size is 14f
    }

    // Save the dark mode preference
    override fun saveDarkMode(isDarkMode: Boolean) {
        prefs.edit().putBoolean("isDarkMode", isDarkMode).apply()
    }

    // Get the saved dark mode preference, defaulting to false (light mode)
    override fun getDarkMode(): Boolean {
        return prefs.getBoolean("isDarkMode", false)  // Default is light mode (false)
    }
}

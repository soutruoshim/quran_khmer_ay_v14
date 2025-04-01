package com.sout_rahim.quran_ay.domain.usecase

import com.sout_rahim.quran_ay.domain.repository.SettingRepository

class SaveDarkModeUseCase(private val repository: SettingRepository) {
    operator fun invoke(isDarkMode: Boolean) {
        // Save the dark mode setting in the repository
        repository.saveDarkMode(isDarkMode)
    }
}
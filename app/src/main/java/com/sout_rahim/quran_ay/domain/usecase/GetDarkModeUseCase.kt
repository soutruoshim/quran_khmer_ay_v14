package com.sout_rahim.quran_ay.domain.usecase

import com.sout_rahim.quran_ay.domain.repository.SettingRepository

class GetDarkModeUseCase(private val repository: SettingRepository) {
    operator fun invoke(): Boolean {
        // Get the current dark mode setting from the repository
        return repository.getDarkMode()
    }
}
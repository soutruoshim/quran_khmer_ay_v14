package com.sout_rahim.quran_ay.domain.usecase

import com.sout_rahim.quran_ay.domain.repository.SettingRepository

class GetAvailableLanguagesUseCase(private val repository: SettingRepository) {
    operator fun invoke() = repository.getLanguages()
}
package com.sout_rahim.quran_ay.domain.usecase

import com.sout_rahim.quran_ay.domain.repository.SettingRepository

class SaveFontSizeUseCase(private val repository: SettingRepository) {
    operator fun invoke(fontSize: Float) {
        repository.saveFontSize(fontSize)
    }
}
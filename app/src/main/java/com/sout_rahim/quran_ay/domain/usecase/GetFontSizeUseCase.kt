package com.sout_rahim.quran_ay.domain.usecase

import com.sout_rahim.quran_ay.domain.repository.SettingRepository

class GetFontSizeUseCase(private val repository: SettingRepository) {
    operator fun invoke() = repository.getFontSize()
}
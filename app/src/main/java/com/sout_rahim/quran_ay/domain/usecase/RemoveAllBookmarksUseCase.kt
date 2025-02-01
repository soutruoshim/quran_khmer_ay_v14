package com.sout_rahim.quran_ay.domain.usecase

import com.sout_rahim.quran_ay.domain.repository.QuranRepository

class RemoveAllBookmarksUseCase(private val repository: QuranRepository) {
    suspend operator fun invoke() {
        repository.removeAllBookmarks()
    }
}
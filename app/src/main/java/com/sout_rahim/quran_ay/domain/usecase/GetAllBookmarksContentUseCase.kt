package com.sout_rahim.quran_ay.domain.usecase

import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.domain.repository.QuranRepository
import kotlinx.coroutines.flow.Flow

class GetAllBookmarksContentUseCase(private val repository: QuranRepository) {
    operator fun invoke(): Flow<List<SurahContentItem>> {
        return repository.getAllBookmarksAyahContent()
    }
}
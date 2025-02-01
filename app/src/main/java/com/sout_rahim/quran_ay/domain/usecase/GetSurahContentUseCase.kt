package com.sout_rahim.quran_ay.domain.usecase

import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.domain.repository.QuranRepository
import kotlinx.coroutines.flow.Flow

class GetSurahContentUseCase(private val repository: QuranRepository) {
    operator fun invoke(surahId: Int): Flow<List<SurahContentItem>> {
        return repository.getSurahContent(surahId)
    }
}
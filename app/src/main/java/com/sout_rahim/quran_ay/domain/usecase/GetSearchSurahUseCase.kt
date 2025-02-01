package com.sout_rahim.quran_ay.domain.usecase

import com.sout_rahim.quran_ay.data.model.SurahItem
import com.sout_rahim.quran_ay.domain.repository.QuranRepository
import kotlinx.coroutines.flow.Flow

class GetSearchSurahUseCase(private val repository: QuranRepository) {
    operator fun invoke(query: String): Flow<List<SurahItem>> {
        return repository.searchSurahs(query)
    }
}
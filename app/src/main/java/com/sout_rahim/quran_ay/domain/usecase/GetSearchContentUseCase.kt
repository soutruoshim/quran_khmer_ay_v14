package com.sout_rahim.quran_ay.domain.usecase

import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.domain.repository.QuranRepository
import kotlinx.coroutines.flow.Flow

class GetSearchContentUseCase(private val repository: QuranRepository) {
    operator fun invoke(query: String): Flow<List<SurahContentItem>> {
        return repository.searchContent(query)
    }
}
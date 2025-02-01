package com.sout_rahim.quran_ay.domain.usecase

import com.sout_rahim.quran_ay.data.model.FavoriteItem
import com.sout_rahim.quran_ay.domain.repository.QuranRepository
import kotlinx.coroutines.flow.Flow

class GetAllBookmarksUseCase(private val repository: QuranRepository) {
    operator fun invoke(): Flow<List<FavoriteItem>> {
        return repository.getAllBookmarks()
    }
}
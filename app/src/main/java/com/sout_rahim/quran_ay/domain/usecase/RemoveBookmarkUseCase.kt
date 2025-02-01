package com.sout_rahim.quran_ay.domain.usecase

import com.sout_rahim.quran_ay.data.model.FavoriteItem
import com.sout_rahim.quran_ay.domain.repository.QuranRepository

class RemoveBookmarkUseCase(private val repository: QuranRepository) {
    suspend operator fun invoke(favoriteItem: FavoriteItem) {
        repository.removeBookmark(favoriteItem)
    }
}
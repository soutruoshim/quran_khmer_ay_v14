package com.sout_rahim.quran_ay.data.repository

import com.sout_rahim.quran_ay.data.model.FavoriteItem
import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.data.model.SurahItem
import com.sout_rahim.quran_ay.domain.repository.QuranRepository
import kotlinx.coroutines.flow.Flow

class QuranRepositoryImpl: QuranRepository {
    override fun getAllSurahs(): Flow<List<SurahItem>> {
        TODO("Not yet implemented")
    }

    override fun searchSurahs(query: String): Flow<List<SurahItem>> {
        TODO("Not yet implemented")
    }

    override fun getSurahContent(surahId: Int): Flow<List<SurahContentItem>> {
        TODO("Not yet implemented")
    }

    override fun searchContent(query: String): Flow<List<SurahContentItem>> {
        TODO("Not yet implemented")
    }

    override fun getAyahByIndex(surahId: Int, verseId: Int): Flow<SurahContentItem> {
        TODO("Not yet implemented")
    }

    override suspend fun addBookmark(favoriteItem: FavoriteItem) {
        TODO("Not yet implemented")
    }

    override suspend fun removeBookmark(favoriteItem: FavoriteItem) {
        TODO("Not yet implemented")
    }

    override suspend fun removeAllBookmarks() {
        TODO("Not yet implemented")
    }

    override fun getAllBookmarks(): Flow<List<FavoriteItem>> {
        TODO("Not yet implemented")
    }
}
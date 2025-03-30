package com.sout_rahim.quran_ay.data.repository

import com.sout_rahim.quran_ay.data.model.FavoriteItem
import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.data.model.SurahItem
import com.sout_rahim.quran_ay.data.repository.data_source.QuranLocalDataSource
import com.sout_rahim.quran_ay.domain.repository.QuranRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class QuranRepositoryImpl(
    private val localDataSource: QuranLocalDataSource
): QuranRepository {
    override fun getAllSurahs(): Flow<List<SurahItem>> {
        return localDataSource.getAllSurahs()
    }

    override fun searchSurahs(query: String): Flow<List<SurahItem>> {
        return localDataSource.searchSurahs(query)
    }

    override fun getSurahContent(surahId: Int): Flow<List<SurahContentItem>> {
        return localDataSource.getSurahContent(surahId)
    }

    override fun searchContent(query: String): Flow<List<SurahContentItem>> {
        return localDataSource.searchContent(query)
    }

    override fun getAyahByIndex(surahId: Int, verseId: Int): Flow<SurahContentItem> {
        return localDataSource.getAyahByIndex(surahId, verseId)
            .filterNotNull() // Ensures the Flow does not emit null values
    }

    override suspend fun addBookmark(favoriteItem: FavoriteItem) {
        localDataSource.addBookmark(favoriteItem)
    }

    override suspend fun removeBookmark(favoriteItem: FavoriteItem) {
        localDataSource.removeBookmark(favoriteItem)
    }

    override suspend fun removeAllBookmarks() {
        localDataSource.removeAllBookmarks()
    }

    override fun getAllBookmarks(): Flow<List<FavoriteItem>> {
        return localDataSource.getAllBookmarks()
    }

    override fun getAllBookmarksAyahContent(): Flow<List<SurahContentItem>> {
        return localDataSource.getAllBookmarksAyahContent()
    }
}
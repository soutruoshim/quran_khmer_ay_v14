package com.sout_rahim.quran_ay.data.repository.data_source_impl

import com.sout_rahim.quran_ay.data.db.FavoriteDAO
import com.sout_rahim.quran_ay.data.db.QuranAyahDAO
import com.sout_rahim.quran_ay.data.db.QuranSuraDAO
import com.sout_rahim.quran_ay.data.model.FavoriteItem
import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.data.model.SurahItem
import com.sout_rahim.quran_ay.data.repository.data_source.QuranLocalDataSource
import kotlinx.coroutines.flow.Flow

class QuranLocalDataSourceImpl(
    private val quranAyahDAO: QuranAyahDAO,
    private val quranSuraDAO: QuranSuraDAO,
    private val favoriteDAO: FavoriteDAO
) : QuranLocalDataSource {

    // Fetch all Surahs
    override fun getAllSurahs(): Flow<List<SurahItem>> {
        return quranSuraDAO.getAllSurahs()
    }

    // Search Surahs by name or other criteria
    override fun searchSurahs(query: String): Flow<List<SurahItem>> {
        return quranSuraDAO.searchSurah(null, query)
    }

    // Fetch content (Ayahs) of a specific Surah
    override fun getSurahContent(surahId: Int): Flow<List<SurahContentItem>> {
        return quranAyahDAO.getSurahContent(surahId)
    }

    // Search for a specific Ayah or phrase in Ayahs
    override fun searchContent(query: String): Flow<List<SurahContentItem>> {
        return quranAyahDAO.searchContent(query)
    }

    // Get a specific Ayah using Surah ID and Verse ID
    override fun getAyahByIndex(surahId: Int, verseId: Int): Flow<SurahContentItem?> {
        return quranAyahDAO.getAyahByIndex(verseId)
    }

    // Bookmark Management

    override suspend fun addBookmark(favoriteItem: FavoriteItem) {
        favoriteDAO.addBookmark(favoriteItem)
    }

    override suspend fun removeBookmark(favoriteItem: FavoriteItem) {
        favoriteDAO.removeBookmark(favoriteItem)
    }

    override suspend fun removeAllBookmarks() {
        favoriteDAO.removeAllBookmarks()
    }

    override fun getAllBookmarks(): Flow<List<FavoriteItem>> {
        return favoriteDAO.getAllBookmarks()
    }
}


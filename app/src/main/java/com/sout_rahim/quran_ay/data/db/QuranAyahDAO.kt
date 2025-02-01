package com.sout_rahim.quran_ay.data.db

import androidx.room.Dao
import androidx.room.Query
import com.sout_rahim.quran_ay.data.model.SurahContentItem
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranAyahDAO {
    // Fetch all Ayahs of a specific Surah by Surah ID
    @Query("SELECT * FROM Quran_Ayah WHERE SuraID = :surahId")
    fun getSurahContent(surahId: Int): Flow<List<SurahContentItem>>

    // Search content by Ayah text, Khmer translation, or Surah name
    @Query("SELECT * FROM Quran_Ayah WHERE AyahText LIKE '%' || :query || '%' OR AyahTextKhmer LIKE '%' || :query || '%' OR SurahName LIKE '%' || :query || '%'")
    fun searchContent(query: String): Flow<List<SurahContentItem>>

    // Get a specific Ayah by its unique index (VerseID)
    @Query("SELECT * FROM Quran_Ayah WHERE VerseID = :verseId")
    fun getAyahByIndex(verseId: Int): Flow<SurahContentItem?>
}

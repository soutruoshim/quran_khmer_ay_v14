package com.sout_rahim.quran_ay.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sout_rahim.quran_ay.data.model.FavoriteItem
import com.sout_rahim.quran_ay.data.model.SurahContentItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDAO {
    /**
     *  Insert a new favorite Ayah
     *  If the same Ayah exists, replace it.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(favoriteItem: FavoriteItem)

    /**
     * Remove a specific favorite Ayah
     */
    @Delete
    suspend fun removeBookmark(favoriteItem: FavoriteItem)

    /**
     * Remove all favorite Ayahs
     */
    @Query("DELETE FROM Favorite")
    suspend fun removeAllBookmarks()

    /**
     * Get all bookmarked Ayahs
     */
    @Query("SELECT * FROM Favorite")
    fun getAllBookmarks(): Flow<List<FavoriteItem>>

    /**
     * Get the SurahContentItem based on SuraID and VerseID from a FavoriteItem
     */
    @Query("SELECT * FROM Quran_Ayah WHERE SuraID = :suraId AND VerseID = :verseId")
    fun getSurahContentByFavorite(suraId: Int, verseId: Int): Flow<SurahContentItem>

    /**
     * Get all SurahContentItems that are bookmarked
     * This will join `Favorite` with `Quran_Ayah` using `SuraID` and `VerseID`
     */
    @Query("""
        SELECT Quran_Ayah.* 
        FROM Quran_Ayah
        INNER JOIN Favorite ON Favorite.SuraID = Quran_Ayah.SuraID 
                            AND Favorite.VerseID = Quran_Ayah.VerseID
    """)
    fun getAllSurahContentsFromFavorites(): Flow<List<SurahContentItem>>
}
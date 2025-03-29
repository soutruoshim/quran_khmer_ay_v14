package com.sout_rahim.quran_ay.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "Favorite"
)
data class FavoriteItem(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var SuraID: Int?,
    var VerseID: Int?,
): Serializable {
    companion object {
        fun fromSurahContentItem(surahContentItem: SurahContentItem): FavoriteItem {
            return FavoriteItem(
                id = surahContentItem.ID,               // Make sure SurahContentItem has `id`
                SuraID = surahContentItem.SuraID,       // Match the correct field name
                VerseID = surahContentItem.VerseID      // Match the correct field name
            )
        }
    }
}


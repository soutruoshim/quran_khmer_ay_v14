package com.sout_rahim.quran_ay.data.model

import androidx.room.Entity
import java.io.Serializable

@Entity(
    tableName = "Favorite"
)
data class FavoriteItem(
    var SuraID: Int?,
    var VerseID: Int?,
    var id: Int?
): Serializable


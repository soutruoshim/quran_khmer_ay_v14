package com.sout_rahim.quran_ay.data.model

import androidx.room.Entity
import java.io.Serializable

@Entity(
    tableName = "Quran_Ayah"
)
data class SurahContentItem(
    var AyahNormal: String?,
    var AyahText: String?,
    var AyahTextKhmer: String?,
    var ID: Int?,
    var SuraID: Int?,
    var SurahName: String?,
    var VerseID: Int?
): Serializable
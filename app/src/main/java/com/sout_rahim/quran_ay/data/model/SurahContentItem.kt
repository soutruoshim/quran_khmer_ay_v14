package com.sout_rahim.quran_ay.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "Quran_Ayah"
)
data class SurahContentItem(
    @PrimaryKey(autoGenerate = false)
    var ID: Int,
    var AyahNormal: String?,
    var AyahText: String?,
    var AyahTextKhmer: String?,
    var SuraID: Int?,
    var SurahName: String?,
    var VerseID: Int?
): Serializable
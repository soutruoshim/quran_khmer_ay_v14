package com.sout_rahim.quran_ay.data.model

import androidx.room.Entity
import java.io.Serializable

@Entity(
    tableName = "QuranSura"
)
data class SurahItem(
    var NAyah: Int?,
    var id: Int?,
    var name: String?,
    var name_symbol: String?,
    var type: String?
): Serializable
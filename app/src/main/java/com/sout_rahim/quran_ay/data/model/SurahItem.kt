package com.sout_rahim.quran_ay.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "QuranSura"
)
data class SurahItem(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,  // Allow nullable id
    val NAyah: Int?,  // Allow nullable NAyah
    val name: String?,  // Allow nullable fields
    val name_symbol: String?,
    val type: String?
): Serializable

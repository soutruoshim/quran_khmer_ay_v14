package com.sout_rahim.quran_ay.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sout_rahim.quran_ay.data.model.FavoriteItem
import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.data.model.SurahItem

@Database(
    entities = [SurahItem::class, SurahContentItem::class, FavoriteItem::class],
    version = 1,
    exportSchema = false
)
abstract class QuranDatabase: RoomDatabase() {
    // DAOs
    abstract fun getQuranSuraDAO(): QuranSuraDAO
    abstract fun getQuranAyahDAO(): QuranAyahDAO
    abstract fun getFavoriteDAO(): FavoriteDAO
}
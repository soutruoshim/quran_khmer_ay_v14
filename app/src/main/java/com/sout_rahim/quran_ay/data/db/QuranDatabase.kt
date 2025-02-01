package com.sout_rahim.quran_ay.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sout_rahim.quran_ay.data.model.FavoriteItem
import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.data.model.SurahItem

@Database(
    entities = [SurahItem::class, SurahContentItem::class, FavoriteItem::class],
    version = 2,
    exportSchema = false
)
abstract class QuranDatabase: RoomDatabase() {
    // DAOs
    abstract fun getQuranSuraDAO(): QuranSuraDAO
    abstract fun getQuranAyahDAO(): QuranAyahDAO
    abstract fun getFavoriteDAO(): FavoriteDAO

    companion object {
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Ensure the schema in Room matches your pre-existing SQLite database
                database.execSQL("CREATE TABLE IF NOT EXISTS QuranSura_new (" +
                        "id INTEGER PRIMARY KEY, " +
                        "NAyah INTEGER, " +
                        "name TEXT, " +
                        "name_symbol TEXT, " +
                        "type TEXT)")

                // Copy data from the old table to the new one
                database.execSQL("INSERT INTO QuranSura_new (id, NAyah, name, name_symbol, type) " +
                        "SELECT id, NAyah, name, name_symbol, type FROM QuranSura")

                // Drop the old table and rename the new one
                database.execSQL("DROP TABLE QuranSura")
                database.execSQL("ALTER TABLE QuranSura_new RENAME TO QuranSura")
            }
        }
    }
}
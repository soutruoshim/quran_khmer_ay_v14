package com.sout_rahim.quran_ay.presentation.di

import android.app.Application
import androidx.room.Room
import com.sout_rahim.quran_ay.data.db.FavoriteDAO
import com.sout_rahim.quran_ay.data.db.QuranAyahDAO
import com.sout_rahim.quran_ay.data.db.QuranDatabase
import com.sout_rahim.quran_ay.data.db.QuranSuraDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Singleton
    @Provides
    fun provideQuranDatabase(app: Application): QuranDatabase {
        return Room.databaseBuilder(app, QuranDatabase::class.java, "qurans_ay")
            .createFromAsset("databases/qurans_ay.db")
            .addMigrations(QuranDatabase.MIGRATION_4_5) // Apply migration from version 4 to 5
            //.fallbackToDestructiveMigration()
            .build()
    }
    @Singleton
    @Provides
    fun provideFavoriteDAO(database: QuranDatabase): FavoriteDAO {
        return database.getFavoriteDAO()
    }

    @Singleton
    @Provides
    fun provideQuranAyahDAO(database: QuranDatabase): QuranAyahDAO {
        return database.getQuranAyahDAO()
    }

    @Singleton
    @Provides
    fun provideQuranSuraDAO(database: QuranDatabase): QuranSuraDAO {
        return database.getQuranSuraDAO()
    }

}

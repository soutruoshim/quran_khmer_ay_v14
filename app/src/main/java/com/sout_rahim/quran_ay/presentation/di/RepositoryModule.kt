package com.sout_rahim.quran_ay.presentation.di

import com.sout_rahim.quran_ay.data.repository.QuranRepositoryImpl
import com.sout_rahim.quran_ay.data.repository.data_source.QuranLocalDataSource
import com.sout_rahim.quran_ay.domain.repository.QuranRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideQuranRepository(
        quranLocalDataSource: QuranLocalDataSource
    ): QuranRepository {
        return QuranRepositoryImpl(quranLocalDataSource)
    }

}
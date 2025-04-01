package com.sout_rahim.quran_ay.presentation.di

import android.app.Application
import com.sout_rahim.quran_ay.data.repository.data_source.SharedPrefManager
import com.sout_rahim.quran_ay.data.repository.data_source_impl.SharedPrefManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {
    @Singleton
    @Provides
    fun provideSharedPrefManager(app: Application): SharedPrefManager {
        return SharedPrefManagerImpl(app)
    }
}
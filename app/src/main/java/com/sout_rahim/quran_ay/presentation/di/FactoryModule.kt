package com.sout_rahim.quran_ay.presentation.di

import com.sout_rahim.quran_ay.domain.usecase.AddBookmarkUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetAllBookmarksContentUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetAllBookmarksUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetAllSurahsUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetAyahByIndexUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetSearchContentUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetSearchSurahUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetSurahContentUseCase
import com.sout_rahim.quran_ay.domain.usecase.RemoveAllBookmarksUseCase
import com.sout_rahim.quran_ay.domain.usecase.RemoveBookmarkUseCase
import com.sout_rahim.quran_ay.presentation.viewmodel.QuranViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FactoryModule {

    @Singleton
    @Provides
    fun provideQuranViewModelFactory(
        getAllSurahsUseCase: GetAllSurahsUseCase,
        getSearchSurahUseCase: GetSearchSurahUseCase,
        getSurahContentUseCase: GetSurahContentUseCase,
        getSearchContentUseCase: GetSearchContentUseCase,
        getAyahByIndexUseCase: GetAyahByIndexUseCase,
        addBookmarkUseCase: AddBookmarkUseCase,
        getAllBookmarksUseCase: GetAllBookmarksUseCase,
        removeBookmarkUseCase: RemoveBookmarkUseCase,
        removeAllBookmarksUseCase: RemoveAllBookmarksUseCase,
        getAllBookmarksContentUseCase: GetAllBookmarksContentUseCase
    ): QuranViewModelFactory {
        return QuranViewModelFactory(
            getAllSurahsUseCase,
            getSearchSurahUseCase,
            getSurahContentUseCase,
            getSearchContentUseCase,
            getAyahByIndexUseCase,
            addBookmarkUseCase,
            getAllBookmarksUseCase,
            removeBookmarkUseCase,
            removeAllBookmarksUseCase,
            getAllBookmarksContentUseCase
        )
    }
}
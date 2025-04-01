package com.sout_rahim.quran_ay.presentation.di

import com.sout_rahim.quran_ay.domain.usecase.AddBookmarkUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetAllBookmarksContentUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetAllBookmarksUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetAllSurahsUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetAvailableLanguagesUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetAyahByIndexUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetCurrentLanguageUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetDarkModeUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetFontSizeUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetSearchContentUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetSearchSurahUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetSurahContentUseCase
import com.sout_rahim.quran_ay.domain.usecase.RemoveAllBookmarksUseCase
import com.sout_rahim.quran_ay.domain.usecase.RemoveBookmarkUseCase
import com.sout_rahim.quran_ay.domain.usecase.SaveDarkModeUseCase
import com.sout_rahim.quran_ay.domain.usecase.SaveFontSizeUseCase
import com.sout_rahim.quran_ay.domain.usecase.SaveLanguageUseCase
import com.sout_rahim.quran_ay.presentation.viewmodel.QuranViewModelFactory
import com.sout_rahim.quran_ay.presentation.viewmodel.SettingViewModelFactory
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

    @Singleton
    @Provides
    fun provideSettingViewModelFactory(
        getAvailableLanguagesUseCase: GetAvailableLanguagesUseCase,
        getCurrentLanguageUseCase: GetCurrentLanguageUseCase,
        saveLanguageUseCase: SaveLanguageUseCase,
        getFontSizeUseCase: GetFontSizeUseCase,
        saveFontSizeUseCase: SaveFontSizeUseCase,
        saveDarkModeUseCase: SaveDarkModeUseCase,
        getDarkModeUseCase: GetDarkModeUseCase
    ): SettingViewModelFactory {
        return SettingViewModelFactory(
            getAvailableLanguagesUseCase,
            getCurrentLanguageUseCase,
            saveLanguageUseCase,
            getFontSizeUseCase,
            saveFontSizeUseCase,
            saveDarkModeUseCase,
            getDarkModeUseCase
        )
    }
}
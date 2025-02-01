package com.sout_rahim.quran_ay.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.sout_rahim.quran_ay.data.model.FavoriteItem
import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.data.model.SurahItem
import com.sout_rahim.quran_ay.domain.usecase.AddBookmarkUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetAllBookmarksUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetAllSurahsUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetAyahByIndexUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetSearchContentUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetSearchSurahUseCase
import com.sout_rahim.quran_ay.domain.usecase.GetSurahContentUseCase
import com.sout_rahim.quran_ay.domain.usecase.RemoveAllBookmarksUseCase
import com.sout_rahim.quran_ay.domain.usecase.RemoveBookmarkUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

class QuranViewModel(
    private val getAllSurahsUseCase: GetAllSurahsUseCase,
    private val getSearchSurahUseCase: GetSearchSurahUseCase,
    private val getSurahContentUseCase: GetSurahContentUseCase,
    private val getSearchContentUseCase: GetSearchContentUseCase,
    private val getAyahByIndexUseCase: GetAyahByIndexUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val getAllBookmarksUseCase: GetAllBookmarksUseCase,
    private val removeBookmarkUseCase: RemoveBookmarkUseCase,
    private val removeAllBookmarksUseCase: RemoveAllBookmarksUseCase
) : ViewModel() {

    // State Flows for UI observation
    private val _surahs = MutableStateFlow<List<SurahItem>>(emptyList())
    val surahs: StateFlow<List<SurahItem>> = _surahs

    private val _searchResults = MutableStateFlow<List<SurahItem>>(emptyList())
    val searchResults: StateFlow<List<SurahItem>> = _searchResults

    private val _surahContent = MutableStateFlow<List<SurahContentItem>>(emptyList())
    val surahContent: StateFlow<List<SurahContentItem>> = _surahContent

    private val _searchContentResults = MutableStateFlow<List<SurahContentItem>>(emptyList())
    val searchContentResults: StateFlow<List<SurahContentItem>> = _searchContentResults

    private val _ayah = MutableStateFlow<SurahContentItem?>(null)
    val ayah: StateFlow<SurahContentItem?> = _ayah

    private val _bookmarks = MutableStateFlow<List<FavoriteItem>>(emptyList())
    val bookmarks: StateFlow<List<FavoriteItem>> = _bookmarks

    // Fetch all Surahs
    fun fetchAllSurahs() {
        viewModelScope.launch {
            getAllSurahsUseCase()
                .catch { e -> e.printStackTrace() } // Handle errors
                .collect { _surahs.value = it }
        }
    }

    // Search Surahs by query
    fun searchSurahs(query: String) {
        viewModelScope.launch {
            getSearchSurahUseCase(query)
                .catch { e -> e.printStackTrace() }
                .collect { _searchResults.value = it }
        }
    }

    // Fetch Surah content by Surah ID
    fun fetchSurahContent(surahId: Int) {
        viewModelScope.launch {
            getSurahContentUseCase(surahId)
                .catch { e -> e.printStackTrace() }
                .collect { _surahContent.value = it }
        }
    }

    // Search Ayahs (content) by query
    fun searchContent(query: String) {
        viewModelScope.launch {
            getSearchContentUseCase(query)
                .catch { e -> e.printStackTrace() }
                .collect { _searchContentResults.value = it }
        }
    }

    // Get a specific Ayah by Surah ID and Verse ID
    fun fetchAyahByIndex(surahId: Int, verseId: Int) {
        viewModelScope.launch {
            getAyahByIndexUseCase(surahId, verseId)
                .catch { e -> e.printStackTrace() }
                .collect { _ayah.value = it }
        }
    }

    // Add a bookmark
    fun addBookmark(favoriteItem: FavoriteItem) {
        viewModelScope.launch {
            addBookmarkUseCase(favoriteItem)
            fetchAllBookmarks() // Refresh bookmarks list
        }
    }

    // Fetch all bookmarks
    fun fetchAllBookmarks() {
        viewModelScope.launch {
            getAllBookmarksUseCase()
                .catch { e -> e.printStackTrace() }
                .collect { _bookmarks.value = it }
        }
    }

    // Remove a specific bookmark
    fun removeBookmark(favoriteItem: FavoriteItem) {
        viewModelScope.launch {
            removeBookmarkUseCase(favoriteItem)
            fetchAllBookmarks() // Refresh bookmarks list
        }
    }

    // Remove all bookmarks
    fun removeAllBookmarks() {
        viewModelScope.launch {
            removeAllBookmarksUseCase()
            fetchAllBookmarks() // Refresh bookmarks list
        }
    }
}
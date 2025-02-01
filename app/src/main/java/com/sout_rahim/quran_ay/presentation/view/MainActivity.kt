package com.sout_rahim.quran_ay.presentation.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sout_rahim.quran_ay.R
import com.sout_rahim.quran_ay.presentation.viewmodel.QuranViewModel
import com.sout_rahim.quran_ay.presentation.viewmodel.QuranViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: QuranViewModelFactory
    lateinit var viewModel: QuranViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this, factory)[QuranViewModel::class.java]

//        // Fetch Surahs
//           viewModel.fetchAllSurahs()
//        // Observe Surah List
//                lifecycleScope.launch {
//                    viewModel.surahs.collect { surahList ->
//                        println("Surahs received in Activity: $surahList")
//                    }
//                }

        //Fetch Surah Content
//        viewModel.fetchSurahContent(1)
//        // Observe Surah Content
//        lifecycleScope.launch {
//            viewModel.surahContent.collect { contentList ->
//                println("Surah Content received in UI: $contentList") // Log in UI
//            }
//        }

       // Observe Bookmarks
        viewModel.fetchAllBookmarks()
        lifecycleScope.launch {
            viewModel.bookmarks.collect { bookmarks ->
                println("Bookmarks received in UI: $bookmarks") // Log in UI
            }
        }
    }
}

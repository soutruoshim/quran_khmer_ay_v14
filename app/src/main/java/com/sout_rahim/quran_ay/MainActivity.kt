package com.sout_rahim.quran_ay

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.sout_rahim.quran_ay.data.model.FavoriteItem
import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.databinding.ActivityMainBinding
import com.sout_rahim.quran_ay.databinding.BottomSheetBinding
import com.sout_rahim.quran_ay.databinding.BottomSheetLanguageBinding
import com.sout_rahim.quran_ay.presentation.adapter.AyahAdapter
import com.sout_rahim.quran_ay.presentation.adapter.AyahBookmarkAdapter
import com.sout_rahim.quran_ay.presentation.adapter.SurahAdapter
import com.sout_rahim.quran_ay.presentation.viewmodel.QuranViewModel
import com.sout_rahim.quran_ay.presentation.viewmodel.QuranViewModelFactory
import com.sout_rahim.quran_ay.presentation.viewmodel.SettingViewModel
import com.sout_rahim.quran_ay.presentation.viewmodel.SettingViewModelFactory
import com.sout_rahim.quran_ay.util.Helper
import com.sout_rahim.quran_ay.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.math.ln

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var quranViewModelFactory: QuranViewModelFactory

    @Inject
    lateinit var settingViewModelFactory: SettingViewModelFactory

    @Inject
    lateinit var surahAdapter: SurahAdapter

    @Inject
    lateinit var ayahAdapter: AyahAdapter

    @Inject
    lateinit var ayahBookmarkAdapter: AyahBookmarkAdapter

    lateinit var quranViewModel: QuranViewModel
    lateinit var settingViewModel: SettingViewModel

    private lateinit var bottomSheetDialog: BottomSheetDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        bottomSheetDialog = BottomSheetDialog(applicationContext)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Find DrawerLayout and NavigationView
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        // Configure AppBarConfiguration with top-level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.surahFragment), // Top-level destinations
            drawerLayout
        )

        // Link NavController with NavigationView
        NavigationUI.setupWithNavController(navigationView, navController)
        // Link NavController with ActionBar (optional)
        setupActionBarWithNavController(navController, appBarConfiguration)


        // navigation menu item click
        binding.apply {
            navigationView.setNavigationItemSelectedListener { menuItem ->
                menuItem.isChecked = true
                drawerLayout.close()
                when (menuItem.itemId) {
                    R.id.nav_menu_bookmark ->{
                        navController.navigate(R.id.bookmarkFragment)
                        searchBar.visibility = View.GONE // Hide SearchBar in Second Fragment
                    }
                    R.id.nav_menu_language -> {
                        //navController.navigate(R.id.action_homeFragment_to_detailFragment)
                        searchBar.visibility = View.GONE // Hide SearchBar in Second Fragment
                        showBottomSheet()

                    } R.id.nav_menu_language -> {
                    //navController.navigate(R.id.action_homeFragment_to_settingFragment)
                    //searchBar.visibility = View.GONE // Hide SearchBar in Second Fragment
                }
                    else -> {
                        // Show CoordinatorLayout for other fragments
                        searchBar.visibility = View.VISIBLE
                    }
                }
                return@setNavigationItemSelectedListener true
            }

            // Listen for navigation changes
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.surahFragment) {
                    searchBar.visibility = View.VISIBLE
                }else{
                    searchBar.visibility = View.GONE
                }
            }
        }


        quranViewModel = ViewModelProvider(this, quranViewModelFactory)[QuranViewModel::class.java]
        settingViewModel = ViewModelProvider(this, settingViewModelFactory)[SettingViewModel::class.java]

                // Fetch Surahs
        settingViewModel.loadSettings()
        settingViewModel.fontSize.observe(this) { fontSize ->
            println("FontSize received in Activity: $fontSize")
        }




        // Observe dark mode changes
        settingViewModel.isDarkMode.observe(this, Observer { isDarkMode ->
            // Apply the theme based on the dark mode preference
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        })

        // Observe the language change in ViewModel
        settingViewModel.currentLanguage.observe(this, Observer { language ->
            // Update the locale when language changes
            LocaleHelper.setLocale(this, language)
            // Recreate the activity to apply the new locale
        })



        // Find the switch and set its state
        val headerView = navigationView.getHeaderView(0) // Access header view
        val themeSwitch = headerView.findViewById<Switch>(R.id.theme_switch)

        // Observe the dark mode LiveData and update the switch state
        settingViewModel.isDarkMode.observe(this, Observer { isDarkMode ->
            themeSwitch.isChecked = isDarkMode
        })

        // Handle switch toggle
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.toggleDarkMode(isChecked)
        }



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
//        viewModel.fetchAllBookmarks()
//        lifecycleScope.launch {
//            viewModel.bookmarks.collect { bookmarks ->
//                println("Bookmarks received in UI: $bookmarks") // Log in UI
//            }
//        }



    }


    private fun setupSearchView() {
        binding.apply {
            // Listen for real-time text changes
            searchView.editText.addTextChangedListener { editable ->
                val queryText = editable.toString()

                // Update the search bar text in real time (optional)
                searchBar.setText(queryText)
                // Perform your action, e.g., filtering a list, showing suggestions
                Toast.makeText(applicationContext, "Query: $queryText", Toast.LENGTH_SHORT).show()
            }

            // Listen for the Enter key press (finalized search)
            searchView.editText.setOnEditorActionListener { textView, actionId, keyEvent ->
                val queryText = textView.text.toString()
                searchBar.setText(queryText)

                Toast.makeText(applicationContext, "You Entered: $queryText", Toast.LENGTH_LONG).show()

                // Hide the SearchView
                searchView.hide()
                return@setOnEditorActionListener false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(this, R.style.NoOverlayDialog).apply {
            val binding = BottomSheetLanguageBinding.inflate(LayoutInflater.from(context))
            setContentView(binding.root)
            // Add a function to directly get the current language (e.g., for a different use case)
            // Observe current language and set button backgrounds accordingly
            settingViewModel.currentLanguage.observe(this@MainActivity, Observer { currentLanguage ->
                updateLanguageButtons(currentLanguage, binding)
            })

            // Handle language selection
            binding.btnEnglish.setOnClickListener {
                changeLanguage("en")  // Set language to English
            }
            binding.btnKhmer.setOnClickListener {
                changeLanguage("km")  // Set language to Khmer
            }
            binding.btnArabic.setOnClickListener {
                changeLanguage("ar")  // Set language to Arabic
            }

//            // Handle language selection
//            binding.btnEnglish.setOnClickListener {
//                settingViewModel.setLanguage("en")  // Set language to English
//                dismiss()
//            }
//            binding.btnKhmer.setOnClickListener {
//                settingViewModel.setLanguage("km")  // Set language to Khmer
//                dismiss()
//            }
//            binding.btnArabic.setOnClickListener {
//                settingViewModel.setLanguage("ar")  // Set language to Arabic
//                dismiss()
//            }


        }
        bottomSheetDialog.show()
    }

    // Update button backgrounds based on the selected language
    private fun updateLanguageButtons(currentLanguage: String, binding: BottomSheetLanguageBinding) {
        // Reset all icons first
        resetButtonIcons(binding)

        Log.d("MYTAG", "langauge $currentLanguage")
        // Set active background for the selected language
        when (currentLanguage) {
            "en" -> {
                binding.btnEnglish.setIconResource(R.drawable.ic_checked_circle)  // Set active button
            }
            "km" -> {
                binding.btnKhmer.setIconResource(R.drawable.ic_checked_circle)  // Set active button
            }
            "ar" -> {
                binding.btnArabic.setIconResource(R.drawable.ic_checked_circle)  // Set active button
            }
        }

    }

    // Reset button icons to default state
    private fun resetButtonIcons(binding: BottomSheetLanguageBinding) {
        binding.btnEnglish.setIconResource(R.drawable.ic_check_circle)
        binding.btnKhmer.setIconResource(R.drawable.ic_check_circle)
        binding.btnArabic.setIconResource(R.drawable.ic_check_circle)
    }

//    override fun attachBaseContext(newBase: Context) {
//        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
//    }

    // Method to handle language change and restart the activity
    private fun changeLanguage(lang: String) {
        settingViewModel.setLanguage(lang)

        LocaleHelper.setLocale(this, lang)
        Log.d("MYTAG", lang)
        // Refresh the activity to apply the new language
        val intent = intent
        finish()
        startActivity(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        if (::bottomSheetDialog.isInitialized && bottomSheetDialog.isShowing) {
            bottomSheetDialog.dismiss()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }

}

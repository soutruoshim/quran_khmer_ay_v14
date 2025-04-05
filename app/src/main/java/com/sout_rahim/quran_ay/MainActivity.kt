package com.sout_rahim.quran_ay

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.sout_rahim.quran_ay.data.model.MixedListItem
import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.data.model.SurahItem
import com.sout_rahim.quran_ay.databinding.ActivityMainBinding
import com.sout_rahim.quran_ay.databinding.BottomSheetFeedbackBinding
import com.sout_rahim.quran_ay.databinding.BottomSheetLanguageBinding
import com.sout_rahim.quran_ay.presentation.adapter.AyahAdapter
import com.sout_rahim.quran_ay.presentation.adapter.AyahBookmarkAdapter
import com.sout_rahim.quran_ay.presentation.adapter.MixedAdapter
import com.sout_rahim.quran_ay.presentation.adapter.SurahAdapter
import com.sout_rahim.quran_ay.presentation.viewmodel.QuranViewModel
import com.sout_rahim.quran_ay.presentation.viewmodel.QuranViewModelFactory
import com.sout_rahim.quran_ay.presentation.viewmodel.SettingViewModel
import com.sout_rahim.quran_ay.presentation.viewmodel.SettingViewModelFactory
import com.sout_rahim.quran_ay.util.Constants
import com.sout_rahim.quran_ay.util.Helper.moreApp
import com.sout_rahim.quran_ay.util.Helper.rateApp
import com.sout_rahim.quran_ay.util.Helper.shareApp
import com.sout_rahim.quran_ay.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

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

    @Inject
    lateinit var mixedAdapter: MixedAdapter

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

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
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
                    R.id.nav_menu_bookmark -> {
                        navController.navigate(R.id.bookmarkFragment)
                        searchBar.visibility = View.GONE // Hide SearchBar in Second Fragment
                    }

                    R.id.nav_menu_language -> {
                        //searchBar.visibility = View.GONE // Hide SearchBar in Second Fragment
                        showBottomSheet()
                    }
                    R.id.nav_menu_help -> {
                        showBottomSheetFeedback()
                    }
                    R.id.nav_menu_about -> {
                        navController.navigate(R.id.aboutFragment)
                    }

                    R.id.nav_menu_share -> {
                        shareApp()
                    }
                    R.id.nav_menu_rate -> {
                        rateApp()
                    }
                    R.id.nav_menu_more -> {
                        moreApp()
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
                } else {
                    searchBar.visibility = View.GONE
                }
            }
        }


        quranViewModel = ViewModelProvider(this, quranViewModelFactory)[QuranViewModel::class.java]
        settingViewModel =
            ViewModelProvider(this, settingViewModelFactory)[SettingViewModel::class.java]

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


        //=========adapter click

        mixedAdapter.setOnAyahClickListener {
            Log.d("selectedSurah", "Selected Surah: click on ayah")
            lifecycleScope.launch {
                val selectedSurah = it.SuraID?.let { id -> quranViewModel.getSurahById(id) }
                it.VerseID?.let { verseId -> quranViewModel.scrollToAyah(verseId) }

                Log.d("selectedSurah", "Selected Surah: $selectedSurah")

                val bundle = Bundle().apply {
                    putSerializable("selected_surah", selectedSurah)
                }

//                withContext(Dispatchers.Main) {
//                    navController.navigate(R.id.surahContentFragment, bundle)
//                }
                navController.navigate(R.id.surahContentFragment,bundle)
                binding.searchView.hide()
            }
        }
        mixedAdapter.setOnSurahClickListener {

            val bundle = Bundle().apply {
                putSerializable("selected_surah",it)
            }
            Log.d("selectedSurah", "Selected Surah: $it")

            quranViewModel.scrollToAyah(Constants.ZERO)

            navController.navigate(R.id.surahContentFragment,bundle)

            binding.searchView.hide()
        }

        setupSearchView()
        initRecyclerView()
        viewSearchList()

    }

    private fun initRecyclerView() {
        val itemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        binding.searchRecyclerView.apply {
            adapter = mixedAdapter
            layoutManager = LinearLayoutManager(this@MainActivity) // Use requireContext() instead of activity
            addItemDecoration(itemDecoration) // Correct way to add item decoration
        }
    }

    private fun viewSearchList() {
        lifecycleScope.launchWhenStarted {
            launch {
                quranViewModel.searchResults.collect { surahList ->
                    val mixedList = buildMixedList(surahList, quranViewModel.searchContentResults.value)
                    mixedAdapter.differ.submitList(mixedList)
                }
            }
            launch {
                quranViewModel.searchContentResults.collect { ayahList ->
                    val mixedList = buildMixedList(quranViewModel.searchResults.value, ayahList)
                    mixedAdapter.differ.submitList(mixedList)
                }
            }
        }
    }

    private fun setupSearchView() {
        binding.apply {
            // Listen for real-time text changes
            searchView.editText.addTextChangedListener { editable ->
                val queryText = editable.toString()

                // Update the search bar text in real time (optional)
                searchBar.setText(queryText)

                quranViewModel.searchSurahs(queryText)
                quranViewModel.searchContent(queryText)

                // Perform your action, e.g., filtering a list, showing suggestions
                //Toast.makeText(applicationContext, "Query: $queryText", Toast.LENGTH_SHORT).show()

            }

            // Listen for the Enter key press (finalized search)
            searchView.editText.setOnEditorActionListener { textView, actionId, keyEvent ->
                val queryText = textView.text.toString()
                searchBar.setText(queryText)

                quranViewModel.searchSurahs(queryText)
                quranViewModel.searchContent(queryText)

                //Toast.makeText(applicationContext, "You Entered: $queryText", Toast.LENGTH_LONG).show()

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
            settingViewModel.currentLanguage.observe(
                this@MainActivity,
                Observer { currentLanguage ->
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
        }
        bottomSheetDialog.show()
    }

    private fun showBottomSheetFeedback() {
        bottomSheetDialog = BottomSheetDialog(this, R.style.NoOverlayDialog).apply {
            val binding = BottomSheetFeedbackBinding.inflate(LayoutInflater.from(context))
            setContentView(binding.root)
            // Handle language selection
            binding.btnSend.setOnClickListener {
                val commandbox = binding.messageInput.text // assuming 'commandbox' is the EditText ID

                // Create the Intent to send email
                val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "sout.rahim@gmail.com", null))

                // Add email subject
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")

                // Add the message body text
                emailIntent.putExtra(Intent.EXTRA_TEXT, commandbox.toString())

                // Start the activity for sending email with chooser
                startActivity(Intent.createChooser(emailIntent, "Send email..."))
            }
        }
        bottomSheetDialog.show()
    }

    // Update button backgrounds based on the selected language
    private fun updateLanguageButtons(
        currentLanguage: String,
        binding: BottomSheetLanguageBinding
    ) {
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


    fun buildMixedList(surahList: List<SurahItem>, ayahList: List<SurahContentItem>): List<MixedListItem> {
        val result = mutableListOf<MixedListItem>()

        if (surahList.isNotEmpty()) {
            result.add(MixedListItem.SurahSectionHeader)
            result.addAll(surahList.map { MixedListItem.SurahData(it) })
        }

        if (ayahList.isNotEmpty()) {
            result.add(MixedListItem.AyahSectionHeader)
            result.addAll(ayahList.map { MixedListItem.AyahData(it) })
        }

        return result
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

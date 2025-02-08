package com.sout_rahim.quran_ay

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.sout_rahim.quran_ay.databinding.ActivityMainBinding
import com.sout_rahim.quran_ay.presentation.viewmodel.QuranViewModel
import com.sout_rahim.quran_ay.presentation.viewmodel.QuranViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var factory: QuranViewModelFactory
    lateinit var viewModel: QuranViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

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
                        searchBar.visibility = View.VISIBLE // Hide SearchBar in Second Fragment
                    }
                    R.id.nav_menu_settings -> {
                        //navController.navigate(R.id.action_homeFragment_to_detailFragment)
                        searchBar.visibility = View.GONE // Hide SearchBar in Second Fragment

                    } R.id.nav_menu_settings -> {
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
                }
            }
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

}

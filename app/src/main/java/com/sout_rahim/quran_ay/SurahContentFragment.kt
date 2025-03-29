package com.sout_rahim.quran_ay

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sout_rahim.quran_ay.data.model.FavoriteItem
import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.util.Constants
import com.sout_rahim.quran_ay.util.Constants.SURAH_TEXT
import com.sout_rahim.quran_ay.util.Helper
import com.sout_rahim.quran_ay.databinding.BottomSheetBinding
import com.sout_rahim.quran_ay.databinding.FragmentSurahContentBinding
import com.sout_rahim.quran_ay.presentation.adapter.AyahAdapter
import com.sout_rahim.quran_ay.presentation.viewmodel.QuranViewModel
import kotlinx.coroutines.launch


class SurahContentFragment : Fragment() {
    private lateinit var fragmentSurahContentBinding: FragmentSurahContentBinding
    private  lateinit var viewModel: QuranViewModel
    private lateinit var ayahAdapter: AyahAdapter

    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_surah_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        fragmentSurahContentBinding = FragmentSurahContentBinding.bind(view)
        viewModel= (activity as MainActivity).viewModel
        ayahAdapter = (activity as MainActivity).ayahAdapter

        bottomSheetDialog = BottomSheetDialog(requireContext())


        ayahAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(Constants.SELECTED_SURAH,it)
            }

            showBottomSheet(it)

            Log.d("MYTAG", "Item clicked: $it")

        }

        val args : SurahContentFragmentArgs by navArgs()
        val surahItem = args.selectedSurah
        Log.d(Constants.MYTAG, surahItem.id.toString())

        fragmentSurahContentBinding.surahName.text = "${SURAH_TEXT} ${surahItem.name}"
        val fontPathTrado = "${Constants.FONT}/${Constants.TRADOBOLD}"
        try {
            val fontTradoBold = Typeface.createFromAsset(fragmentSurahContentBinding.root.context.assets, fontPathTrado)
            fragmentSurahContentBinding.surahName.typeface= fontTradoBold
        } catch (e: Exception) {
            e.printStackTrace()
        }

        initRecyclerView()
        viewSurahList(surahItem.id!!)
        setupSpinnerSelection()
    }

    private fun showBottomSheet(surahContentItem: SurahContentItem) {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.NoOverlayDialog).apply {
            val binding = BottomSheetBinding.inflate(LayoutInflater.from(context))
            setContentView(binding.root)
            viewModel.fetchAllBookmarks()
            // Observe current bookmarks
            val favoriteItem = FavoriteItem.fromSurahContentItem(surahContentItem)
            val isBookmarked = viewModel.bookmarks.value.any { it.id == favoriteItem.id }

            with(binding) {
                dragHandle.setOnClickListener { dismiss() }
                layoutCopy.setOnClickListener {
                    Helper.copyToClipboard(requireContext(), Helper.formatAyahText(surahContentItem))
                    dismiss()
                }
                layoutShare.setOnClickListener {
                    Helper.shareText(requireContext(), Helper.formatAyahText(surahContentItem))
                    dismiss()
                }

                if (isBookmarked) {
                    layoutBookmark.text = getString(R.string.remove_bookmark)
                    layoutBookmark.setIconResource(R.drawable.ic_bookmark_remove)
                    layoutBookmark.setOnClickListener {
                        viewModel.removeBookmark(favoriteItem)
                        viewModel.fetchAllBookmarks()
                        Toast.makeText(requireContext(), getString(R.string.bookmark_removed), Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                } else {
                    layoutBookmark.text = getString(R.string.add_bookmark)
                    layoutBookmark.setIconResource(R.drawable.ic_bookmark_border)
                    layoutBookmark.setOnClickListener {
                        viewModel.addBookmark(favoriteItem)
                        viewModel.fetchAllBookmarks()
                        Toast.makeText(requireContext(), getString(R.string.bookmark_added), Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }
            }
        }
        bottomSheetDialog.show()
    }

    private fun setupSpinnerSelection() {
        fragmentSurahContentBinding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) { // Ensure the first item (e.g., "آية") is not used
                    val item = fragmentSurahContentBinding.spinner.getItemAtPosition(position).toString()
                    Toast.makeText(requireContext(), item, Toast.LENGTH_LONG).show()

                    val itemScroll = item.toIntOrNull() ?: return

                    fragmentSurahContentBinding.ayahRecyclerView.layoutManager?.let { layoutManager ->
                        if (layoutManager is LinearLayoutManager) {
                            layoutManager.scrollToPositionWithOffset(itemScroll, 0)
                        }
                    }

                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No action needed
            }
        }
    }

    private fun initRecyclerView() {
        val itemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        fragmentSurahContentBinding.ayahRecyclerView.apply {
            adapter = ayahAdapter
            layoutManager = LinearLayoutManager(requireContext()) // Use requireContext() instead of activity
            addItemDecoration(itemDecoration) // Correct way to add item decoration
        }
    }

    private fun viewSurahList(surahId:Int) {
        viewModel.fetchSurahContent(surahId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.surahContent.collect { ayahs ->
                setAyahNumToSpinner(ayahs)
                // Ensure the list is not empty before checking the first Ayah
                if (ayahs.isNotEmpty()) {
                    val firstAyahNumber = ayahs[0].SuraID?.toString() ?: Constants.ZERO_STRING
                    // Create a mutable list from the collected ayahs
                    val modifiedAyahs = ayahs.toMutableList()
                    // Add Bismillah except for Surah Al-Fatiha (1) and At-Tawbah (9)
                    if (!(firstAyahNumber == Constants.FIRST_AYAH_NUMBER || firstAyahNumber == Constants.SURAH_TAWBAH_NUMBER)) {
                        modifiedAyahs.add(
                            Constants.ZERO, // Add at the beginning of the list
                            Helper.createBismillahItem()
                        )
                    }
                    // Submit the modified list to the adapter
                    ayahAdapter.differ.submitList(modifiedAyahs)
                } else {
                    // If no ayahs, still update the adapter (empty list case)
                    ayahAdapter.differ.submitList(ayahs)
                }
            }
        }
    }

    private fun setAyahNumToSpinner(surahContentList: List<SurahContentItem>) {
        val labels = Helper.getAyahNumbers(surahContentList) // Adjust based on your model structure
        val dataAdapter = ArrayAdapter(requireContext(), R.layout.spinner, labels)
        dataAdapter.setDropDownViewResource(R.layout.spinner)
        fragmentSurahContentBinding.spinner.adapter = dataAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_surah_fragement, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                //Toast.makeText(requireContext(), "Settings clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


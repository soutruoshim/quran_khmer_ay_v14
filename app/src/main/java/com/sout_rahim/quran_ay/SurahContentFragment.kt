package com.sout_rahim.quran_ay

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDragHandleView
import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.data.util.Constants
import com.sout_rahim.quran_ay.data.util.Constants.SURAH_TEXT
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

        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.NoOverlayDialog)
        //bottomSheetDialog = BottomSheetDialog(requireContext())
        val binding = BottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog.setContentView(binding.root)



       // Set click listener for the drag handle to dismiss the bottom sheet
        binding.dragHandle.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        // Set click listener for the "Copy" option
        binding.layoutCopy.setOnClickListener {
            // Handle the "Copy" action
            copyText()
            bottomSheetDialog.dismiss()
        }

        // Set click listener for the "Share" option
        binding.layoutShare.setOnClickListener {
            // Handle the "Share" action
            shareContent()
            bottomSheetDialog.dismiss()
        }

        // Set click listener for the "Add Bookmark" option
        binding.layoutBookmark.setOnClickListener {
            // Handle the "Add Bookmark" action
            addBookmark()
            bottomSheetDialog.dismiss()
        }


        bottomSheetDialog.show()
    }

    // Function to handle the "Copy" action
    private fun copyText() {
        // Implement your copy logic here
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", "Text to copy") // Replace "Text to copy" with the actual text
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    // Function to handle the "Share" action
    private fun shareContent() {
        // Implement your share logic here
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Content to share") // Replace "Content to share" with the actual content
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    // Function to handle the "Add Bookmark" action
    private fun addBookmark() {
        // Implement your bookmark logic here
        Toast.makeText(requireContext(), "Bookmark added", Toast.LENGTH_SHORT).show()
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
                    val firstAyahNumber = ayahs[0].SuraID?.toString() ?: "0"
                    // Create a mutable list from the collected ayahs
                    val modifiedAyahs = ayahs.toMutableList()
                    // Add Bismillah except for Surah Al-Fatiha (1) and At-Tawbah (9)
                    if (!(firstAyahNumber == "1" || firstAyahNumber == "9")) {
                        modifiedAyahs.add(
                            0, // Add at the beginning of the list
                            createBismillahItem()
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
        val labels = getAyahNumber(surahContentList) // Adjust based on your model structure
        val dataAdapter = ArrayAdapter(requireContext(), R.layout.spinner, labels)
        dataAdapter.setDropDownViewResource(R.layout.spinner)
        fragmentSurahContentBinding.spinner.adapter = dataAdapter
    }

    private fun getAyahNumber(surahContentList: List<SurahContentItem>): List<String> {
        val labels: MutableList<String> = mutableListOf()
        // Add a header label if needed
        labels.add(Constants.AYAH_TEXT)
        // Iterate over the list and extract the relevant field
        for (item in surahContentList) {
            labels.add(item.VerseID.toString()) // Adjust based on your model structure
            Log.d(Constants.MYTAG, item.VerseID.toString())
        }
        return labels
    }

    private fun createBismillahItem(): SurahContentItem {
        return SurahContentItem(
            ID = 0,
            AyahNormal = "0",
            AyahText = Constants.BISMILLAH_TEXT,
            AyahTextKhmer = Constants.BISMILLAH_TRANSLATE_TEXT,
            SuraID = 0,
            SurahName = null,
            VerseID = 0
        )
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
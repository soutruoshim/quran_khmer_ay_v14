package com.sout_rahim.quran_ay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sout_rahim.quran_ay.databinding.FragmentSurahBinding
import com.sout_rahim.quran_ay.presentation.adapter.SurahAdapter
import com.sout_rahim.quran_ay.presentation.viewmodel.QuranViewModel
import com.sout_rahim.quran_ay.util.Constants
import kotlinx.coroutines.launch

class SurahFragment : Fragment() {
    private lateinit var fragmentSurahBinding: FragmentSurahBinding
    private  lateinit var viewModel: QuranViewModel
    private lateinit var surahAdapter: SurahAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSurahBinding = FragmentSurahBinding.bind(view)

        viewModel= (activity as MainActivity).quranViewModel
        surahAdapter= (activity as MainActivity).surahAdapter

        surahAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(Constants.SELECTED_SURAH,it)
            }

            viewModel.scrollToAyah(Constants.ZERO)

            findNavController().navigate(
                R.id.action_surahFragment_to_surahContentFragment,
                bundle
            )
        }

        initRecyclerView()
        viewSurahList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_surah, container, false)
    }

    private fun initRecyclerView() {
        val itemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        fragmentSurahBinding.rvSurah.apply {
            adapter = surahAdapter
            layoutManager = LinearLayoutManager(requireContext()) // Use requireContext() instead of activity
            addItemDecoration(itemDecoration) // Correct way to add item decoration
        }
    }

    private fun viewSurahList() {
        viewModel.fetchAllSurahs()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.surahs.collect { surahs ->
                surahAdapter.differ.submitList(surahs)
            }
        }
    }
}
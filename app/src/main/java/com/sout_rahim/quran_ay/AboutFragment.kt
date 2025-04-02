package com.sout_rahim.quran_ay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sout_rahim.quran_ay.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {
    private lateinit var aboutFragmentBinding: FragmentAboutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aboutFragmentBinding = FragmentAboutBinding.bind(view)
    }
}
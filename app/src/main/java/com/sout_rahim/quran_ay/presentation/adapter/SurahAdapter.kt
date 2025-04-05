package com.sout_rahim.quran_ay.presentation.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sout_rahim.quran_ay.data.model.SurahItem
import com.sout_rahim.quran_ay.util.Constants
import com.sout_rahim.quran_ay.databinding.ItemSurahBinding


class SurahAdapter:RecyclerView.Adapter<SurahAdapter.SurahViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<SurahItem>(){
        override fun areItemsTheSame(oldItem: SurahItem, newItem: SurahItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SurahItem, newItem: SurahItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        val binding = ItemSurahBinding
            .inflate(LayoutInflater.from(parent.context),parent,false)
        return SurahViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SurahViewHolder, position: Int) {
        val surahItem = differ.currentList[position]
        holder.bind(surahItem)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class SurahViewHolder(
        val binding: ItemSurahBinding
    ):
        RecyclerView.ViewHolder(binding.root){
        fun bind(surahItem: SurahItem){

            val surahID: String = surahItem.id.toString()
            binding.surahName.text = Constants.SURAH_NAMES[(Integer.parseInt(surahID))-1]
            binding.surahId.text = surahItem.id.toString()
            binding.surahType.text = surahItem.type.toString()
            binding.ayahN.text = Constants.AYAHS_TEXT + surahItem.NAyah

            //font surah
            // Get font using utility function
            val fontFileSurahName = Constants.getFontForSurah(surahID.toInt())
            // Make sure the font file exists inside "assets/fonts/"
            val fontPathSurahName = "${Constants.FONT}/$fontFileSurahName"
            val fontPathTradoBold = "${Constants.FONT}/${Constants.TRADOBOLD}"
            try {
                val fontSurahName = Typeface.createFromAsset(binding.root.context.assets, fontPathSurahName)
                val uthmanBold = Typeface.createFromAsset(binding.root.context.assets, fontPathTradoBold)
                binding.surahName.typeface = fontSurahName
                binding.surahType.typeface = uthmanBold

            } catch (e: Exception) {
                e.printStackTrace()
            }
            //uthman

            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(surahItem)
                }
            }

        }

    }

    private var onItemClickListener :((SurahItem)->Unit)?=null

    fun setOnItemClickListener(listener : (SurahItem)->Unit){
        onItemClickListener = listener
    }
}
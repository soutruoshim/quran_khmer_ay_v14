package com.sout_rahim.quran_ay.presentation.adapter

import android.graphics.Typeface
import android.os.Build
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sout_rahim.quran_ay.R
import com.sout_rahim.quran_ay.data.model.MixedListItem
import com.sout_rahim.quran_ay.data.model.SurahContentItem
import com.sout_rahim.quran_ay.data.model.SurahItem
import com.sout_rahim.quran_ay.databinding.ItemAyahBookmarkBinding
import com.sout_rahim.quran_ay.databinding.ItemAyahSearchBinding
import com.sout_rahim.quran_ay.databinding.ItemSurahBinding
import com.sout_rahim.quran_ay.util.ArabConvertNumber
import com.sout_rahim.quran_ay.util.Constants

class MixedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SURAH_HEADER = 0
        private const val VIEW_TYPE_SURAH_ITEM = 1
        private const val VIEW_TYPE_AYAH_HEADER = 2
        private const val VIEW_TYPE_AYAH_ITEM = 3
    }

    private val differCallback = object : DiffUtil.ItemCallback<MixedListItem>() {
        override fun areItemsTheSame(oldItem: MixedListItem, newItem: MixedListItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MixedListItem, newItem: MixedListItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is MixedListItem.SurahSectionHeader -> VIEW_TYPE_SURAH_HEADER
            is MixedListItem.SurahData -> VIEW_TYPE_SURAH_ITEM
            is MixedListItem.AyahSectionHeader -> VIEW_TYPE_AYAH_HEADER
            is MixedListItem.AyahData -> VIEW_TYPE_AYAH_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_SURAH_HEADER, VIEW_TYPE_AYAH_HEADER -> {
                val view = inflater.inflate(R.layout.item_section_header, parent, false)
                SectionHeaderViewHolder(view)
            }
            VIEW_TYPE_SURAH_ITEM -> {
                val binding = ItemSurahBinding.inflate(inflater, parent, false)
                SurahViewHolder(binding)
            }
            VIEW_TYPE_AYAH_ITEM -> {
                val binding = ItemAyahSearchBinding.inflate(inflater, parent, false)
                AyahViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = differ.currentList[position]) {
            is MixedListItem.SurahSectionHeader -> (holder as SectionHeaderViewHolder).bind(holder.itemView.context.getString(R.string.section_surahs))
            is MixedListItem.AyahSectionHeader -> (holder as SectionHeaderViewHolder).bind(holder.itemView.context.getString(R.string.section_ayahs))
            is MixedListItem.SurahData -> (holder as SurahViewHolder).bind(item.item)
            is MixedListItem.AyahData -> (holder as AyahViewHolder).bind(item.item)
        }
    }

    override fun getItemCount() = differ.currentList.size

    inner class SectionHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(title: String) {
            itemView.findViewById<TextView>(R.id.sectionTitle).text = title
        }
    }

    inner class SurahViewHolder(private val binding: ItemSurahBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(surahItem: SurahItem) {
            val surahId = surahItem.id ?: return
            binding.surahName.text =  Constants.SURAH_NAMES[(Integer.parseInt(surahId.toString()))-1]
            binding.surahId.text = surahId.toString()
            binding.surahType.text = surahItem.type ?: "-"
            binding.ayahN.text = "${Constants.AYAHS_TEXT} ${surahItem.NAyah ?: 0}"

            //font surah
            // Get font using utility function
            val fontFileSurahName = Constants.getFontForSurah(surahId)
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

            binding.root.setOnClickListener {
                onSurahClickListener?.let {
                    it(surahItem)
                }
            }
        }
    }

    inner class AyahViewHolder(private val binding: ItemAyahSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SurahContentItem) {
            val ayahNumAr = ArabConvertNumber(item.VerseID.toString()).output.let {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) it.reversed() else it
            }


            val rawAyah = item.AyahText ?: ""
            val ayahText = if (item.ID != 1) {
                rawAyah.removePrefix(Constants.BISMILLAH_TEXT)
            } else {
                rawAyah
            }

            val formatted = SpannableString("$ayahText\t$ayahNumAr")

            binding.ayahText.text = formatted
            binding.translateText.text = item.AyahTextKhmer ?: ""
            binding.surahText.text = "${Constants.SURAH_TEXT}${item.SurahName ?: ""} ${Constants.AYAH_TEXT}"
            binding.extraText.text = "(${item.VerseID ?: ""})"

            applyCustomFonts(binding)

            binding.layoutBookmark.setOnClickListener {
                onAyahClickListener?.let {
                    it(item)
                }
            }
        }
    }

    private fun applyCustomFonts(binding: ItemAyahSearchBinding) {
        try {
            val fontUthmanic = Typeface.createFromAsset(binding.root.context.assets, "${Constants.FONT}/${Constants.UTHMANIC}")
            val fontKhmerOs = Typeface.createFromAsset(binding.root.context.assets, "${Constants.FONT}/${Constants.KHMEROS}")
            val fontTrado = Typeface.createFromAsset(binding.root.context.assets, "${Constants.FONT}/${Constants.TRADO}")

            binding.ayahText.typeface = fontUthmanic
            binding.translateText.typeface = fontKhmerOs
            binding.surahText.typeface = fontTrado
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private var onSurahClickListener: ((SurahItem) -> Unit)? = null
    private var onAyahClickListener: ((SurahContentItem) -> Unit)? = null

    fun setOnSurahClickListener(listener: (SurahItem) -> Unit) {
        onSurahClickListener = listener
    }

    fun setOnAyahClickListener(listener: (SurahContentItem) -> Unit) {
        onAyahClickListener = listener
    }


}


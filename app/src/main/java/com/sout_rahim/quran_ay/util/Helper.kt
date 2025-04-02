package com.sout_rahim.quran_ay.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.sout_rahim.quran_ay.R
import com.sout_rahim.quran_ay.data.model.SurahContentItem

object Helper {

    // Function to copy text to clipboard
    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(context.getString(R.string.copied_text), text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
    }

    // Function to share text via other apps
    fun shareText(context: Context, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_via)))
    }

    // Helper function to format the Ayah text for Copy & Share
    fun formatAyahText(surahContentItem: SurahContentItem): String {
        return if (surahContentItem.SurahName == null) {
            "${surahContentItem.AyahText}\n${surahContentItem.AyahTextKhmer}"
        } else {
            "﴿${surahContentItem.AyahText}﴾\t[${Constants.SURAH_TEXT}${surahContentItem.SurahName}(${surahContentItem.VerseID})]\n${surahContentItem.AyahTextKhmer}"
        }
    }

    // Function to create a default Bismillah item
    fun createBismillahItem(): SurahContentItem {
        return SurahContentItem(
            ID = Constants.ZERO,
            AyahNormal = Constants.EMPTY_STRING,
            AyahText = Constants.BISMILLAH_TEXT,
            AyahTextKhmer = Constants.BISMILLAH_TRANSLATE_TEXT,
            SuraID = Constants.ZERO,
            SurahName = null,
            VerseID = Constants.ZERO
        )
    }

    // Function to extract Ayah numbers from a list of SurahContentItem
    fun getAyahNumbers(surahContentList: List<SurahContentItem>): List<String> {
        return mutableListOf<String>().apply {
            add(Constants.AYAH_TEXT) // Add header label
            surahContentList.forEach { add(it.VerseID.toString()) } // Extract VerseID
        }
    }

    // Function to rate the app
    fun Context.rateApp() {
        try {
            val uri = Uri.parse("market://details?id=${packageName}")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            startActivity(goToMarket)
        } catch (e: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    // Function to share the app link
    fun Context.shareApp() {
        val share = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Share this app")
            putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=$packageName")
        }
        startActivity(Intent.createChooser(share, "Share link Download!"))
    }

    // Function to open more apps from the developer
    fun Context.moreApp() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Srh%20Dp&hl=en"))
            startActivity(intent)
        } catch (e: Exception) {
            // Handle exception if needed
        }
    }
}
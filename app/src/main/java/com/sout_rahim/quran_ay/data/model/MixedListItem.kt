package com.sout_rahim.quran_ay.data.model

sealed class MixedListItem {
    object SurahSectionHeader : MixedListItem()
    object AyahSectionHeader : MixedListItem()
    data class SurahData(val item: SurahItem) : MixedListItem()
    data class AyahData(val item: SurahContentItem) : MixedListItem()
}

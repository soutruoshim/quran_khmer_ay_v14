package com.sout_rahim.quran_ay.util

object Constants {

    const val EMPTY_STRING = ""
    const val ZERO = 0
    const val ZERO_STRING = "0"
    const val FIRST_AYAH_NUMBER = "1"
    const val SURAH_TAWBAH_NUMBER = "9"
    const val AYAHS_TEXT = "الآيات "
    const val AYAH_TEXT = "آية"
    const val SURAH_TEXT = "سورة "
    const val BISMILLAH_TEXT = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
    const val BISMILLAH_TRANSLATE_TEXT = "ក្នុងនាមអល់ឡោះ មហាសប្បុរស មហាអាណិតស្រលាញ់"
    const val SELECTED_SURAH = "selected_surahِ"
    const val MYTAG = "MYTAG"

    const val FONT: String = "fonts"
    const val TRADOBOLD: String = "tradoB.ttf"
    const val TRADO: String = "trado.ttf"
    const val UTHMANIC: String = "UthmanicHafs_V22.ttf"
    const val KHMEROS: String = "KhmerOS.ttf"

    val SURAH_NAMES = arrayOf(
        "\u0030", "\u0032", "\u0034", "\u0036", "\u0038",
        "\u0041", "\u0043", "\u0045", "\u0047", "\u0049",
        "\u004B", "\u004D", "\u004F", "\u0051", "\u0053",
        "\u0055", "\u0057", "\u0059", "\u0061", "\u0063",
        "\u0065", "\u0067", "\u0069", "\u006B", "\u006C",
        "\u006E", "\u0070", "\u0072", "\u0074", "\u0076",
        "\u0078", "\u0030", "\u0032", "\u0034", "\u0036",
        "\u0038", "\u0041", "\u0043", "\u0045", "\u0047",
        "\u0049", "\u004B", "\u004D", "\u004F", "\u0051",
        "\u0053", "\u0055", "\u0057", "\u0059", "\u0061",
        "\u0063", "\u0065", "\u0067", "\u0069", "\u006B",
        "\u006D", "\u006F", "\u0071", "\u0073", "\u0075",
        "\u0077", "\u0079", "\u0030", "\u0032", "\u0034",
        "\u0036", "\u0038", "\u0041", "\u0043", "\u0045",
        "\u0047", "\u0049", "\u004B", "\u004D", "\u004F",
        "\u0051", "\u0053", "\u0055", "\u0057", "\u0059",
        "\u0061", "\u0063", "\u0065", "\u0067", "\u0069",
        "\u006B", "\u006D", "\u006F", "\u0071", "\u0073",
        "\u0075", "\u0077", "\u0079", "\u0030", "\u0032",
        "\u0034", "\u0036", "\u0038", "\u0041", "\u0043",
        "\u0045", "\u0047", "\u0049", "\u004B", "\u004D",
        "\u004F", "\u0051", "\u0053", "\u0055", "\u0057",
        "\u0059", "\u0061", "\u0063", "\u0065"
    )

    fun getFontForSurah(surahID: Int): String {
        return when {
            surahID <= 31 -> "ar-ALHAWE_QURAN_1_31.ttf"
            surahID in 32..62 -> "ar-ALHAWE_QURAN_32_62.ttf"
            surahID in 63..93 -> "ar-ALHAWE_QURAN_63_93.ttf"
            surahID in 94..114 -> "ar-ALHAWE_QURAN_94_114.otf"
            else -> "default_font.ttf" // Fallback in case of an invalid surah ID
        }
    }
}
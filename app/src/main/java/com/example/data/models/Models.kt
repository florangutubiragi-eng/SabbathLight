package com.example.data.models

enum class AppLanguage(val code: String, val displayName: String) {
    SWAHILI("sw", "Kiswahili"),
    ENGLISH("en", "English")
}

enum class AppThemeMode(val displayNameSwahili: String, val displayNameEnglish: String) {
    SYSTEM("Kama Mfumo", "System Default"),
    LIGHT("Mwanga", "Light Mode"),
    DARK("Giza (Navy)", "Dark Mode")
}

enum class FontSizeMode(
    val scaleFactor: Float,
    val labelSwahili: String,
    val labelEnglish: String
) {
    NORMAL(1.0f, "Kawaida", "Normal"),
    MEDIUM(1.15f, "Wastani", "Medium"),
    LARGE(1.30f, "Kubwa", "Large"),
    EXTRA_LARGE(1.45f, "Kubwa Sana", "Extra Large")
}

enum class Testament {
    OLD, NEW
}

data class BibleVerse(
    val bookId: String,
    val bookNameSwahili: String,
    val bookNameEnglish: String,
    val chapter: Int,
    val verseNumber: Int,
    val textSwahili: String,
    val textEnglish: String
) {
    val reference: String
        get() = "$bookNameSwahili $chapter:$verseNumber"
}

data class BibleChapter(
    val bookId: String,
    val chapterNumber: Int,
    val verses: List<BibleVerse>
)

data class BibleBook(
    val id: String,
    val nameSwahili: String,
    val nameEnglish: String,
    val testament: Testament,
    val totalChapters: Int,
    val sampleChapters: List<BibleChapter> = emptyList()
)

enum class HymnCollection(
    val id: String,
    val titleSwahili: String,
    val titleEnglish: String,
    val shortCode: String,
    val editionTitle: String,
    val languageCode: String, // "sw", "en", "multi"
    val isCheckedAgainstPublishedEdition: Boolean,
    val totalHymnsInPublishedEdition: Int,
    val descriptionSwahili: String,
    val descriptionEnglish: String
) {
    NYIMBO_ZA_KRISTO(
        id = "nzk",
        titleSwahili = "Nyimbo za Kristo",
        titleEnglish = "Nyimbo za Kristo (Swahili)",
        shortCode = "NZK",
        editionTitle = "Nyimbo za Kristo (Toleo Rasmi la Kiswahili - Waadventista wa Sabato)",
        languageCode = "sw",
        isCheckedAgainstPublishedEdition = true,
        totalHymnsInPublishedEdition = 220,
        descriptionSwahili = "Kitabu rasmi cha nyimbo za Waadventista wa Sabato katika lugha ya Kiswahili kikiwa na nambari rasmi.",
        descriptionEnglish = "Official Swahili Seventh-day Adventist Hymnal arranged strictly by official hymn numbers."
    ),
    SDA_HYMNAL_1985(
        id = "sdah_1985",
        titleSwahili = "SDA Hymnal (1985)",
        titleEnglish = "Seventh-day Adventist Hymnal (1985)",
        shortCode = "SDAH",
        editionTitle = "Seventh-day Adventist Hymnal (1985 Edition, Review & Herald Publishing Association)",
        languageCode = "en",
        isCheckedAgainstPublishedEdition = true,
        totalHymnsInPublishedEdition = 695,
        descriptionSwahili = "Kitabu rasmi cha nyimbo cha Kiingereza cha Waadventista wa Sabato (Toleo la 1985).",
        descriptionEnglish = "Official English Seventh-day Adventist Hymnal (1985) arranged strictly by original published SDAH numbers."
    ),
    OTHER_ADVENTIST(
        id = "other_adventist",
        titleSwahili = "Mikusanyiko Mingine ya Kisabato",
        titleEnglish = "Other Adventist Collections",
        shortCode = "OAC",
        editionTitle = "Christ in Song (1908) & Nyimbo za Kisabato (Toleo la Kanda ya Afrika Mashariki)",
        languageCode = "multi",
        isCheckedAgainstPublishedEdition = true,
        totalHymnsInPublishedEdition = 100,
        descriptionSwahili = "Mikusanyiko mingine iliyoidhinishwa ya nyimbo za Kisabato ikiwemo Christ in Song na Nyimbo za Kisabato.",
        descriptionEnglish = "Other verified Adventist hymnal collections including Christ in Song (1908) and Nyimbo za Kisabato."
    ),
    CHOIR_SONGS(
        id = "choir_songs",
        titleSwahili = "Nyimbo za Kwaya",
        titleEnglish = "Choir Songs",
        shortCode = "KWAYA",
        editionTitle = "Kwaya za Waadventista wa Sabato (SDA Choirs & Anthems Collection)",
        languageCode = "sw",
        isCheckedAgainstPublishedEdition = true,
        totalHymnsInPublishedEdition = 50,
        descriptionSwahili = "Mkusanyiko maalum wa nyimbo za kwaya za Kisabato, nyimbo za uinjilisti na vijana.",
        descriptionEnglish = "Adventist choral anthems, evangelistic songs, and youth choir collection."
    )
}

enum class HymnBookType(val displayNameSwahili: String, val displayNameEnglish: String) {
    NYIMBO_ZA_KRISTO("Nyimbo za Kristo", "Christ in Song / NZK"),
    NYIMBO_ZA_KISABATO("Nyimbo za Kisabato", "Adventist Hymns / KS"),
    SDA_HYMNAL_1985("SDA Hymnal (1985)", "Seventh-day Adventist Hymnal (1985)"),
    CHOIR_SONGS("Nyimbo za Kwaya", "Choir Songs")
}

data class Hymn(
    val id: String,
    val number: Int,
    val titleSwahili: String,
    val titleEnglish: String,
    val category: String,
    val bookType: HymnBookType = HymnBookType.NYIMBO_ZA_KRISTO,
    val collection: HymnCollection = HymnCollection.NYIMBO_ZA_KRISTO,
    val editionName: String = "",
    val isVerifiedEntry: Boolean = true,
    val verificationNotes: String = "",
    val languageCode: String = "sw",
    val versesSwahili: List<String>,
    val chorusSwahili: String? = null,
    val versesEnglish: List<String> = emptyList(),
    val chorusEnglish: String? = null,
    val scriptureRef: String = "",
    val artistOrComposer: String = "",
    val tuneName: String = "",
    val audioUrl: String = "",
    val isCustom: Boolean = false
)

data class DailyLesson(
    val id: String,
    val dayIndex: Int, // 0 for Sabbath afternoon to 6 for Friday
    val dayNameSwahili: String,
    val dayNameEnglish: String,
    val titleSwahili: String,
    val titleEnglish: String,
    val dateString: String,
    val scriptureReferences: List<String>,
    val contentSwahili: String,
    val contentEnglish: String,
    val questionsSwahili: List<String> = emptyList(),
    val questionsEnglish: List<String> = emptyList()
)

data class QuarterlyLesson(
    val id: String,
    val quarterTitleSwahili: String,
    val quarterTitleEnglish: String,
    val lessonNumber: Int,
    val lessonTitleSwahili: String,
    val lessonTitleEnglish: String,
    val dateRange: String,
    val memoryVerseSwahili: String,
    val memoryVerseEnglish: String,
    val memoryVerseReference: String,
    val dailyLessons: List<DailyLesson>
)

data class EgwChapter(
    val id: String,
    val bookId: String,
    val chapterNumber: Int,
    val titleSwahili: String,
    val titleEnglish: String,
    val contentSwahili: String,
    val contentEnglish: String
)

data class EgwBook(
    val id: String,
    val titleSwahili: String,
    val titleEnglish: String,
    val author: String = "Ellen G. White",
    val descriptionSwahili: String,
    val descriptionEnglish: String,
    val chapters: List<EgwChapter> = emptyList()
)

enum class SermonType {
    AUDIO, VIDEO
}

data class Sermon(
    val id: String,
    val titleSwahili: String,
    val titleEnglish: String,
    val speaker: String,
    val durationMinutes: Int,
    val type: SermonType,
    val date: String,
    val topicSwahili: String,
    val topicEnglish: String,
    val mediaUrl: String,
    val descriptionSwahili: String,
    val descriptionEnglish: String
)

enum class PrayerCategory(val labelSwahili: String, val labelEnglish: String) {
    MORNING("Asubuhi", "Morning"),
    MIDDAY("Mchana", "Midday"),
    EVENING("Jioni / Usiku", "Evening / Night"),
    PERSONAL("Maombi Binafsi", "Personal Prayers")
}

data class PrayerItem(
    val id: String,
    val titleSwahili: String,
    val titleEnglish: String,
    val category: PrayerCategory,
    val scripture: String,
    val scriptureRef: String,
    val prayerTextSwahili: String,
    val prayerTextEnglish: String,
    val isAnswered: Boolean = false,
    val isUserCustom: Boolean = false,
    val dateCreated: Long = System.currentTimeMillis()
)

data class HopeChannelItem(
    val id: String,
    val titleSwahili: String,
    val titleEnglish: String,
    val programHost: String,
    val scheduleTime: String,
    val descriptionSwahili: String,
    val descriptionEnglish: String,
    val streamUrl: String,
    val isLiveNow: Boolean = false
)

enum class ContentType {
    BIBLE, HYMN, LESSON, SERMON, EGW, PRAYER
}

data class BookmarkItem(
    val id: String,
    val type: ContentType,
    val title: String,
    val subtitle: String,
    val targetId: String,
    val savedAt: Long = System.currentTimeMillis()
)

data class RecentlyViewedItem(
    val id: String,
    val type: ContentType,
    val title: String,
    val subtitle: String,
    val targetId: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class DownloadedItem(
    val id: String,
    val type: ContentType,
    val title: String,
    val subtitle: String,
    val estimatedSize: String,
    val downloadSource: String = "SDA Authorized Public Resource",
    val downloadedAt: Long = System.currentTimeMillis()
)

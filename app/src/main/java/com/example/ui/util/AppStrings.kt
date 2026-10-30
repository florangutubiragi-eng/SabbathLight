package com.example.ui.util

import com.example.data.models.AppLanguage

object AppStrings {

    fun appName(lang: AppLanguage): String = "SABBATH LIFE"

    fun tagline(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Kuishi kwa Neno, Kuendelea na Kristo."
        AppLanguage.ENGLISH -> "Living by the Word, Growing with Christ."
    }

    // Navigation
    fun navHome(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Nyumbani"
        AppLanguage.ENGLISH -> "Home"
    }

    fun navBible(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Biblia"
        AppLanguage.ENGLISH -> "Bible"
    }

    fun navLesson(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Somo"
        AppLanguage.ENGLISH -> "Lesson"
    }

    fun navHymns(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Nyimbo"
        AppLanguage.ENGLISH -> "Hymns"
    }

    fun navSermons(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Mahubiri"
        AppLanguage.ENGLISH -> "Sermons"
    }

    fun navMore(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Zaidi"
        AppLanguage.ENGLISH -> "More"
    }

    // Home
    fun sabbathGreeting(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Sabato Njema!"
        AppLanguage.ENGLISH -> "Happy Sabbath!"
    }

    fun welcomeMessage(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Karibu katika pumziko takatifu la Bwana. Nuru ya Neno ikuongoze leo."
        AppLanguage.ENGLISH -> "Welcome to the Lord's holy rest. May the light of His Word guide you today."
    }

    fun dailyVerseTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Aya ya Sabato ya Leo"
        AppLanguage.ENGLISH -> "Today's Sabbath Scripture"
    }

    fun weeklyLessonSpotlight(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Somo la Shule ya Sabato la Juma Hili"
        AppLanguage.ENGLISH -> "This Week's Sabbath School Lesson"
    }

    fun quickFeaturesTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Huduma Kuu za Kiroho"
        AppLanguage.ENGLISH -> "Spiritual Pillars & Ministries"
    }

    fun hopeChannelSection(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Hope Channel Tanzania"
        AppLanguage.ENGLISH -> "Hope Channel Tanzania"
    }

    fun recentlyViewed(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Uliyotazama Hivi Karibuni"
        AppLanguage.ENGLISH -> "Recently Viewed"
    }

    // Bible
    fun oldTestament(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Agano la Kale"
        AppLanguage.ENGLISH -> "Old Testament"
    }

    fun newTestament(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Agano Jipya"
        AppLanguage.ENGLISH -> "New Testament"
    }

    fun searchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Tafuta kitabu, sura au neno..."
        AppLanguage.ENGLISH -> "Search book, chapter or keyword..."
    }

    fun readChapter(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Soma Sura"
        AppLanguage.ENGLISH -> "Read Chapter"
    }

    // Hymns
    fun nyimboZaKristo(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Nyimbo za Kristo (Kiswahili)"
        AppLanguage.ENGLISH -> "Christ in Song (English)"
    }

    fun searchHymnPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Tafuta namba au jina la wimbo..."
        AppLanguage.ENGLISH -> "Search hymn number or title..."
    }

    // Roho ya Unabii
    fun spiritOfProphecy(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Roho ya Unabii"
        AppLanguage.ENGLISH -> "Spirit of Prophecy"
    }

    fun egwSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Maandishi Yaliyoidhinishwa ya Ellen G. White"
        AppLanguage.ENGLISH -> "Authorized Ellen G. White Writings"
    }

    // Prayers
    fun prayerTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Maisha ya Maombi"
        AppLanguage.ENGLISH -> "Prayer Life"
    }

    fun addPrayer(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Ongeza Ombi"
        AppLanguage.ENGLISH -> "Add Request"
    }

    fun answeredBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Imejibiwa!"
        AppLanguage.ENGLISH -> "Answered!"
    }

    // Settings
    fun settings(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Mipangilio"
        AppLanguage.ENGLISH -> "Settings"
    }

    fun languageLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Lugha / Language"
        AppLanguage.ENGLISH -> "Language / Lugha"
    }

    fun themeLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Mandhari ya Mwanga / Giza"
        AppLanguage.ENGLISH -> "Theme (Light / Dark)"
    }

    fun textSizeLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Ukubwa wa Maandishi"
        AppLanguage.ENGLISH -> "Text Font Size"
    }

    fun aboutUs(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Kuhusu Sabbath Life"
        AppLanguage.ENGLISH -> "About Sabbath Life"
    }

    fun bookmarksAndFavorites(lang: AppLanguage): String = when (lang) {
        AppLanguage.SWAHILI -> "Alama na Vipendwa"
        AppLanguage.ENGLISH -> "Bookmarks & Favorites"
    }
}

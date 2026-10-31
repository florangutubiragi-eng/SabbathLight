package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.models.AppLanguage
import com.example.data.models.AppThemeMode
import com.example.data.models.ContentType
import com.example.data.models.DownloadedItem
import com.example.data.models.FontSizeMode
import com.example.data.models.PrayerCategory
import com.example.data.models.PrayerItem
import com.example.data.models.RecentlyViewedItem
import com.example.data.models.Hymn
import com.example.data.models.HymnBookType
import com.example.data.models.HymnCollection
import org.json.JSONArray
import org.json.JSONObject

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("sabbath_life_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LANGUAGE = "app_language"
        private const val KEY_THEME = "app_theme"
        private const val KEY_FONT_SIZE = "app_font_size"
        private const val KEY_BOOKMARKS = "bookmarked_ids"
        private const val KEY_FAVORITE_HYMNS = "favorite_hymns"
        private const val KEY_CUSTOM_PRAYERS = "custom_prayers_json"
        private const val KEY_STUDY_NOTES = "study_notes_json"
        private const val KEY_RECENTLY_VIEWED = "recently_viewed_json"
        private const val KEY_DAILY_REMINDER = "daily_prayer_reminder"
        private const val KEY_RECENTLY_PLAYED_HYMNS = "recently_played_hymns_json"
        private const val KEY_CUSTOM_HYMNS = "custom_hymns_json"
        private const val KEY_ADMIN_CORRECTIONS = "admin_hymn_corrections_json"
        private const val KEY_AUDIO_VOLUME = "audio_player_volume"
        private const val KEY_DOWNLOADED_ITEMS = "downloaded_items_json"
    }

    var language: AppLanguage
        get() {
            val code = prefs.getString(KEY_LANGUAGE, AppLanguage.SWAHILI.code) ?: AppLanguage.SWAHILI.code
            return if (code == AppLanguage.ENGLISH.code) AppLanguage.ENGLISH else AppLanguage.SWAHILI
        }
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value.code).apply()

    var themeMode: AppThemeMode
        get() {
            val name = prefs.getString(KEY_THEME, AppThemeMode.DARK.name) ?: AppThemeMode.DARK.name
            return try {
                AppThemeMode.valueOf(name)
            } catch (e: Exception) {
                AppThemeMode.DARK
            }
        }
        set(value) = prefs.edit().putString(KEY_THEME, value.name).apply()

    var fontSizeMode: FontSizeMode
        get() {
            val name = prefs.getString(KEY_FONT_SIZE, FontSizeMode.NORMAL.name) ?: FontSizeMode.NORMAL.name
            return try {
                FontSizeMode.valueOf(name)
            } catch (e: Exception) {
                FontSizeMode.NORMAL
            }
        }
        set(value) = prefs.edit().putString(KEY_FONT_SIZE, value.name).apply()

    var dailyReminderEnabled: Boolean
        get() = prefs.getBoolean(KEY_DAILY_REMINDER, true)
        set(value) = prefs.edit().putBoolean(KEY_DAILY_REMINDER, value).apply()

    fun getBookmarks(): Set<String> {
        return prefs.getStringSet(KEY_BOOKMARKS, emptySet()) ?: emptySet()
    }

    fun toggleBookmark(id: String): Boolean {
        val current = getBookmarks().toMutableSet()
        val isAdded: Boolean
        if (current.contains(id)) {
            current.remove(id)
            isAdded = false
        } else {
            current.add(id)
            isAdded = true
        }
        prefs.edit().putStringSet(KEY_BOOKMARKS, current).apply()
        return isAdded
    }

    fun getFavoriteHymns(): Set<String> {
        return prefs.getStringSet(KEY_FAVORITE_HYMNS, emptySet()) ?: emptySet()
    }

    fun toggleFavoriteHymn(hymnId: String): Boolean {
        val current = getFavoriteHymns().toMutableSet()
        val isAdded: Boolean
        if (current.contains(hymnId)) {
            current.remove(hymnId)
            isAdded = false
        } else {
            current.add(hymnId)
            isAdded = true
        }
        prefs.edit().putStringSet(KEY_FAVORITE_HYMNS, current).apply()
        return isAdded
    }

    fun saveStudyNote(lessonDayId: String, noteText: String) {
        val notesJson = prefs.getString(KEY_STUDY_NOTES, "{}") ?: "{}"
        try {
            val jsonObject = JSONObject(notesJson)
            jsonObject.put(lessonDayId, noteText)
            prefs.edit().putString(KEY_STUDY_NOTES, jsonObject.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getStudyNote(lessonDayId: String): String {
        val notesJson = prefs.getString(KEY_STUDY_NOTES, "{}") ?: "{}"
        return try {
            val jsonObject = JSONObject(notesJson)
            jsonObject.optString(lessonDayId, "")
        } catch (e: Exception) {
            ""
        }
    }

    fun getCustomPrayers(): List<PrayerItem> {
        val jsonStr = prefs.getString(KEY_CUSTOM_PRAYERS, "[]") ?: "[]"
        val list = mutableListOf<PrayerItem>()
        try {
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val categoryName = obj.optString("category", PrayerCategory.PERSONAL.name)
                val category = try {
                    PrayerCategory.valueOf(categoryName)
                } catch (e: Exception) {
                    PrayerCategory.PERSONAL
                }

                list.add(
                    PrayerItem(
                        id = obj.getString("id"),
                        titleSwahili = obj.optString("titleSwahili", ""),
                        titleEnglish = obj.optString("titleEnglish", ""),
                        category = category,
                        scripture = obj.optString("scripture", ""),
                        scriptureRef = obj.optString("scriptureRef", ""),
                        prayerTextSwahili = obj.optString("prayerTextSwahili", ""),
                        prayerTextEnglish = obj.optString("prayerTextEnglish", ""),
                        isAnswered = obj.optBoolean("isAnswered", false),
                        isUserCustom = true,
                        dateCreated = obj.optLong("dateCreated", System.currentTimeMillis())
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveCustomPrayers(prayers: List<PrayerItem>) {
        try {
            val jsonArray = JSONArray()
            for (item in prayers) {
                val obj = JSONObject()
                obj.put("id", item.id)
                obj.put("titleSwahili", item.titleSwahili)
                obj.put("titleEnglish", item.titleEnglish)
                obj.put("category", item.category.name)
                obj.put("scripture", item.scripture)
                obj.put("scriptureRef", item.scriptureRef)
                obj.put("prayerTextSwahili", item.prayerTextSwahili)
                obj.put("prayerTextEnglish", item.prayerTextEnglish)
                obj.put("isAnswered", item.isAnswered)
                obj.put("isUserCustom", item.isUserCustom)
                obj.put("dateCreated", item.dateCreated)
                jsonArray.put(obj)
            }
            prefs.edit().putString(KEY_CUSTOM_PRAYERS, jsonArray.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getRecentlyViewed(): List<RecentlyViewedItem> {
        val jsonStr = prefs.getString(KEY_RECENTLY_VIEWED, "[]") ?: "[]"
        val list = mutableListOf<RecentlyViewedItem>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val typeName = obj.optString("type", ContentType.BIBLE.name)
                val type = try {
                    ContentType.valueOf(typeName)
                } catch (e: Exception) {
                    ContentType.BIBLE
                }
                list.add(
                    RecentlyViewedItem(
                        id = obj.getString("id"),
                        type = type,
                        title = obj.getString("title"),
                        subtitle = obj.optString("subtitle", ""),
                        targetId = obj.getString("targetId"),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun addRecentlyViewed(item: RecentlyViewedItem) {
        val current = getRecentlyViewed().toMutableList()
        current.removeAll { it.targetId == item.targetId }
        current.add(0, item)
        val trimmed = if (current.size > 10) current.subList(0, 10) else current
        try {
            val array = JSONArray()
            for (it in trimmed) {
                val obj = JSONObject()
                obj.put("id", it.id)
                obj.put("type", it.type.name)
                obj.put("title", it.title)
                obj.put("subtitle", it.subtitle)
                obj.put("targetId", it.targetId)
                obj.put("timestamp", it.timestamp)
                array.put(obj)
            }
            prefs.edit().putString(KEY_RECENTLY_VIEWED, array.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearHistory() {
        prefs.edit().remove(KEY_RECENTLY_VIEWED).apply()
    }

    var audioVolume: Float
        get() = prefs.getFloat(KEY_AUDIO_VOLUME, 0.8f)
        set(value) = prefs.edit().putFloat(KEY_AUDIO_VOLUME, value.coerceIn(0f, 1f)).apply()

    fun getRecentlyPlayedHymnIds(): List<String> {
        val json = prefs.getString(KEY_RECENTLY_PLAYED_HYMNS, "[]") ?: "[]"
        val list = mutableListOf<String>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun addRecentlyPlayedHymnId(id: String) {
        val current = getRecentlyPlayedHymnIds().toMutableList()
        current.remove(id)
        current.add(0, id)
        val trimmed = if (current.size > 20) current.subList(0, 20) else current
        try {
            val array = JSONArray()
            for (item in trimmed) {
                array.put(item)
            }
            prefs.edit().putString(KEY_RECENTLY_PLAYED_HYMNS, array.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCustomHymns(): List<Hymn> {
        val json = prefs.getString(KEY_CUSTOM_HYMNS, "[]") ?: "[]"
        val list = mutableListOf<Hymn>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val bookTypeName = obj.optString("bookType", HymnBookType.NYIMBO_ZA_KRISTO.name)
                val bookType = try {
                    HymnBookType.valueOf(bookTypeName)
                } catch (e: Exception) {
                    HymnBookType.NYIMBO_ZA_KRISTO
                }

                val collectionName = obj.optString("collection", HymnCollection.NYIMBO_ZA_KRISTO.name)
                val collection = try {
                    HymnCollection.valueOf(collectionName)
                } catch (e: Exception) {
                    when (bookType) {
                        HymnBookType.NYIMBO_ZA_KRISTO -> HymnCollection.NYIMBO_ZA_KRISTO
                        HymnBookType.NYIMBO_ZA_KISABATO -> HymnCollection.OTHER_ADVENTIST
                        HymnBookType.SDA_HYMNAL_1985 -> HymnCollection.SDA_HYMNAL_1985
                        HymnBookType.CHOIR_SONGS -> HymnCollection.CHOIR_SONGS
                    }
                }

                val versesSwahiliArr = obj.optJSONArray("versesSwahili") ?: JSONArray()
                val versesSwahili = mutableListOf<String>()
                for (j in 0 until versesSwahiliArr.length()) {
                    versesSwahili.add(versesSwahiliArr.getString(j))
                }

                val versesEnglishArr = obj.optJSONArray("versesEnglish") ?: JSONArray()
                val versesEnglish = mutableListOf<String>()
                for (j in 0 until versesEnglishArr.length()) {
                    versesEnglish.add(versesEnglishArr.getString(j))
                }

                list.add(
                    Hymn(
                        id = obj.getString("id"),
                        number = obj.optInt("number", 0),
                        titleSwahili = obj.optString("titleSwahili", ""),
                        titleEnglish = obj.optString("titleEnglish", ""),
                        category = obj.optString("category", "Ziada"),
                        bookType = bookType,
                        collection = collection,
                        editionName = obj.optString("editionName", collection.editionTitle),
                        isVerifiedEntry = obj.optBoolean("isVerifiedEntry", true),
                        verificationNotes = obj.optString("verificationNotes", ""),
                        languageCode = obj.optString("languageCode", collection.languageCode),
                        versesSwahili = versesSwahili,
                        chorusSwahili = obj.optString("chorusSwahili").takeIf { it.isNotEmpty() },
                        versesEnglish = versesEnglish,
                        chorusEnglish = obj.optString("chorusEnglish").takeIf { it.isNotEmpty() },
                        scriptureRef = obj.optString("scriptureRef", ""),
                        artistOrComposer = obj.optString("artistOrComposer", ""),
                        tuneName = obj.optString("tuneName", ""),
                        audioUrl = obj.optString("audioUrl", ""),
                        isCustom = true
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveCustomHymns(hymns: List<Hymn>) {
        try {
            val array = JSONArray()
            for (h in hymns) {
                val obj = JSONObject()
                obj.put("id", h.id)
                obj.put("number", h.number)
                obj.put("titleSwahili", h.titleSwahili)
                obj.put("titleEnglish", h.titleEnglish)
                obj.put("category", h.category)
                obj.put("bookType", h.bookType.name)
                obj.put("collection", h.collection.name)
                obj.put("editionName", h.editionName)
                obj.put("isVerifiedEntry", h.isVerifiedEntry)
                obj.put("verificationNotes", h.verificationNotes)
                obj.put("languageCode", h.languageCode)
                obj.put("chorusSwahili", h.chorusSwahili ?: "")
                obj.put("chorusEnglish", h.chorusEnglish ?: "")
                obj.put("scriptureRef", h.scriptureRef)
                obj.put("artistOrComposer", h.artistOrComposer)
                obj.put("tuneName", h.tuneName)
                obj.put("audioUrl", h.audioUrl)
                obj.put("isCustom", true)

                val vSwahili = JSONArray()
                h.versesSwahili.forEach { vSwahili.put(it) }
                obj.put("versesSwahili", vSwahili)

                val vEnglish = JSONArray()
                h.versesEnglish.forEach { vEnglish.put(it) }
                obj.put("versesEnglish", vEnglish)

                array.put(obj)
            }
            prefs.edit().putString(KEY_CUSTOM_HYMNS, array.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAdminCorrections(): Map<String, Hymn> {
        val json = prefs.getString(KEY_ADMIN_CORRECTIONS, "{}") ?: "{}"
        val map = mutableMapOf<String, Hymn>()
        try {
            val root = JSONObject(json)
            val keys = root.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val obj = root.getJSONObject(key)

                val bookTypeName = obj.optString("bookType", HymnBookType.NYIMBO_ZA_KRISTO.name)
                val bookType = try {
                    HymnBookType.valueOf(bookTypeName)
                } catch (e: Exception) {
                    HymnBookType.NYIMBO_ZA_KRISTO
                }

                val collectionName = obj.optString("collection", HymnCollection.NYIMBO_ZA_KRISTO.name)
                val collection = try {
                    HymnCollection.valueOf(collectionName)
                } catch (e: Exception) {
                    HymnCollection.NYIMBO_ZA_KRISTO
                }

                val versesSwahiliArr = obj.optJSONArray("versesSwahili") ?: JSONArray()
                val versesSwahili = mutableListOf<String>()
                for (j in 0 until versesSwahiliArr.length()) {
                    versesSwahili.add(versesSwahiliArr.getString(j))
                }

                val versesEnglishArr = obj.optJSONArray("versesEnglish") ?: JSONArray()
                val versesEnglish = mutableListOf<String>()
                for (j in 0 until versesEnglishArr.length()) {
                    versesEnglish.add(versesEnglishArr.getString(j))
                }

                map[key] = Hymn(
                    id = obj.getString("id"),
                    number = obj.optInt("number", 0),
                    titleSwahili = obj.optString("titleSwahili", ""),
                    titleEnglish = obj.optString("titleEnglish", ""),
                    category = obj.optString("category", "Sifa"),
                    bookType = bookType,
                    collection = collection,
                    editionName = obj.optString("editionName", collection.editionTitle),
                    isVerifiedEntry = obj.optBoolean("isVerifiedEntry", true),
                    verificationNotes = obj.optString("verificationNotes", ""),
                    languageCode = obj.optString("languageCode", collection.languageCode),
                    versesSwahili = versesSwahili,
                    chorusSwahili = obj.optString("chorusSwahili").takeIf { it.isNotEmpty() },
                    versesEnglish = versesEnglish,
                    chorusEnglish = obj.optString("chorusEnglish").takeIf { it.isNotEmpty() },
                    scriptureRef = obj.optString("scriptureRef", ""),
                    artistOrComposer = obj.optString("artistOrComposer", ""),
                    tuneName = obj.optString("tuneName", ""),
                    audioUrl = obj.optString("audioUrl", ""),
                    isCustom = obj.optBoolean("isCustom", false)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return map
    }

    fun saveSingleAdminCorrection(hymn: Hymn) {
        val current = getAdminCorrections().toMutableMap()
        current[hymn.id] = hymn
        saveAdminCorrections(current)
    }

    fun removeAdminCorrection(hymnId: String) {
        val current = getAdminCorrections().toMutableMap()
        current.remove(hymnId)
        saveAdminCorrections(current)
    }

    fun resetAllAdminCorrections() {
        prefs.edit().remove(KEY_ADMIN_CORRECTIONS).apply()
    }

    private fun saveAdminCorrections(corrections: Map<String, Hymn>) {
        try {
            val root = JSONObject()
            for ((key, h) in corrections) {
                val obj = JSONObject()
                obj.put("id", h.id)
                obj.put("number", h.number)
                obj.put("titleSwahili", h.titleSwahili)
                obj.put("titleEnglish", h.titleEnglish)
                obj.put("category", h.category)
                obj.put("bookType", h.bookType.name)
                obj.put("collection", h.collection.name)
                obj.put("editionName", h.editionName)
                obj.put("isVerifiedEntry", h.isVerifiedEntry)
                obj.put("verificationNotes", h.verificationNotes)
                obj.put("languageCode", h.languageCode)
                obj.put("chorusSwahili", h.chorusSwahili ?: "")
                obj.put("chorusEnglish", h.chorusEnglish ?: "")
                obj.put("scriptureRef", h.scriptureRef)
                obj.put("artistOrComposer", h.artistOrComposer)
                obj.put("tuneName", h.tuneName)
                obj.put("audioUrl", h.audioUrl)
                obj.put("isCustom", h.isCustom)

                val vSwahili = JSONArray()
                h.versesSwahili.forEach { vSwahili.put(it) }
                obj.put("versesSwahili", vSwahili)

                val vEnglish = JSONArray()
                h.versesEnglish.forEach { vEnglish.put(it) }
                obj.put("versesEnglish", vEnglish)

                root.put(key, obj)
            }
            prefs.edit().putString(KEY_ADMIN_CORRECTIONS, root.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getDownloadedItems(): List<DownloadedItem> {
        val json = prefs.getString(KEY_DOWNLOADED_ITEMS, null)
        if (json.isNullOrBlank()) {
            return listOf(
                DownloadedItem(
                    id = "dl_nzk_essentials",
                    type = ContentType.HYMN,
                    title = "Nyimbo za Kristo (Nyimbo Muhimu 1, 2, 24)",
                    subtitle = "Sifa na Utukufu & Heri Sabato ya Bwana",
                    estimatedSize = "4.2 MB",
                    downloadSource = "SDA Public Domain / Authorized",
                    downloadedAt = System.currentTimeMillis() - 86400000L
                ),
                DownloadedItem(
                    id = "dl_lesson_current",
                    type = ContentType.LESSON,
                    title = "Somo la Shule ya Sabato: Mwanga wa Injili",
                    subtitle = "Masomo ya Siku 7 (Sabato hadi Ijumaa)",
                    estimatedSize = "1.8 MB",
                    downloadSource = "Adventist Adult Sabbath School",
                    downloadedAt = System.currentTimeMillis() - 43200000L
                ),
                DownloadedItem(
                    id = "dl_genesis_creation",
                    type = ContentType.BIBLE,
                    title = "Biblia SUV: Mwanzo Sura 1 & 2 (Uumbaji)",
                    subtitle = "Maandishi kamili ya Agano la Kale",
                    estimatedSize = "850 KB",
                    downloadSource = "Swahili Union Version Authorized Text",
                    downloadedAt = System.currentTimeMillis() - 172800000L
                ),
                DownloadedItem(
                    id = "dl_steps_to_christ",
                    type = ContentType.EGW,
                    title = "Hatua kwa Kristo (Steps to Christ)",
                    subtitle = "Sura 1: Upendo wa Mungu kwa Mwanadamu",
                    estimatedSize = "1.1 MB",
                    downloadSource = "Ellen G. White Estate Official",
                    downloadedAt = System.currentTimeMillis() - 259200000L
                )
            )
        }
        val list = mutableListOf<DownloadedItem>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val type = try {
                    ContentType.valueOf(obj.getString("type"))
                } catch (e: Exception) {
                    ContentType.HYMN
                }
                list.add(
                    DownloadedItem(
                        id = obj.getString("id"),
                        type = type,
                        title = obj.getString("title"),
                        subtitle = obj.optString("subtitle", ""),
                        estimatedSize = obj.optString("estimatedSize", "1.0 MB"),
                        downloadSource = obj.optString("downloadSource", "SDA Authorized"),
                        downloadedAt = obj.optLong("downloadedAt", System.currentTimeMillis())
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveDownloadedItem(item: DownloadedItem) {
        val current = getDownloadedItems().toMutableList()
        val index = current.indexOfFirst { it.id == item.id }
        if (index >= 0) {
            current[index] = item
        } else {
            current.add(0, item)
        }
        saveDownloadedList(current)
    }

    fun removeDownloadedItem(id: String) {
        val current = getDownloadedItems().toMutableList()
        current.removeAll { it.id == id }
        saveDownloadedList(current)
    }

    fun clearAllDownloadedItems() {
        prefs.edit().remove(KEY_DOWNLOADED_ITEMS).apply()
    }

    private fun saveDownloadedList(items: List<DownloadedItem>) {
        try {
            val array = JSONArray()
            for (item in items) {
                val obj = JSONObject()
                obj.put("id", item.id)
                obj.put("type", item.type.name)
                obj.put("title", item.title)
                obj.put("subtitle", item.subtitle)
                obj.put("estimatedSize", item.estimatedSize)
                obj.put("downloadSource", item.downloadSource)
                obj.put("downloadedAt", item.downloadedAt)
                array.put(obj)
            }
            prefs.edit().putString(KEY_DOWNLOADED_ITEMS, array.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.PreferencesManager
import com.example.data.models.AppLanguage
import com.example.data.models.AppThemeMode
import com.example.data.models.BibleBook
import com.example.data.models.ContentType
import com.example.data.models.DailyLesson
import com.example.data.models.EgwBook
import com.example.data.models.EgwChapter
import com.example.data.models.FontSizeMode
import com.example.data.models.Hymn
import com.example.data.models.HymnBookType
import com.example.data.models.HymnCollection
import com.example.data.models.PrayerCategory
import com.example.data.models.PrayerItem
import com.example.data.models.RecentlyViewedItem
import com.example.data.models.Sermon
import com.example.data.models.SermonType
import com.example.data.sample.SampleData
import com.example.ui.navigation.NavScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import java.util.UUID

data class AudioPlayerState(
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val title: String = "",
    val subtitle: String = "",
    val audioUrl: String = "",
    val progress: Float = 0f,
    val currentDurationSeconds: Int = 0,
    val totalDurationSeconds: Int = 180,
    val speed: Float = 1.0f,
    val volume: Float = 0.8f,
    val currentHymn: Hymn? = null,
    val playlist: List<Hymn> = emptyList(),
    val currentIndex: Int = -1,
    val errorMessage: String? = null
)

class SabbathViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferencesManager(application)

    // Language & Theme & Font
    private val _language = MutableStateFlow(prefs.language)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _themeMode = MutableStateFlow(prefs.themeMode)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _fontSizeMode = MutableStateFlow(prefs.fontSizeMode)
    val fontSizeMode: StateFlow<FontSizeMode> = _fontSizeMode.asStateFlow()

    private val _dailyReminder = MutableStateFlow(prefs.dailyReminderEnabled)
    val dailyReminder: StateFlow<Boolean> = _dailyReminder.asStateFlow()

    // Navigation state with history stack for proper back handling
    private val _currentScreen = MutableStateFlow(NavScreen.HOME)
    val currentScreen: StateFlow<NavScreen> = _currentScreen.asStateFlow()

    private val screenStack = mutableListOf(NavScreen.HOME)

    // Selection states
    private val _selectedBibleBook = MutableStateFlow<BibleBook?>(SampleData.sampleBooks.firstOrNull())
    val selectedBibleBook: StateFlow<BibleBook?> = _selectedBibleBook.asStateFlow()

    private val _selectedBibleChapter = MutableStateFlow(1)
    val selectedBibleChapter: StateFlow<Int> = _selectedBibleChapter.asStateFlow()

    private val _selectedHymn = MutableStateFlow<Hymn?>(null)
    val selectedHymn: StateFlow<Hymn?> = _selectedHymn.asStateFlow()

    private val _selectedDailyLessonIndex = MutableStateFlow(0)
    val selectedDailyLessonIndex: StateFlow<Int> = _selectedDailyLessonIndex.asStateFlow()

    private val _studyNoteText = MutableStateFlow("")
    val studyNoteText: StateFlow<String> = _studyNoteText.asStateFlow()

    private val _selectedEgwBook = MutableStateFlow<EgwBook?>(SampleData.egwBooks.firstOrNull())
    val selectedEgwBook: StateFlow<EgwBook?> = _selectedEgwBook.asStateFlow()

    private val _selectedEgwChapter = MutableStateFlow<EgwChapter?>(null)
    val selectedEgwChapter: StateFlow<EgwChapter?> = _selectedEgwChapter.asStateFlow()

    private val _selectedSermon = MutableStateFlow<Sermon?>(null)
    val selectedSermon: StateFlow<Sermon?> = _selectedSermon.asStateFlow()

    // Search & Filter
    private val _bibleSearch = MutableStateFlow("")
    val bibleSearch: StateFlow<String> = _bibleSearch.asStateFlow()

    private val _hymnSearch = MutableStateFlow("")
    val hymnSearch: StateFlow<String> = _hymnSearch.asStateFlow()

    private val _sermonFilter = MutableStateFlow<SermonType?>(null)
    val sermonFilter: StateFlow<SermonType?> = _sermonFilter.asStateFlow()

    private val _activePrayerCategory = MutableStateFlow(PrayerCategory.MORNING)
    val activePrayerCategory: StateFlow<PrayerCategory> = _activePrayerCategory.asStateFlow()

    // Bookmarks, Favorites & Prayers
    private val _bookmarks = MutableStateFlow(prefs.getBookmarks())
    val bookmarks: StateFlow<Set<String>> = _bookmarks.asStateFlow()

    private val _favoriteHymns = MutableStateFlow(prefs.getFavoriteHymns())
    val favoriteHymns: StateFlow<Set<String>> = _favoriteHymns.asStateFlow()

    private val _customHymns = MutableStateFlow(prefs.getCustomHymns())
    val customHymns: StateFlow<List<Hymn>> = _customHymns.asStateFlow()

    private val _adminCorrections = MutableStateFlow(prefs.getAdminCorrections())
    val adminCorrections: StateFlow<Map<String, Hymn>> = _adminCorrections.asStateFlow()

    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    private val _selectedCollectionFilter = MutableStateFlow<HymnCollection?>(null)
    val selectedCollectionFilter: StateFlow<HymnCollection?> = _selectedCollectionFilter.asStateFlow()

    private val _selectedLanguageFilter = MutableStateFlow<String?>(null)
    val selectedLanguageFilter: StateFlow<String?> = _selectedLanguageFilter.asStateFlow()

    private val _recentlyPlayedHymnIds = MutableStateFlow(prefs.getRecentlyPlayedHymnIds())
    val recentlyPlayedHymnIds: StateFlow<List<String>> = _recentlyPlayedHymnIds.asStateFlow()

    private val _customPrayers = MutableStateFlow(prefs.getCustomPrayers())
    val customPrayers: StateFlow<List<PrayerItem>> = _customPrayers.asStateFlow()

    private val _recentlyViewed = MutableStateFlow(prefs.getRecentlyViewed())
    val recentlyViewed: StateFlow<List<RecentlyViewedItem>> = _recentlyViewed.asStateFlow()

    // Interactive Audio Simulation
    private val _audioPlayerState = MutableStateFlow(AudioPlayerState(volume = prefs.audioVolume))
    val audioPlayerState: StateFlow<AudioPlayerState> = _audioPlayerState.asStateFlow()

    private var playbackJob: Job? = null

    init {
        loadStudyNoteForCurrentLesson()
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
        prefs.language = lang
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
        prefs.themeMode = mode
    }

    fun setFontSizeMode(mode: FontSizeMode) {
        _fontSizeMode.value = mode
        prefs.fontSizeMode = mode
    }

    fun setDailyReminder(enabled: Boolean) {
        _dailyReminder.value = enabled
        prefs.dailyReminderEnabled = enabled
    }

    fun navigateTo(screen: NavScreen) {
        if (_currentScreen.value != screen) {
            screenStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.size - 1)
            _currentScreen.value = screenStack.last()
            return true
        } else if (_currentScreen.value != NavScreen.HOME) {
            screenStack.clear()
            screenStack.add(NavScreen.HOME)
            _currentScreen.value = NavScreen.HOME
            return true
        }
        return false
    }

    fun openBibleBook(book: BibleBook, chapter: Int = 1) {
        _selectedBibleBook.value = book
        _selectedBibleChapter.value = chapter
        navigateTo(NavScreen.BIBLE_CHAPTER_READER)
        recordRecentlyViewed(
            type = ContentType.BIBLE,
            title = "${book.nameSwahili} (${book.nameEnglish})",
            subtitle = "Sura / Chapter $chapter",
            targetId = "bible_${book.id}_$chapter"
        )
    }

    fun setBibleChapter(chapter: Int) {
        _selectedBibleChapter.value = chapter
    }

    fun setBibleSearch(query: String) {
        _bibleSearch.value = query
    }

    fun openHymn(hymn: Hymn) {
        _selectedHymn.value = hymn
        navigateTo(NavScreen.HYMN_DETAIL)
        recordRecentlyViewed(
            type = ContentType.HYMN,
            title = "#${hymn.number} ${hymn.titleSwahili}",
            subtitle = hymn.titleEnglish,
            targetId = hymn.id
        )
    }

    fun setHymnSearch(query: String) {
        _hymnSearch.value = query
    }

    fun selectDailyLesson(index: Int) {
        _selectedDailyLessonIndex.value = index
        loadStudyNoteForCurrentLesson()
    }

    private fun loadStudyNoteForCurrentLesson() {
        val currentLesson = SampleData.currentQuarterlyLesson.dailyLessons.getOrNull(_selectedDailyLessonIndex.value)
        if (currentLesson != null) {
            _studyNoteText.value = prefs.getStudyNote(currentLesson.id)
        }
    }

    fun updateStudyNote(text: String) {
        _studyNoteText.value = text
        val currentLesson = SampleData.currentQuarterlyLesson.dailyLessons.getOrNull(_selectedDailyLessonIndex.value)
        if (currentLesson != null) {
            prefs.saveStudyNote(currentLesson.id, text)
        }
    }

    fun openEgwBook(book: EgwBook) {
        _selectedEgwBook.value = book
        _selectedEgwChapter.value = book.chapters.firstOrNull()
        navigateTo(NavScreen.EGW_CHAPTER_READER)
        recordRecentlyViewed(
            type = ContentType.EGW,
            title = book.titleSwahili,
            subtitle = book.titleEnglish,
            targetId = book.id
        )
    }

    fun selectEgwChapter(chapter: EgwChapter) {
        _selectedEgwChapter.value = chapter
    }

    fun openSermon(sermon: Sermon) {
        _selectedSermon.value = sermon
        startAudioPlayback(
            title = sermon.titleSwahili,
            subtitle = "${sermon.speaker} • ${sermon.durationMinutes} min",
            totalSeconds = sermon.durationMinutes * 60
        )
        recordRecentlyViewed(
            type = ContentType.SERMON,
            title = sermon.titleSwahili,
            subtitle = sermon.speaker,
            targetId = sermon.id
        )
    }

    fun setSermonFilter(type: SermonType?) {
        _sermonFilter.value = type
    }

    fun setActivePrayerCategory(cat: PrayerCategory) {
        _activePrayerCategory.value = cat
    }

    fun toggleBookmark(id: String, type: ContentType, title: String, subtitle: String): Boolean {
        val added = prefs.toggleBookmark(id)
        _bookmarks.value = prefs.getBookmarks()
        return added
    }

    fun toggleFavoriteHymn(hymnId: String): Boolean {
        val added = prefs.toggleFavoriteHymn(hymnId)
        _favoriteHymns.value = prefs.getFavoriteHymns()
        return added
    }

    fun addCustomPrayer(titleSwahili: String, titleEnglish: String, prayerTextSwahili: String, prayerTextEnglish: String) {
        val newPrayer = PrayerItem(
            id = "prayer_custom_${UUID.randomUUID()}",
            titleSwahili = titleSwahili,
            titleEnglish = if (titleEnglish.isNotBlank()) titleEnglish else titleSwahili,
            category = PrayerCategory.PERSONAL,
            scripture = "Ombeni, nanyi mtapewa; tafuteni, nanyi mtaona.",
            scriptureRef = "Mathayo 7:7",
            prayerTextSwahili = prayerTextSwahili,
            prayerTextEnglish = if (prayerTextEnglish.isNotBlank()) prayerTextEnglish else prayerTextSwahili,
            isAnswered = false,
            isUserCustom = true
        )
        val updated = _customPrayers.value.toMutableList()
        updated.add(0, newPrayer)
        _customPrayers.value = updated
        prefs.saveCustomPrayers(updated)
    }

    fun togglePrayerAnswered(prayerId: String) {
        val updated = _customPrayers.value.map {
            if (it.id == prayerId) it.copy(isAnswered = !it.isAnswered) else it
        }
        _customPrayers.value = updated
        prefs.saveCustomPrayers(updated)
    }

    fun deleteCustomPrayer(prayerId: String) {
        val updated = _customPrayers.value.filterNot { it.id == prayerId }
        _customPrayers.value = updated
        prefs.saveCustomPrayers(updated)
    }

    fun recordRecentlyViewed(type: ContentType, title: String, subtitle: String, targetId: String) {
        val item = RecentlyViewedItem(
            id = UUID.randomUUID().toString(),
            type = type,
            title = title,
            subtitle = subtitle,
            targetId = targetId
        )
        prefs.addRecentlyViewed(item)
        _recentlyViewed.value = prefs.getRecentlyViewed()
    }

    fun clearHistory() {
        prefs.clearHistory()
        _recentlyViewed.value = emptyList()
    }

    // Audio Player Controls & Real MediaPlayer Management
    private var mediaPlayer: MediaPlayer? = null

    private fun releaseMediaPlayer() {
        try {
            playbackJob?.cancel()
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                player.reset()
                player.release()
            }
        } catch (e: Exception) {
            Log.e("SabbathAudio", "Error releasing MediaPlayer", e)
        } finally {
            mediaPlayer = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        releaseMediaPlayer()
    }

    fun playHymn(hymn: Hymn, playlist: List<Hymn> = emptyList()) {
        val fullList = if (playlist.isNotEmpty()) playlist else listOf(hymn)
        val index = fullList.indexOfFirst { it.id == hymn.id }.let { if (it >= 0) it else 0 }

        prefs.addRecentlyPlayedHymnId(hymn.id)
        _recentlyPlayedHymnIds.value = prefs.getRecentlyPlayedHymnIds()
        recordRecentlyViewed(
            type = ContentType.HYMN,
            title = "#${hymn.number} ${hymn.titleSwahili}",
            subtitle = hymn.bookType.displayNameSwahili,
            targetId = hymn.id
        )

        releaseMediaPlayer()

        val audioUrl = hymn.audioUrl.trim()
        val hasUrl = audioUrl.isNotBlank()

        _audioPlayerState.value = AudioPlayerState(
            isPlaying = false,
            isLoading = hasUrl,
            title = "#${hymn.number} ${hymn.titleSwahili}",
            subtitle = if (hymn.titleEnglish.isNotBlank()) "${hymn.titleEnglish} • ${hymn.bookType.displayNameSwahili}" else hymn.bookType.displayNameSwahili,
            audioUrl = audioUrl,
            progress = 0f,
            currentDurationSeconds = 0,
            totalDurationSeconds = 180,
            volume = prefs.audioVolume,
            currentHymn = hymn,
            playlist = fullList,
            currentIndex = index,
            errorMessage = if (!hasUrl) "Hakuna kiungo cha sauti kilichounganishwa kwa wimbo huu." else null
        )

        if (!hasUrl) return

        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(getApplication(), Uri.parse(audioUrl))
                val vol = prefs.audioVolume
                setVolume(vol, vol)

                setOnPreparedListener { mp ->
                    val durationSec = (mp.duration / 1000).coerceAtLeast(1)
                    _audioPlayerState.value = _audioPlayerState.value.copy(
                        isPlaying = true,
                        isLoading = false,
                        totalDurationSeconds = durationSec,
                        errorMessage = null
                    )
                    mp.start()
                    startMediaProgressTicker()
                }

                setOnCompletionListener {
                    _audioPlayerState.value = _audioPlayerState.value.copy(
                        isPlaying = false,
                        progress = 1f
                    )
                    if (_audioPlayerState.value.playlist.isNotEmpty()) {
                        playNextTrack()
                    }
                }

                setOnErrorListener { _, what, extra ->
                    Log.e("SabbathAudio", "MediaPlayer error: what=$what, extra=$extra for url $audioUrl")
                    _audioPlayerState.value = _audioPlayerState.value.copy(
                        isPlaying = false,
                        isLoading = false,
                        errorMessage = "Imeshindikana kucheza sauti ($what, $extra). Tafadhali angalia mtandao wako."
                    )
                    true
                }

                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            Log.e("SabbathAudio", "Failed to initialize MediaPlayer for $audioUrl", e)
            _audioPlayerState.value = _audioPlayerState.value.copy(
                isPlaying = false,
                isLoading = false,
                errorMessage = "Hitilafu ya sauti: ${e.localizedMessage ?: "Network error"}"
            )
        }
    }

    fun startAudioPlayback(title: String, subtitle: String, totalSeconds: Int = 180) {
        val current = _audioPlayerState.value
        if (current.currentHymn != null) {
            playHymn(current.currentHymn, current.playlist)
        } else {
            _audioPlayerState.value = AudioPlayerState(
                isPlaying = true,
                title = title,
                subtitle = subtitle,
                totalDurationSeconds = totalSeconds,
                volume = prefs.audioVolume
            )
        }
    }

    private fun startMediaProgressTicker() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (true) {
                delay(500)
                val mp = mediaPlayer
                if (mp != null && _audioPlayerState.value.isPlaying) {
                    try {
                        if (mp.isPlaying) {
                            val currentMs = mp.currentPosition
                            val totalMs = mp.duration.coerceAtLeast(1000)
                            val currentSec = currentMs / 1000
                            val totalSec = totalMs / 1000
                            val progress = currentMs.toFloat() / totalMs.toFloat()
                            _audioPlayerState.value = _audioPlayerState.value.copy(
                                currentDurationSeconds = currentSec,
                                totalDurationSeconds = totalSec,
                                progress = progress.coerceIn(0f, 1f)
                            )
                        }
                    } catch (e: Exception) {
                        // ignore transitory states during prepare/seek
                    }
                } else if (!_audioPlayerState.value.isPlaying && _audioPlayerState.value.errorMessage != null) {
                    break
                }
            }
        }
    }

    fun playNextTrack() {
        val state = _audioPlayerState.value
        if (state.playlist.isNotEmpty()) {
            val nextIndex = (state.currentIndex + 1) % state.playlist.size
            playHymn(state.playlist[nextIndex], state.playlist)
        }
    }

    fun playPreviousTrack() {
        val state = _audioPlayerState.value
        if (state.playlist.isNotEmpty()) {
            val prevIndex = if (state.currentIndex - 1 < 0) state.playlist.size - 1 else state.currentIndex - 1
            playHymn(state.playlist[prevIndex], state.playlist)
        }
    }

    fun setAudioVolume(vol: Float) {
        val clamped = vol.coerceIn(0f, 1f)
        prefs.audioVolume = clamped
        try {
            mediaPlayer?.setVolume(clamped, clamped)
        } catch (e: Exception) {
            Log.e("SabbathAudio", "Error setting volume", e)
        }
        _audioPlayerState.value = _audioPlayerState.value.copy(volume = clamped)
    }

    fun togglePlayPause() {
        val player = mediaPlayer
        val current = _audioPlayerState.value
        if (player != null) {
            try {
                if (player.isPlaying) {
                    player.pause()
                    _audioPlayerState.value = current.copy(isPlaying = false)
                } else {
                    player.start()
                    _audioPlayerState.value = current.copy(isPlaying = true, errorMessage = null)
                    startMediaProgressTicker()
                }
            } catch (e: Exception) {
                Log.e("SabbathAudio", "togglePlayPause error", e)
                if (current.currentHymn != null) {
                    playHymn(current.currentHymn, current.playlist)
                }
            }
        } else if (current.currentHymn != null) {
            playHymn(current.currentHymn, current.playlist)
        }
    }

    fun retryCurrentAudio() {
        val current = _audioPlayerState.value.currentHymn
        if (current != null) {
            playHymn(current, _audioPlayerState.value.playlist)
        }
    }

    fun seekAudio(progressFraction: Float) {
        val player = mediaPlayer
        if (player != null) {
            try {
                val totalMs = player.duration
                if (totalMs > 0) {
                    val targetMs = (progressFraction * totalMs).toInt().coerceIn(0, totalMs)
                    player.seekTo(targetMs)
                    val targetSec = targetMs / 1000
                    _audioPlayerState.value = _audioPlayerState.value.copy(
                        progress = progressFraction.coerceIn(0f, 1f),
                        currentDurationSeconds = targetSec
                    )
                }
            } catch (e: Exception) {
                Log.e("SabbathAudio", "seekAudio error", e)
            }
        }
    }

    fun closeAudioPlayer() {
        releaseMediaPlayer()
        _audioPlayerState.value = AudioPlayerState(volume = prefs.audioVolume)
    }

    // Hymn Library & Custom Additions
    fun toggleAdminMode() {
        _isAdminMode.value = !_isAdminMode.value
    }

    fun setCollectionFilter(collection: HymnCollection?) {
        _selectedCollectionFilter.value = collection
    }

    fun setLanguageFilter(lang: String?) {
        _selectedLanguageFilter.value = lang
    }

    fun saveHymnCorrection(hymn: Hymn) {
        prefs.saveSingleAdminCorrection(hymn)
        _adminCorrections.value = prefs.getAdminCorrections()
        if (_selectedHymn.value?.id == hymn.id) {
            _selectedHymn.value = hymn
        }
    }

    fun resetHymnCorrection(hymnId: String) {
        prefs.removeAdminCorrection(hymnId)
        _adminCorrections.value = prefs.getAdminCorrections()
        val original = getAllHymns().find { it.id == hymnId }
        if (_selectedHymn.value?.id == hymnId) {
            _selectedHymn.value = original
        }
    }

    fun resetAllCatalogCorrections() {
        prefs.resetAllAdminCorrections()
        _adminCorrections.value = emptyMap()
    }

    fun addCustomHymn(
        number: Int,
        titleSwahili: String,
        titleEnglish: String,
        category: String,
        bookType: HymnBookType,
        collection: HymnCollection = when (bookType) {
            HymnBookType.NYIMBO_ZA_KRISTO -> HymnCollection.NYIMBO_ZA_KRISTO
            HymnBookType.NYIMBO_ZA_KISABATO -> HymnCollection.OTHER_ADVENTIST
            HymnBookType.SDA_HYMNAL_1985 -> HymnCollection.SDA_HYMNAL_1985
            HymnBookType.CHOIR_SONGS -> HymnCollection.CHOIR_SONGS
        },
        editionName: String = collection.editionTitle,
        isVerifiedEntry: Boolean = true,
        verificationNotes: String = "Imeongezwa na mtumiaji / msimamizi.",
        languageCode: String = collection.languageCode,
        versesSwahili: List<String>,
        chorusSwahili: String?,
        versesEnglish: List<String> = emptyList(),
        chorusEnglish: String? = null,
        artistOrComposer: String = "",
        tuneName: String = "",
        scriptureRef: String = "",
        audioUrl: String = ""
    ) {
        val newHymn = Hymn(
            id = "custom_hymn_${UUID.randomUUID()}",
            number = number,
            titleSwahili = titleSwahili,
            titleEnglish = titleEnglish,
            category = category.ifBlank { "Ziada" },
            bookType = bookType,
            collection = collection,
            editionName = editionName,
            isVerifiedEntry = isVerifiedEntry,
            verificationNotes = verificationNotes,
            languageCode = languageCode,
            versesSwahili = versesSwahili,
            chorusSwahili = chorusSwahili?.takeIf { it.isNotBlank() },
            versesEnglish = versesEnglish,
            chorusEnglish = chorusEnglish?.takeIf { it.isNotBlank() },
            scriptureRef = scriptureRef,
            artistOrComposer = artistOrComposer,
            tuneName = tuneName,
            audioUrl = audioUrl,
            isCustom = true
        )
        val updated = _customHymns.value.toMutableList()
        updated.add(0, newHymn)
        _customHymns.value = updated
        prefs.saveCustomHymns(updated)
    }

    fun deleteCustomHymn(id: String) {
        val updated = _customHymns.value.filterNot { it.id == id }
        _customHymns.value = updated
        prefs.saveCustomHymns(updated)
    }

    fun getAllHymns(): List<Hymn> {
        val base = SampleData.sampleHymns
        val custom = _customHymns.value
        val corrections = _adminCorrections.value
        return (base + custom).map { hymn ->
            corrections[hymn.id] ?: hymn
        }
    }

    fun getRecentlyPlayedHymns(): List<Hymn> {
        val ids = _recentlyPlayedHymnIds.value
        val all = getAllHymns()
        return ids.mapNotNull { id -> all.find { it.id == id } }
    }
}

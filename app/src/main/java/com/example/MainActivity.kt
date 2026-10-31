package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.models.AppLanguage
import com.example.data.models.AppThemeMode
import com.example.ui.components.GlobalAudioPlayerBar
import com.example.ui.components.SabbathBottomBar
import com.example.ui.components.SabbathTopBar
import com.example.ui.navigation.NavScreen
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.BibleReaderScreen
import com.example.ui.screens.BibleScreen
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.EgwReaderScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HopeChannelScreen
import com.example.ui.screens.HymnDetailScreen
import com.example.ui.screens.HymnsScreen
import com.example.ui.screens.LessonScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.screens.PrayerScreen
import com.example.ui.screens.SermonsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SpiritOfProphecyScreen
import com.example.ui.theme.SabbathLifeTheme
import com.example.ui.viewmodel.SabbathViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SabbathViewModel = viewModel()
            val themeMode by viewModel.themeMode.collectAsState()
            val language by viewModel.language.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()
            val bookmarks by viewModel.bookmarks.collectAsState()
            val audioPlayerState by viewModel.audioPlayerState.collectAsState()

            val isDark = when (themeMode) {
                AppThemeMode.DARK -> true
                AppThemeMode.LIGHT -> false
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            SabbathLifeTheme(darkTheme = isDark) {
                // Handle System Back Button
                BackHandler(enabled = currentScreen != NavScreen.HOME) {
                    viewModel.navigateBack()
                }

                val isReaderScreen = currentScreen == NavScreen.BIBLE_CHAPTER_READER ||
                        currentScreen == NavScreen.HYMN_DETAIL ||
                        currentScreen == NavScreen.EGW_CHAPTER_READER

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (!isReaderScreen) {
                            SabbathTopBar(
                                language = language,
                                currentScreen = currentScreen,
                                bookmarkCount = bookmarks.size,
                                onLanguageToggle = {
                                    val next = if (language == AppLanguage.SWAHILI) AppLanguage.ENGLISH else AppLanguage.SWAHILI
                                    viewModel.setLanguage(next)
                                },
                                onNavigate = { screen -> viewModel.navigateTo(screen) }
                            )
                        }
                    },
                    bottomBar = {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            GlobalAudioPlayerBar(
                                state = audioPlayerState,
                                onTogglePlayPause = { viewModel.togglePlayPause() },
                                onPrevious = { viewModel.playPreviousTrack() },
                                onNext = { viewModel.playNextTrack() },
                                onSeek = { viewModel.seekAudio(it) },
                                onVolumeChange = { viewModel.setAudioVolume(it) },
                                onRetry = { viewModel.retryCurrentAudio() },
                                onClose = { viewModel.closeAudioPlayer() }
                            )

                            if (!isReaderScreen) {
                                SabbathBottomBar(
                                    currentScreen = currentScreen,
                                    language = language,
                                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "ScreenTransition"
                        ) { screen ->
                            when (screen) {
                                NavScreen.HOME -> HomeScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    onNavigate = { viewModel.navigateTo(it) }
                                )
                                NavScreen.BIBLE -> BibleScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    onNavigate = { viewModel.navigateTo(it) }
                                )
                                NavScreen.BIBLE_CHAPTER_READER -> BibleReaderScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    onBack = { viewModel.navigateBack() }
                                )
                                NavScreen.LESSON -> LessonScreen(
                                    viewModel = viewModel,
                                    language = language
                                )
                                NavScreen.HYMNS -> HymnsScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    onNavigate = { viewModel.navigateTo(it) }
                                )
                                NavScreen.HYMN_DETAIL -> HymnDetailScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    onBack = { viewModel.navigateBack() }
                                )
                                NavScreen.SERMONS -> SermonsScreen(
                                    viewModel = viewModel,
                                    language = language
                                )
                                NavScreen.SPIRIT_OF_PROPHECY -> SpiritOfProphecyScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    onNavigate = { viewModel.navigateTo(it) }
                                )
                                NavScreen.EGW_CHAPTER_READER -> EgwReaderScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    onBack = { viewModel.navigateBack() }
                                )
                                NavScreen.PRAYER -> PrayerScreen(
                                    viewModel = viewModel,
                                    language = language
                                )
                                NavScreen.HOPE_CHANNEL -> HopeChannelScreen(
                                    viewModel = viewModel,
                                    language = language
                                )
                                NavScreen.BOOKMARKS -> BookmarksScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    onNavigate = { viewModel.navigateTo(it) }
                                )
                                NavScreen.SETTINGS -> SettingsScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    onNavigate = { viewModel.navigateTo(it) }
                                )
                                NavScreen.ABOUT -> AboutScreen(
                                    language = language
                                )
                                NavScreen.DOWNLOADS -> DownloadsScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    onNavigate = { viewModel.navigateTo(it) }
                                )
                                NavScreen.MORE -> MoreScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    onNavigate = { viewModel.navigateTo(it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

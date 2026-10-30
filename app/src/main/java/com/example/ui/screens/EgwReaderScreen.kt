package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.data.models.ContentType
import com.example.data.models.FontSizeMode
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.viewmodel.SabbathViewModel

@Composable
fun EgwReaderScreen(
    viewModel: SabbathViewModel,
    language: AppLanguage,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val book by viewModel.selectedEgwBook.collectAsState()
    val currentChapter by viewModel.selectedEgwChapter.collectAsState()
    val fontSizeMode by viewModel.fontSizeMode.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()

    var showEnglish by remember { mutableStateOf(language == AppLanguage.ENGLISH) }
    var showFontMenu by remember { mutableStateOf(false) }

    val targetBook = book
    if (targetBook == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Kitabu hakikupatikana", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    val activeChapter = currentChapter ?: targetBook.chapters.firstOrNull()
    val isBookmarked = bookmarks.contains("egw_${activeChapter?.id}")

    val baseFontSize = 16.sp
    val dynamicSize = (baseFontSize.value * fontSizeMode.scaleFactor).sp

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("egw_reader_screen")
    ) {
        // Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column {
                        Text(
                            text = if (language == AppLanguage.SWAHILI) targetBook.titleSwahili else targetBook.titleEnglish,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                        Text(
                            text = "Ellen G. White • Sura ${activeChapter?.chapterNumber ?: 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Language Switcher
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clickable { showEnglish = !showEnglish }
                            .padding(end = 4.dp)
                    ) {
                        Text(
                            text = if (showEnglish) "EN" else "SW",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = RoyalBluePrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }

                    // Font Size dropdown
                    Box {
                        IconButton(onClick = { showFontMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.FormatSize,
                                contentDescription = "Font Size",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        DropdownMenu(
                            expanded = showFontMenu,
                            onDismissRequest = { showFontMenu = false }
                        ) {
                            FontSizeMode.entries.forEach { mode ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = if (language == AppLanguage.SWAHILI) mode.labelSwahili else mode.labelEnglish,
                                            fontWeight = if (fontSizeMode == mode) FontWeight.Bold else FontWeight.Normal,
                                            color = if (fontSizeMode == mode) GoldPrimary else MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        viewModel.setFontSizeMode(mode)
                                        showFontMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Bookmark
                    IconButton(
                        onClick = {
                            if (activeChapter != null) {
                                viewModel.toggleBookmark(
                                    id = "egw_${activeChapter.id}",
                                    type = ContentType.EGW,
                                    title = targetBook.titleSwahili,
                                    subtitle = activeChapter.titleSwahili
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Chapters chip row
        if (targetBook.chapters.size > 1) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(targetBook.chapters) { ch ->
                    val isSelected = (ch.id == activeChapter?.id)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) RoyalBluePrimary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { viewModel.selectEgwChapter(ch) }
                    ) {
                        Text(
                            text = "Sura ${ch.chapterNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Chapter Body
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (showEnglish)
                                    "Chapter ${activeChapter?.chapterNumber}: ${activeChapter?.titleEnglish}"
                                else
                                    "Sura ya ${activeChapter?.chapterNumber}: ${activeChapter?.titleSwahili}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            Row {
                                IconButton(
                                    onClick = {
                                        val text = if (showEnglish) activeChapter?.contentEnglish ?: "" else activeChapter?.contentSwahili ?: ""
                                        viewModel.startAudioPlayback(
                                            title = activeChapter?.titleSwahili ?: targetBook.titleSwahili,
                                            subtitle = targetBook.titleSwahili,
                                            totalSeconds = 150
                                        )
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }

                                IconButton(
                                    onClick = {
                                        val text = "${targetBook.titleSwahili} - ${activeChapter?.titleSwahili}\n\n${activeChapter?.contentSwahili}\n\n(Kupitia Sabbath Life)"
                                        val intent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, text)
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(intent, null))
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        val content = if (showEnglish)
                            activeChapter?.contentEnglish ?: ""
                        else
                            activeChapter?.contentSwahili ?: ""

                        Text(
                            text = content,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = dynamicSize,
                                lineHeight = (dynamicSize.value * 1.6f).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

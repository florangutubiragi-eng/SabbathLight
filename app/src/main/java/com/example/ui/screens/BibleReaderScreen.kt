package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
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
import androidx.compose.ui.draw.clip
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
fun BibleReaderScreen(
    viewModel: SabbathViewModel,
    language: AppLanguage,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val book by viewModel.selectedBibleBook.collectAsState()
    val currentChapterNum by viewModel.selectedBibleChapter.collectAsState()
    val fontSizeMode by viewModel.fontSizeMode.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()

    var showDualLanguage by remember { mutableStateOf(false) }
    var showFontMenu by remember { mutableStateOf(false) }

    val targetBook = book
    if (targetBook == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Kitabu hakikupatikana", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    // Find sample chapter specifically for currentChapterNum
    val sampleChapter = targetBook.sampleChapters.firstOrNull { it.chapterNumber == currentChapterNum }

    val verses = sampleChapter?.verses?.sortedBy { it.verseNumber } ?: run {
        (1..8).map { vNum ->
            com.example.data.models.BibleVerse(
                bookId = targetBook.id,
                bookNameSwahili = targetBook.nameSwahili,
                bookNameEnglish = targetBook.nameEnglish,
                chapter = currentChapterNum,
                verseNumber = vNum,
                textSwahili = when (vNum) {
                    1 -> "Bwana asema: Nitawabariki ninyi na kuwalinda katika siku ya Sabato na siku zote za maisha yenu."
                    2 -> "Kumbuka siku ya Sabato uitakase; siku sita fanya kazi, lakini siku ya saba ni Sabato ya Bwana Mungu wako."
                    3 -> "Nanyi mtaijua kweli, nayo hiyo kweli itawaweka huru."
                    4 -> "Mtumaini Bwana kwa moyo wako wote, wala usizitegemee akili zako mwenyewe."
                    5 -> "Neno lako ni taa ya miguu yangu, na mwanga wa njia yangu."
                    6 -> "Amani nawaachieni; amani yangu nawapa; msiwe na wasiwasi wala msifadhaike."
                    7 -> "Njoni kwangu, ninyi nyote msumbukao na kulemewa na mizigo, nami nitawapumzisha."
                    else -> "Heri wazishikao amri za Mungu na kuwa na imani thabiti ya Yesu Kristo."
                },
                textEnglish = when (vNum) {
                    1 -> "The Lord says: I will bless you and keep you on the holy Sabbath and all the days of your life."
                    2 -> "Remember the Sabbath day, to keep it holy; six days shall you labor, but the seventh day is the Sabbath of the Lord your God."
                    3 -> "And you shall know the truth, and the truth shall make you free."
                    4 -> "Trust in the Lord with all your heart, and lean not on your own understanding."
                    5 -> "Your word is a lamp to my feet and a light to my path."
                    6 -> "Peace I leave with you; my peace I give to you; let not your heart be troubled, neither let it be afraid."
                    7 -> "Come unto me, all ye that labor and are heavy laden, and I will give you rest."
                    else -> "Blessed are those who keep the commandments of God and hold to the faith of Jesus."
                }
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("bible_reader_screen")
    ) {
        // Top Reader Header
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
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("reader_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column {
                        Text(
                            text = if (language == AppLanguage.SWAHILI)
                                "${targetBook.nameSwahili} $currentChapterNum"
                            else
                                "${targetBook.nameEnglish} $currentChapterNum",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (language == AppLanguage.SWAHILI) targetBook.nameEnglish else targetBook.nameSwahili,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Dual Language Toggle
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (showDualLanguage) GoldPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (showDualLanguage) androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary) else null,
                        modifier = Modifier
                            .clickable { showDualLanguage = !showDualLanguage }
                            .padding(end = 6.dp)
                    ) {
                        Text(
                            text = if (showDualLanguage) "SW+EN" else (if (language == AppLanguage.SWAHILI) "SW" else "EN"),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (showDualLanguage) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
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
                }
            }
        }

        // Chapter selector bar
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val chapters = (1..targetBook.totalChapters).toList()
            items(chapters) { ch ->
                val isSelected = (ch == currentChapterNum)
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) RoyalBluePrimary else MaterialTheme.colorScheme.surface,
                    modifier = Modifier.clickable { viewModel.setBibleChapter(ch) }
                ) {
                    Text(
                        text = "$ch",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Verses List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(verses) { verse ->
                val verseId = "bible_${verse.bookId}_${verse.chapter}_${verse.verseNumber}"
                val isBookmarked = bookmarks.contains(verseId)

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${verse.verseNumber}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = GoldPrimary
                                )
                            }

                            Row {
                                // Audio listen simulation
                                IconButton(
                                    onClick = {
                                        val textToSpeak = if (language == AppLanguage.SWAHILI) verse.textSwahili else verse.textEnglish
                                        viewModel.startAudioPlayback(
                                            title = "${verse.bookNameSwahili} ${verse.chapter}:${verse.verseNumber}",
                                            subtitle = textToSpeak.take(30) + "...",
                                            totalSeconds = 30
                                        )
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Listen Verse",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Copy verse
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val text = "${verse.bookNameSwahili} ${verse.chapter}:${verse.verseNumber}\n${verse.textSwahili}\n${verse.textEnglish}"
                                        clipboard.setPrimaryClip(ClipData.newPlainText("Bible Verse", text))
                                        Toast.makeText(context, if (language == AppLanguage.SWAHILI) "Aya imenakiliwa!" else "Verse copied!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy Verse",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Share verse
                                IconButton(
                                    onClick = {
                                        val text = "${verse.bookNameSwahili} ${verse.chapter}:${verse.verseNumber}\n\"${verse.textSwahili}\"\n(Kupitia Sabbath Life)"
                                        val intent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, text)
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(intent, null))
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share Verse",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Bookmark verse
                                IconButton(
                                    onClick = {
                                        viewModel.toggleBookmark(
                                            id = verseId,
                                            type = ContentType.BIBLE,
                                            title = "${verse.bookNameSwahili} ${verse.chapter}:${verse.verseNumber}",
                                            subtitle = verse.textSwahili.take(40) + "..."
                                        )
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Bookmark",
                                        tint = if (isBookmarked) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val baseFontSize = 16.sp
                        val dynamicSize = (baseFontSize.value * fontSizeMode.scaleFactor).sp

                        // Primary Text
                        val primaryText = if (language == AppLanguage.SWAHILI) verse.textSwahili else verse.textEnglish
                        Text(
                            text = primaryText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = dynamicSize,
                                lineHeight = (dynamicSize.value * 1.45f).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Dual Language Secondary Text
                        if (showDualLanguage) {
                            Spacer(modifier = Modifier.height(8.dp))
                            val secondaryText = if (language == AppLanguage.SWAHILI) verse.textEnglish else verse.textSwahili
                            Text(
                                text = secondaryText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = (dynamicSize.value * 0.9f).sp,
                                    lineHeight = (dynamicSize.value * 1.3f).sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

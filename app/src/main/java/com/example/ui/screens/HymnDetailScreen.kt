package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import com.example.data.models.Hymn
import com.example.data.models.HymnCollection
import com.example.data.models.HymnBookType
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.data.models.FontSizeMode
import com.example.ui.components.Html5AudioPlayerView
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyDarkCard
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.viewmodel.SabbathViewModel

@Composable
fun HymnDetailScreen(
    viewModel: SabbathViewModel,
    language: AppLanguage,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val hymn by viewModel.selectedHymn.collectAsState()
    val favoriteHymns by viewModel.favoriteHymns.collectAsState()
    val fontSizeMode by viewModel.fontSizeMode.collectAsState()
    val audioState by viewModel.audioPlayerState.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()

    var showEnglishLyrics by remember { mutableStateOf(language == AppLanguage.ENGLISH) }
    var showFontMenu by remember { mutableStateOf(false) }
    var showAdminAuditDialog by remember { mutableStateOf(false) }

    val currentHymn = hymn
    if (currentHymn == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = if (language == AppLanguage.SWAHILI) "Wimbo haukuchaguliwa" else "No hymn selected",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        return
    }

    val isFavorite = favoriteHymns.contains(currentHymn.id)
    val verses = if (showEnglishLyrics && currentHymn.versesEnglish.isNotEmpty()) currentHymn.versesEnglish else currentHymn.versesSwahili
    val chorus = if (showEnglishLyrics && currentHymn.chorusEnglish != null) currentHymn.chorusEnglish else currentHymn.chorusSwahili

    val baseFontSize = 16.sp
    val dynamicSize = (baseFontSize.value * fontSizeMode.scaleFactor).sp

    val isThisHymnActive = audioState.currentHymn?.id == currentHymn.id
    val isPlaying = isThisHymnActive && audioState.isPlaying
    val isLoading = isThisHymnActive && audioState.isLoading
    val errorMessage = if (isThisHymnActive) audioState.errorMessage else null

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("hymn_detail_screen")
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("hymn_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column(modifier = Modifier.padding(end = 8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = GoldPrimary,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "${currentHymn.collection.shortCode} #${currentHymn.number}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF071324),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (showEnglishLyrics && currentHymn.titleEnglish.isNotBlank()) currentHymn.titleEnglish else currentHymn.titleSwahili,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = currentHymn.editionName.ifBlank { currentHymn.collection.editionTitle },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Admin Audit Button
                    IconButton(
                        onClick = { showAdminAuditDialog = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Catalogue Audit",
                            tint = if (isAdminMode) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Language Switcher
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clickable { showEnglishLyrics = !showEnglishLyrics }
                            .padding(end = 4.dp)
                    ) {
                        Text(
                            text = if (showEnglishLyrics) "EN" else "SW",
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

                    // Favorite Toggle
                    IconButton(onClick = { viewModel.toggleFavoriteHymn(currentHymn.id) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Enhanced Native Music Player Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyDarkCard)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(RoyalBluePrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.GraphicEq,
                                        contentDescription = null,
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "#${currentHymn.number} ${currentHymn.titleSwahili}",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                    if (currentHymn.artistOrComposer.isNotBlank()) {
                                        Text(
                                            text = "${currentHymn.artistOrComposer}${if (currentHymn.tuneName.isNotBlank()) " • Tune: ${currentHymn.tuneName}" else ""}",
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                            color = GoldPrimary,
                                            maxLines = 1
                                        )
                                    }
                                    if (currentHymn.scriptureRef.isNotBlank()) {
                                        Text(
                                            text = currentHymn.scriptureRef,
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                            color = Color(0xFF94A3B8)
                                        )
                                    }
                                }
                            }
                        }

                        // Error Banner if audio failed
                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0x33FF5252),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = null,
                                        tint = Color(0xFFFF6B6B),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = errorMessage,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFFFC5C5),
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { viewModel.retryCurrentAudio() },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Retry", tint = GoldPrimary, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }

                        // Progress slider
                        Spacer(modifier = Modifier.height(10.dp))
                        Slider(
                            value = if (isThisHymnActive) audioState.progress else 0f,
                            onValueChange = { frac ->
                                if (isThisHymnActive) {
                                    viewModel.seekAudio(frac)
                                }
                            },
                            valueRange = 0f..1f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = GoldPrimary,
                                activeTrackColor = GoldPrimary,
                                inactiveTrackColor = Color(0x33FFFFFF)
                            )
                        )

                        val currMin = if (isThisHymnActive) audioState.currentDurationSeconds / 60 else 0
                        val currSec = if (isThisHymnActive) audioState.currentDurationSeconds % 60 else 0
                        val totMin = if (isThisHymnActive) audioState.totalDurationSeconds / 60 else 0
                        val totSec = if (isThisHymnActive) audioState.totalDurationSeconds % 60 else 0
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = String.format("%02d:%02d", currMin, currSec),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFCBD5E1)
                            )
                            Text(
                                text = String.format("%02d:%02d", totMin, totSec),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFCBD5E1)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Controls: Previous, Play/Pause, Next
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(
                                onClick = { viewModel.playPreviousTrack() },
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x22FFFFFF))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "Previous Song",
                                    tint = Color.White
                                )
                            }

                            Button(
                                onClick = {
                                    if (isThisHymnActive) {
                                        viewModel.togglePlayPause()
                                    } else {
                                        viewModel.playHymn(currentHymn, viewModel.getAllHymns())
                                    }
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldPrimary,
                                    contentColor = Color(0xFF071324)
                                ),
                                modifier = Modifier
                                    .height(44.dp)
                                    .testTag("play_hymn_tune_button")
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color(0xFF071324),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Inapakia...", fontWeight = FontWeight.Bold)
                                } else {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (isPlaying) "Pause" else "Play",
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isPlaying) {
                                            if (language == AppLanguage.SWAHILI) "Simamisha" else "Pause"
                                        } else {
                                            if (language == AppLanguage.SWAHILI) "Cheza Sauti" else "Play Audio"
                                        },
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            IconButton(
                                onClick = { viewModel.playNextTrack() },
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x22FFFFFF))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "Next Song",
                                    tint = Color.White
                                )
                            }
                        }

                        // Interactive Volume Slider
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (audioState.volume <= 0.05f) Icons.Default.VolumeMute else Icons.Default.VolumeDown,
                                contentDescription = "Volume",
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Slider(
                                value = audioState.volume,
                                onValueChange = { viewModel.setAudioVolume(it) },
                                valueRange = 0f..1f,
                                modifier = Modifier.weight(1f),
                                colors = SliderDefaults.colors(
                                    thumbColor = GoldPrimary,
                                    activeTrackColor = GoldPrimary,
                                    inactiveTrackColor = Color(0x33FFFFFF)
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${(audioState.volume * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFCBD5E1),
                                modifier = Modifier.width(36.dp)
                            )
                        }

                        // Audio Source URL Display (Inspection)
                        if (currentHymn.audioUrl.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = currentHymn.audioUrl,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace
                                    ),
                                    color = Color(0xFF94A3B8),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Published Edition & Catalogue Verification Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Surface(
                                    color = GoldPrimary,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${currentHymn.collection.shortCode} #${currentHymn.number}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color(0xFF071324),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = currentHymn.editionName.ifBlank { currentHymn.collection.editionTitle },
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            if (currentHymn.isVerifiedEntry) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (language == AppLanguage.SWAHILI) "Imethibitishwa" else "Verified",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = GoldPrimary
                                    )
                                }
                            }
                        }

                        if (currentHymn.verificationNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = currentHymn.verificationNotes,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Katalogi: ${currentHymn.collection.titleSwahili}",
                                style = MaterialTheme.typography.labelSmall,
                                color = RoyalBluePrimary
                            )
                            if (isAdminMode) {
                                OutlinedButton(
                                    onClick = { showAdminAuditDialog = true },
                                    modifier = Modifier.height(30.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (language == AppLanguage.SWAHILI) "Sahihisha" else "Edit Entry", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            // HTML5 Audio Player Card (WebView-backed HTML5 <audio> component)
            if (currentHymn.audioUrl.isNotBlank()) {
                item {
                    Html5AudioPlayerView(
                        audioUrl = currentHymn.audioUrl,
                        title = "#${currentHymn.number} ${currentHymn.titleSwahili}",
                        subtitle = currentHymn.bookType.displayNameSwahili
                    )
                }
            }

            // Stanzas and Chorus
            items(verses.size) { index ->
                val stanzaNumber = index + 1
                val text = verses[index]

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(RoyalBluePrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$stanzaNumber",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = RoyalBluePrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "Ubeti $stanzaNumber" else "Stanza $stanzaNumber",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = dynamicSize,
                                lineHeight = (dynamicSize.value * 1.5f).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Show chorus after first stanza if available
                if (index == 0 && chorus != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldPrimary.copy(alpha = 0.6f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "KIITIKIO / CHORUS" else "CHORUS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = GoldPrimary,
                                    letterSpacing = 1.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = chorus,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = dynamicSize,
                                    lineHeight = (dynamicSize.value * 1.5f).sp,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Share & Copy Actions Footer
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val lyrics = buildString {
                                appendLine("#${currentHymn.number} ${currentHymn.titleSwahili}")
                                if (currentHymn.artistOrComposer.isNotBlank()) appendLine("Mtunzi: ${currentHymn.artistOrComposer}")
                                if (currentHymn.audioUrl.isNotBlank()) appendLine("Sauti: ${currentHymn.audioUrl}")
                                if (chorus != null) appendLine("Kiitikio: $chorus\n")
                                verses.forEachIndexed { i, v -> appendLine("${i + 1}. $v\n") }
                            }
                            clipboard.setPrimaryClip(ClipData.newPlainText("Hymn", lyrics))
                            Toast.makeText(context, if (language == AppLanguage.SWAHILI) "Maneno yamenakiliwa!" else "Lyrics copied!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.SWAHILI) "Nakili Maneno" else "Copy Lyrics",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = {
                            val lyrics = buildString {
                                appendLine("#${currentHymn.number} ${currentHymn.titleSwahili}")
                                verses.firstOrNull()?.let { appendLine("1. $it\n") }
                                if (chorus != null) appendLine("Kiitikio: $chorus")
                                if (currentHymn.audioUrl.isNotBlank()) appendLine("\nSikiliza Sauti: ${currentHymn.audioUrl}")
                                appendLine("\n(Kupitia Sabbath Life - Kuishi kwa Neno)")
                            }
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, lyrics)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(intent, null))
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.SWAHILI) "Shiriki" else "Share",
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Admin Audit Dialog
        if (showAdminAuditDialog) {
            AdminHymnDetailAuditDialog(
                hymn = currentHymn,
                language = language,
                onDismiss = { showAdminAuditDialog = false },
                onSaveCorrection = { corrected ->
                    viewModel.saveHymnCorrection(corrected)
                    showAdminAuditDialog = false
                },
                onResetToDefault = {
                    viewModel.resetHymnCorrection(currentHymn.id)
                    showAdminAuditDialog = false
                }
            )
        }
    }
}

@Composable
private fun AdminHymnDetailAuditDialog(
    hymn: Hymn,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSaveCorrection: (Hymn) -> Unit,
    onResetToDefault: () -> Unit
) {
    val isSwahili = language == AppLanguage.SWAHILI

    var numberStr by remember { mutableStateOf(hymn.number.toString()) }
    var titleSwahili by remember { mutableStateOf(hymn.titleSwahili) }
    var titleEnglish by remember { mutableStateOf(hymn.titleEnglish) }
    var category by remember { mutableStateOf(hymn.category) }
    var editionName by remember { mutableStateOf(hymn.editionName.ifBlank { hymn.collection.editionTitle }) }
    var artistComposer by remember { mutableStateOf(hymn.artistOrComposer) }
    var tuneName by remember { mutableStateOf(hymn.tuneName) }
    var scriptureRef by remember { mutableStateOf(hymn.scriptureRef) }
    var audioUrl by remember { mutableStateOf(hymn.audioUrl) }
    var isVerified by remember { mutableStateOf(hymn.isVerifiedEntry) }
    var verificationNotes by remember { mutableStateOf(hymn.verificationNotes) }
    var stanzasSwahiliText by remember { mutableStateOf(hymn.versesSwahili.joinToString("\n\n")) }
    var chorusSwahiliText by remember { mutableStateOf(hymn.chorusSwahili ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = GoldPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isSwahili) "Uhakiki: ${hymn.collection.shortCode} #${hymn.number}" else "Audit: ${hymn.collection.shortCode} #${hymn.number}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (isSwahili)
                        "Mkusanyiko: ${hymn.collection.titleSwahili}\nSahihisha nambari na maneno kulingana na toleo lililochapishwa."
                    else
                        "Collection: ${hymn.collection.titleEnglish}\nVerify and correct entry against physical published edition.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = numberStr,
                    onValueChange = { numberStr = it.filter { char -> char.isDigit() } },
                    label = { Text(if (isSwahili) "Nambari Rasmi ya Wimbo (${hymn.collection.shortCode}) *" else "Official Number *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = titleSwahili,
                    onValueChange = { titleSwahili = it },
                    label = { Text(if (isSwahili) "Jina la Wimbo (Kiswahili) *" else "Title (Swahili) *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = titleEnglish,
                    onValueChange = { titleEnglish = it },
                    label = { Text(if (isSwahili) "Jina kwa Kiingereza" else "Title (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = editionName,
                    onValueChange = { editionName = it },
                    label = { Text(if (isSwahili) "Toleo Lililochapishwa" else "Published Edition Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text(if (isSwahili) "Jamii / Kundi" else "Category") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = artistComposer,
                    onValueChange = { artistComposer = it },
                    label = { Text(if (isSwahili) "Mtunzi / Composer" else "Author / Composer") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = tuneName,
                    onValueChange = { tuneName = it },
                    label = { Text(if (isSwahili) "Melodi / Tune" else "Tune Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = scriptureRef,
                    onValueChange = { scriptureRef = it },
                    label = { Text(if (isSwahili) "Andiko la Marejeleo" else "Scripture Reference") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = audioUrl,
                    onValueChange = { audioUrl = it },
                    label = { Text(if (isSwahili) "URL ya Sauti (Audio Stream)" else "Audio Stream URL") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isVerified, onCheckedChange = { isVerified = it })
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSwahili) "Imethibitishwa (Verified)" else "Verified Entry",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                OutlinedTextField(
                    value = verificationNotes,
                    onValueChange = { verificationNotes = it },
                    label = { Text(if (isSwahili) "Maelezo ya Uhakiki / Ukaguzi" else "Verification Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                OutlinedTextField(
                    value = chorusSwahiliText,
                    onValueChange = { chorusSwahiliText = it },
                    label = { Text(if (isSwahili) "Kiitikio (Chorus)" else "Chorus") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                OutlinedTextField(
                    value = stanzasSwahiliText,
                    onValueChange = { stanzasSwahiliText = it },
                    label = { Text(if (isSwahili) "Betiza (Tenganisha kwa mstari mtupu)" else "Stanzas (Separate with blank line)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalNumber = numberStr.toIntOrNull() ?: hymn.number
                    val finalVerses = stanzasSwahiliText.split("\n\n").map { it.trim() }.filter { it.isNotBlank() }

                    val corrected = hymn.copy(
                        number = finalNumber,
                        titleSwahili = titleSwahili.trim(),
                        titleEnglish = titleEnglish.trim(),
                        editionName = editionName.trim(),
                        category = category.trim(),
                        artistOrComposer = artistComposer.trim(),
                        tuneName = tuneName.trim(),
                        scriptureRef = scriptureRef.trim(),
                        audioUrl = audioUrl.trim(),
                        isVerifiedEntry = isVerified,
                        verificationNotes = verificationNotes.trim(),
                        versesSwahili = if (finalVerses.isEmpty() && stanzasSwahiliText.isNotBlank()) listOf(stanzasSwahiliText.trim()) else finalVerses,
                        chorusSwahili = chorusSwahiliText.trim().takeIf { it.isNotBlank() }
                    )
                    onSaveCorrection(corrected)
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color(0xFF071324))
            ) {
                Text(if (isSwahili) "Hifadhi Marekebisho" else "Save Corrections", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Row {
                OutlinedButton(onClick = onResetToDefault) {
                    Text(if (isSwahili) "Rejesha Asili" else "Reset to Default")
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(onClick = onDismiss) {
                    Text(if (isSwahili) "Ghairi" else "Cancel")
                }
            }
        }
    )
}

package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.data.models.Sermon
import com.example.data.models.SermonType
import com.example.data.sample.SampleData
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.viewmodel.SabbathViewModel

@Composable
fun SermonsScreen(
    viewModel: SabbathViewModel,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val filterType by viewModel.sermonFilter.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedTopic by remember { mutableStateOf<String?>(null) }

    val sermons = SampleData.sampleSermons.filter { sermon ->
        val matchesType = (filterType == null || sermon.type == filterType)
        val matchesQuery = searchQuery.isBlank() ||
                sermon.titleSwahili.contains(searchQuery, ignoreCase = true) ||
                sermon.titleEnglish.contains(searchQuery, ignoreCase = true) ||
                sermon.speaker.contains(searchQuery, ignoreCase = true)
        val matchesTopic = selectedTopic == null ||
                sermon.topicSwahili.contains(selectedTopic!!, ignoreCase = true) ||
                sermon.topicEnglish.contains(selectedTopic!!, ignoreCase = true)

        matchesType && matchesQuery && matchesTopic
    }

    val topics = listOf(
        "Sabato",
        "Unabii",
        "Familia",
        "Afya"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("sermons_screen")
    ) {
        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("sermons_search_input"),
            placeholder = {
                Text(
                    text = if (language == AppLanguage.SWAHILI) "Tafuta mahubiri, mnenaji, au mada..." else "Search sermons, speaker, or topic...",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RoyalBluePrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        // Type Filter Chips: All, Audio, Video
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = filterType == null,
                onClick = { viewModel.setSermonFilter(null) },
                label = { Text(if (language == AppLanguage.SWAHILI) "Yote (All)" else "All") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.testTag("chip_all_sermons")
            )
            FilterChip(
                selected = filterType == SermonType.AUDIO,
                onClick = { viewModel.setSermonFilter(SermonType.AUDIO) },
                label = { Text("Audio") },
                leadingIcon = { Icon(Icons.Default.Audiotrack, contentDescription = null, modifier = Modifier.size(16.dp)) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.testTag("chip_audio_sermons")
            )
            FilterChip(
                selected = filterType == SermonType.VIDEO,
                onClick = { viewModel.setSermonFilter(SermonType.VIDEO) },
                label = { Text("Video") },
                leadingIcon = { Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(16.dp)) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.testTag("chip_video_sermons")
            )
        }

        // Topic chips row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(topics) { topic ->
                val isSelected = (selectedTopic == topic)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) GoldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clickable {
                        selectedTopic = if (isSelected) null else topic
                    }
                ) {
                    Text(
                        text = topic,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = if (isSelected) Color(0xFF071324) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Sermons List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sermons) { sermon ->
                SermonCard(
                    sermon = sermon,
                    language = language,
                    onPlayClick = {
                        if (sermon.type == SermonType.AUDIO) {
                            viewModel.openSermon(sermon)
                        } else {
                            viewModel.recordRecentlyViewed(
                                type = com.example.data.models.ContentType.SERMON,
                                title = sermon.titleSwahili,
                                subtitle = sermon.speaker,
                                targetId = sermon.id
                            )
                            // Open video player or fallback web intent
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=" + Uri.encode(sermon.titleSwahili)))
                            context.startActivity(intent)
                        }
                    },
                    onShareClick = {
                        val text = "${sermon.titleSwahili} (${sermon.speaker})\n${sermon.descriptionSwahili}\n(Kupitia Sabbath Life)"
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, text)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(intent, null))
                    }
                )
            }

            if (sermons.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (language == AppLanguage.SWAHILI) "Hakuna mahubiri yaliyopatikana" else "No sermons found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SermonCard(
    sermon: Sermon,
    language: AppLanguage,
    onPlayClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayClick() }
            .testTag("sermon_card_${sermon.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (sermon.type == SermonType.AUDIO) RoyalBluePrimary.copy(alpha = 0.15f) else Color(0xFFEF4444).copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (sermon.type == SermonType.AUDIO) Icons.Default.Audiotrack else Icons.Default.Videocam,
                            contentDescription = null,
                            tint = if (sermon.type == SermonType.AUDIO) RoyalBluePrimary else Color(0xFFEF4444),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = if (language == AppLanguage.SWAHILI) sermon.topicSwahili else sermon.topicEnglish,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Text(
                    text = "${sermon.durationMinutes} min • ${sermon.date}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.SWAHILI) sermon.titleSwahili else sermon.titleEnglish,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = sermon.speaker,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = GoldPrimary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (language == AppLanguage.SWAHILI) sermon.descriptionSwahili else sermon.descriptionEnglish,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onPlayClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (sermon.type == SermonType.AUDIO) RoyalBluePrimary else Color(0xFFDC2626)
                    ),
                    modifier = Modifier.testTag("play_sermon_${sermon.id}")
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (sermon.type == SermonType.AUDIO)
                            (if (language == AppLanguage.SWAHILI) "Sikiliza Hubiri" else "Listen Audio")
                        else
                            (if (language == AppLanguage.SWAHILI) "Tazama Video" else "Watch Video"),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                IconButton(onClick = onShareClick) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

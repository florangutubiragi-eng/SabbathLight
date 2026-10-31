package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.data.models.ContentType
import com.example.data.models.DownloadedItem
import com.example.ui.navigation.NavScreen
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyDarkCard
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.viewmodel.SabbathViewModel

private enum class DownloadFilterType {
    ALL, HYMNS, LESSONS, BIBLE_EGW
}

@Composable
fun DownloadsScreen(
    viewModel: SabbathViewModel,
    language: AppLanguage,
    onNavigate: (NavScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val downloads by viewModel.downloadedItems.collectAsState()
    var selectedFilter by remember { mutableStateOf(DownloadFilterType.ALL) }
    var itemToDelete by remember { mutableStateOf<DownloadedItem?>(null) }
    var showClearAllConfirm by remember { mutableStateOf(false) }

    val filteredList = downloads.filter { item ->
        when (selectedFilter) {
            DownloadFilterType.ALL -> true
            DownloadFilterType.HYMNS -> item.type == ContentType.HYMN
            DownloadFilterType.LESSONS -> item.type == ContentType.LESSON
            DownloadFilterType.BIBLE_EGW -> item.type == ContentType.BIBLE || item.type == ContentType.EGW
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("downloads_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Storage Status Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("downloads_storage_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDarkCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storage,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.SWAHILI) "Hifadhi ya Nje ya Mtandao" else "Offline Local Storage",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Text(
                                    text = if (language == AppLanguage.SWAHILI)
                                        "${downloads.size} Nyenzo zimehifadhiwa kwa Sabato"
                                    else
                                        "${downloads.size} items ready for offline Sabbath worship",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.2f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (language == AppLanguage.SWAHILI) "Inafanya Kazi" else "Offline Ready",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF10B981)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.addDownloadedItem(
                                    DownloadedItem(
                                        id = "dl_new_${System.currentTimeMillis()}",
                                        type = ContentType.HYMN,
                                        title = if (language == AppLanguage.SWAHILI) "Kifurushi Kipya cha Nyimbo za Sabato" else "New Sabbath Hymns Offline Pack",
                                        subtitle = if (language == AppLanguage.SWAHILI) "Nyimbo 10 za Nyimbo za Kristo" else "10 Hymns from Christ in Song",
                                        estimatedSize = "3.5 MB",
                                        downloadSource = "SDA Public Domain Archive"
                                    )
                                )
                                Toast.makeText(
                                    context,
                                    if (language == AppLanguage.SWAHILI) "Nyenzo imepakuliwa kwa mafanikio!" else "Pack downloaded successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = Color(0xFF071324)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "Pakua Zaidi" else "Download More",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        if (downloads.isNotEmpty()) {
                            OutlinedButton(
                                onClick = { showClearAllConfirm = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.White.copy(alpha = 0.85f)
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = if (language == AppLanguage.SWAHILI) "Safisha" else "Clear",
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedFilter == DownloadFilterType.ALL,
                        onClick = { selectedFilter = DownloadFilterType.ALL },
                        label = { Text(if (language == AppLanguage.SWAHILI) "Zote (${downloads.size})" else "All (${downloads.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = Color(0xFF071324)
                        )
                    )
                }
                item {
                    val count = downloads.count { it.type == ContentType.HYMN }
                    FilterChip(
                        selected = selectedFilter == DownloadFilterType.HYMNS,
                        onClick = { selectedFilter = DownloadFilterType.HYMNS },
                        label = { Text(if (language == AppLanguage.SWAHILI) "Nyimbo ($count)" else "Hymns ($count)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = Color(0xFF071324)
                        )
                    )
                }
                item {
                    val count = downloads.count { it.type == ContentType.LESSON }
                    FilterChip(
                        selected = selectedFilter == DownloadFilterType.LESSONS,
                        onClick = { selectedFilter = DownloadFilterType.LESSONS },
                        label = { Text(if (language == AppLanguage.SWAHILI) "Masomo ($count)" else "Lessons ($count)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = Color(0xFF071324)
                        )
                    )
                }
                item {
                    val count = downloads.count { it.type == ContentType.BIBLE || it.type == ContentType.EGW }
                    FilterChip(
                        selected = selectedFilter == DownloadFilterType.BIBLE_EGW,
                        onClick = { selectedFilter = DownloadFilterType.BIBLE_EGW },
                        label = { Text(if (language == AppLanguage.SWAHILI) "Biblia & EGW ($count)" else "Bible & EGW ($count)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = Color(0xFF071324)
                        )
                    )
                }
            }
        }

        // Items List
        if (filteredList.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.DownloadDone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (language == AppLanguage.SWAHILI) "Hakuna nyenzo katika kundi hili" else "No downloaded items in this category",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredList, key = { it.id }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("download_item_${item.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            val (icon, tint) = when (item.type) {
                                ContentType.HYMN -> Icons.Default.MusicNote to GoldPrimary
                                ContentType.LESSON -> Icons.Default.AutoStories to Color(0xFF10B981)
                                ContentType.BIBLE -> Icons.Default.MenuBook to RoyalBluePrimary
                                ContentType.EGW -> Icons.Default.AutoStories to Color(0xFF8B5CF6)
                                else -> Icons.Default.Storage to GoldPrimary
                            }

                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(tint.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Text(
                                            text = item.estimatedSize,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = item.downloadSource,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = GoldPrimary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    when (item.type) {
                                        ContentType.HYMN -> onNavigate(NavScreen.HYMNS)
                                        ContentType.LESSON -> onNavigate(NavScreen.LESSON)
                                        ContentType.BIBLE -> onNavigate(NavScreen.BIBLE)
                                        ContentType.EGW -> onNavigate(NavScreen.SPIRIT_OF_PROPHECY)
                                        else -> onNavigate(NavScreen.HOME)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Open item",
                                    tint = GoldPrimary
                                )
                            }

                            IconButton(
                                onClick = { itemToDelete = item }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete download",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Copyright & Legal Notice Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (language == AppLanguage.SWAHILI)
                            "Taarifa ya Hakimiliki: Sabbath Life inahifadhi nyenzo zilizo wazi kwa umma au zilizoidhinishwa kisheria (k.m. Nyimbo za Kristo za zamani, maandishi ya Ellen G. White yaliyo katika uwanda wa umma, na maelekezo ya Kanisa la Waadventista wa Sabato). Hatuhifadhi wala kusambaza nyenzo zenye hakimiliki binafsi bila kibali."
                        else
                            "Copyright & Authorization Notice: Sabbath Life caches only verified public-domain or authorized Seventh-day Adventist materials. We do not distribute copyrighted audio or media without express permission.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    // Confirmation dialog for deleting a single download
    itemToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = {
                Text(if (language == AppLanguage.SWAHILI) "Futa Nyenzo Hii?" else "Delete this Download?")
            },
            text = {
                Text(
                    if (language == AppLanguage.SWAHILI)
                        "Je, una uhakika unataka kufuta \"${item.title}\" kwenye hifadhi ya kifaa hiki?"
                    else
                        "Are you sure you want to delete \"${item.title}\" from your device storage?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.removeDownloadedItem(item.id)
                        itemToDelete = null
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.SWAHILI) "Nyenzo imefutwa" else "Item deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(if (language == AppLanguage.SWAHILI) "Futa" else "Delete", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { itemToDelete = null }) {
                    Text(if (language == AppLanguage.SWAHILI) "Ghairi" else "Cancel")
                }
            }
        )
    }

    // Confirmation dialog for clearing all downloads
    if (showClearAllConfirm) {
        AlertDialog(
            onDismissRequest = { showClearAllConfirm = false },
            title = {
                Text(if (language == AppLanguage.SWAHILI) "Futa Vipakuliwa Vyote?" else "Clear All Downloads?")
            },
            text = {
                Text(
                    if (language == AppLanguage.SWAHILI)
                        "Hii itafuta nyenzo zote zilizohifadhiwa nje ya mtandao kwenye kifaa chako."
                    else
                        "This will remove all offline cached materials from your device."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllDownloads()
                        showClearAllConfirm = false
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.SWAHILI) "Vipakuliwa vyote vimefutwa" else "All downloads cleared",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(if (language == AppLanguage.SWAHILI) "Futa Zote" else "Clear All", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showClearAllConfirm = false }) {
                    Text(if (language == AppLanguage.SWAHILI) "Ghairi" else "Cancel")
                }
            }
        )
    }
}

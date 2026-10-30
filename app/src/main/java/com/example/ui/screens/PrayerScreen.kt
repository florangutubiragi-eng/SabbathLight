package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.data.models.PrayerCategory
import com.example.data.models.PrayerItem
import com.example.data.sample.SampleData
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyDarkCard
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.util.AppStrings
import com.example.ui.viewmodel.SabbathViewModel

@Composable
fun PrayerScreen(
    viewModel: SabbathViewModel,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeCategory by viewModel.activePrayerCategory.collectAsState()
    val customPrayers by viewModel.customPrayers.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newDetails by remember { mutableStateOf("") }

    val categories = PrayerCategory.entries

    val guidedPrayers = SampleData.samplePrayers.filter { it.category == activeCategory }
    val displayPrayers = if (activeCategory == PrayerCategory.PERSONAL) {
        customPrayers + guidedPrayers
    } else {
        guidedPrayers
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("prayer_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Category Tabs
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(activeCategory),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = GoldPrimary,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    val index = categories.indexOf(activeCategory)
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                        color = GoldPrimary,
                        height = 3.dp
                    )
                }
            ) {
                categories.forEach { cat ->
                    val isSelected = (cat == activeCategory)
                    Tab(
                        selected = isSelected,
                        onClick = { viewModel.setActivePrayerCategory(cat) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val icon = when (cat) {
                                    PrayerCategory.MORNING -> Icons.Default.WbSunny
                                    PrayerCategory.MIDDAY -> Icons.Default.WbTwilight
                                    PrayerCategory.EVENING -> Icons.Default.NightsStay
                                    PrayerCategory.PERSONAL -> Icons.Default.VolunteerActivism
                                }
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.SWAHILI) cat.labelSwahili else cat.labelEnglish,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        modifier = Modifier.testTag("tab_prayer_${cat.name}")
                    )
                }
            }

            // Prayers list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Category Banner Card
                item {
                    val (bannerTitle, bannerSubtitle) = when (activeCategory) {
                        PrayerCategory.MORNING -> Pair(
                            if (language == AppLanguage.SWAHILI) "Maombi ya Asubuhi" else "Morning Prayers",
                            if (language == AppLanguage.SWAHILI) "Anza siku yako kwa kumshukuru Muumba wako na kuweka njia zako mikononi mwake." else "Begin your day consecrating your thoughts and steps to the Creator."
                        )
                        PrayerCategory.MIDDAY -> Pair(
                            if (language == AppLanguage.SWAHILI) "Maombi ya Mchana" else "Midday Prayers",
                            if (language == AppLanguage.SWAHILI) "Tulia katikati ya shughuli za siku na upokee amani na nguvu mpya kutoka kwa Kristo." else "Pause amid the day's tasks to draw fresh spiritual peace from Christ."
                        )
                        PrayerCategory.EVENING -> Pair(
                            if (language == AppLanguage.SWAHILI) "Maombi ya Jioni na Usiku" else "Evening & Night Prayers",
                            if (language == AppLanguage.SWAHILI) "Kiri fadhili za siku, tubu makosa, na ulale salama chini ya ulinzi wa malaika." else "Reflect on God's mercy, confess faults, and rest securely under holy angels."
                        )
                        PrayerCategory.PERSONAL -> Pair(
                            if (language == AppLanguage.SWAHILI) "Daftari la Maombi Binafsi" else "Personal Prayer Journal",
                            if (language == AppLanguage.SWAHILI) "Weka maombi yako, shukrani, na uone Mungu anavyojibu kila ombi lako!" else "Record your personal requests and praise God as He faithfully answers!"
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = NavyDarkCard)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = bannerTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = GoldPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = bannerSubtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                }

                // Prayer Cards
                items(displayPrayers) { prayer ->
                    PrayerCard(
                        prayer = prayer,
                        language = language,
                        onToggleAnswered = {
                            viewModel.togglePrayerAnswered(prayer.id)
                        },
                        onDelete = {
                            viewModel.deleteCustomPrayer(prayer.id)
                        },
                        onShare = {
                            val text = "${prayer.titleSwahili}\n\n\"${prayer.scripture}\" (${prayer.scriptureRef})\n\n${prayer.prayerTextSwahili}\n\n(Kupitia Sabbath Life)"
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, text)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(intent, null))
                        }
                    )
                }

                if (displayPrayers.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolunteerActivism,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "Bado hujaongeza ombi binafsi. Bonyeza kitufe hapo chini kuongeza!" else "No personal prayers added yet. Tap the button below to add one!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Button: Add Prayer Request
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_prayer_fab"),
            containerColor = GoldPrimary,
            contentColor = Color(0xFF071324)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Prayer")
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (language == AppLanguage.SWAHILI) "Ongeza Ombi" else "Add Request",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Add Prayer Dialog
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = {
                    Text(
                        text = if (language == AppLanguage.SWAHILI) "Ongeza Ombi la Maombi" else "Add Prayer Request",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text(if (language == AppLanguage.SWAHILI) "Kichwa cha Ombi" else "Prayer Title") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_prayer_title"),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = newDetails,
                            onValueChange = { newDetails = it },
                            label = { Text(if (language == AppLanguage.SWAHILI) "Maelezo / Nia ya Ombi" else "Prayer Details") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .testTag("input_prayer_details"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newTitle.isNotBlank()) {
                                viewModel.addCustomPrayer(
                                    titleSwahili = newTitle,
                                    titleEnglish = newTitle,
                                    prayerTextSwahili = newDetails,
                                    prayerTextEnglish = newDetails
                                )
                                newTitle = ""
                                newDetails = ""
                                showAddDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
                        modifier = Modifier.testTag("submit_prayer_button")
                    ) {
                        Text(if (language == AppLanguage.SWAHILI) "Hifadhi" else "Save")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showAddDialog = false }) {
                        Text(if (language == AppLanguage.SWAHILI) "Ghairi" else "Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun PrayerCard(
    prayer: PrayerItem,
    language: AppLanguage,
    onToggleAnswered: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("prayer_card_${prayer.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.SWAHILI) prayer.titleSwahili else prayer.titleEnglish,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (prayer.isUserCustom) {
                        // Answered checkbox toggle
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (prayer.isAnswered) Color(0xFF10B981).copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clickable { onToggleAnswered() }
                                .padding(end = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = if (prayer.isAnswered) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = "Mark Answered",
                                    tint = if (prayer.isAnswered) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (prayer.isAnswered)
                                        (if (language == AppLanguage.SWAHILI) "Imejibiwa!" else "Answered!")
                                    else
                                        (if (language == AppLanguage.SWAHILI) "Jibu bado" else "In prayer"),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (prayer.isAnswered) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                        }
                    }

                    IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // Scripture promise badge
            if (prayer.scripture.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RoyalBluePrimary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Ahadi ya Neno (${prayer.scriptureRef}):",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = RoyalBluePrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "\"${prayer.scripture}\"",
                            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Prayer words
            val text = if (language == AppLanguage.SWAHILI) prayer.prayerTextSwahili else prayer.prayerTextEnglish
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

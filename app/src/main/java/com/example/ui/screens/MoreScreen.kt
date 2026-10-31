package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.ui.navigation.NavScreen
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.util.AppStrings
import com.example.ui.viewmodel.SabbathViewModel

private data class MoreMenuItem(
    val titleSwahili: String,
    val titleEnglish: String,
    val subtitleSwahili: String,
    val subtitleEnglish: String,
    val icon: ImageVector,
    val iconTint: Color,
    val destination: NavScreen,
    val tag: String
)

@Composable
fun MoreScreen(
    viewModel: SabbathViewModel,
    language: AppLanguage,
    onNavigate: (NavScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        MoreMenuItem(
            titleSwahili = "Roho ya Unabii",
            titleEnglish = "Spirit of Prophecy",
            subtitleSwahili = "Maandishi ya Ellen G. White (Hatua kwa Kristo, n.k)",
            subtitleEnglish = "Ellen G. White writings (Steps to Christ, etc)",
            icon = Icons.Default.AutoStories,
            iconTint = Color(0xFF8B5CF6),
            destination = NavScreen.SPIRIT_OF_PROPHECY,
            tag = "more_item_egw"
        ),
        MoreMenuItem(
            titleSwahili = "Maisha ya Maombi",
            titleEnglish = "Prayer Life",
            subtitleSwahili = "Maombi ya asubuhi, mchana, jioni na maombi binafsi",
            subtitleEnglish = "Morning, midday, evening, and personal prayer log",
            icon = Icons.Default.VolunteerActivism,
            iconTint = Color(0xFFF59E0B),
            destination = NavScreen.PRAYER,
            tag = "more_item_prayer"
        ),
        MoreMenuItem(
            titleSwahili = "Hope Channel Tanzania",
            titleEnglish = "Hope Channel Tanzania",
            subtitleSwahili = "Matangazo mubashara na vipindi vya afya na injili",
            subtitleEnglish = "Live television broadcasts, health, and gospel shows",
            icon = Icons.Default.LiveTv,
            iconTint = Color(0xFFEF4444),
            destination = NavScreen.HOPE_CHANNEL,
            tag = "more_item_hope_channel"
        ),
        MoreMenuItem(
            titleSwahili = "Alama na Vipendwa",
            titleEnglish = "Bookmarks & Favorites",
            subtitleSwahili = "Aya, nyimbo, na maombi uliyohifadhi",
            subtitleEnglish = "Saved scriptures, favorite hymns, and prayers",
            icon = Icons.Default.Bookmark,
            iconTint = GoldPrimary,
            destination = NavScreen.BOOKMARKS,
            tag = "more_item_bookmarks"
        ),
        MoreMenuItem(
            titleSwahili = "Vipakuliwa na Hifadhi",
            titleEnglish = "Downloads & Offline Storage",
            subtitleSwahili = "Nyimbo, masomo na maandishi yaliyohifadhiwa nje ya mtandao",
            subtitleEnglish = "Hymns, lessons, and notes saved for offline access",
            icon = Icons.Default.CloudDownload,
            iconTint = Color(0xFF0284C7),
            destination = NavScreen.DOWNLOADS,
            tag = "more_item_downloads"
        ),
        MoreMenuItem(
            titleSwahili = "Mipangilio",
            titleEnglish = "Settings",
            subtitleSwahili = "Badili lugha, muonekano wa giza, na ukubwa wa maandishi",
            subtitleEnglish = "Change language, dark theme, and text size",
            icon = Icons.Default.Settings,
            iconTint = RoyalBluePrimary,
            destination = NavScreen.SETTINGS,
            tag = "more_item_settings"
        ),
        MoreMenuItem(
            titleSwahili = "Kuhusu Sabbath Life",
            titleEnglish = "About Sabbath Life",
            subtitleSwahili = "Dira yetu, imani ya Sabato na taarifa za programu",
            subtitleEnglish = "Vision, Sabbath faith pillars, and app information",
            icon = Icons.Default.Info,
            iconTint = Color(0xFF10B981),
            destination = NavScreen.ABOUT,
            tag = "more_item_about"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("more_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = if (language == AppLanguage.SWAHILI) "Huduma na Sehemu Nyingine" else "More Ministries & Sections",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        items(items) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(item.destination) }
                    .testTag(item.tag),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(item.iconTint.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = item.iconTint,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = if (language == AppLanguage.SWAHILI) item.titleSwahili else item.titleEnglish,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (language == AppLanguage.SWAHILI) item.subtitleSwahili else item.subtitleEnglish,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

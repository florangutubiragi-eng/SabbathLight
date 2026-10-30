package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.AppLanguage
import com.example.data.models.ContentType
import com.example.data.models.RecentlyViewedItem
import com.example.data.sample.SampleData
import com.example.ui.navigation.NavScreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainerLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyDarkCard
import com.example.ui.theme.NavyDarkSurface
import com.example.ui.theme.RoyalBlueDark
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.theme.RoyalBlueSoft
import com.example.ui.util.AppStrings
import com.example.ui.viewmodel.SabbathViewModel

@Composable
fun HomeScreen(
    viewModel: SabbathViewModel,
    language: AppLanguage,
    onNavigate: (NavScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dailyVerse = SampleData.dailyVerse
    val lesson = SampleData.currentQuarterlyLesson
    val bookmarks = viewModel.bookmarks.value
    val isVerseBookmarked = bookmarks.contains("daily_verse_${dailyVerse.chapter}_${dailyVerse.verseNumber}")
    val recentlyViewed = viewModel.recentlyViewed.value

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Banner with Image & Sabbath Greeting
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                // Background image
                Image(
                    painter = painterResource(id = R.drawable.sabbath_hero),
                    contentDescription = "Sabbath Sunrise and Bible",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay for readable text
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x77071324),
                                    Color(0xCC071324),
                                    Color(0xF0071324)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    // Gold pill badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = GoldPrimary.copy(alpha = 0.25f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.6f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "SABATO TAKATIFU" else "HOLY SABBATH",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = GoldPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = AppStrings.sabbathGreeting(language),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black
                        ),
                        color = Color.White
                    )

                    Text(
                        text = AppStrings.welcomeMessage(language),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                        color = Color(0xFFE2E8F0),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Daily Bible Verse Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .testTag("daily_verse_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant
                )
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
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.dailyVerseTitle(language),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Row {
                            IconButton(
                                onClick = {
                                    val verseRef = if (language == AppLanguage.SWAHILI)
                                        "${dailyVerse.bookNameSwahili} ${dailyVerse.chapter}:${dailyVerse.verseNumber}"
                                    else
                                        "${dailyVerse.bookNameEnglish} ${dailyVerse.chapter}:${dailyVerse.verseNumber}"
                                    val textToShare = if (language == AppLanguage.SWAHILI)
                                        "\"${dailyVerse.textSwahili}\" — $verseRef\n(Kupitia Sabbath Life App)"
                                    else
                                        "\"${dailyVerse.textEnglish}\" — $verseRef\n(Via Sabbath Life App)"

                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, textToShare)
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, null)
                                    context.startActivity(shareIntent)
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share Verse",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    viewModel.toggleBookmark(
                                        id = "daily_verse_${dailyVerse.chapter}_${dailyVerse.verseNumber}",
                                        type = ContentType.BIBLE,
                                        title = if (language == AppLanguage.SWAHILI) dailyVerse.bookNameSwahili else dailyVerse.bookNameEnglish,
                                        subtitle = "${dailyVerse.chapter}:${dailyVerse.verseNumber}"
                                    )
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isVerseBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark Verse",
                                    tint = if (isVerseBookmarked) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (language == AppLanguage.SWAHILI) "\"${dailyVerse.textSwahili}\"" else "\"${dailyVerse.textEnglish}\"",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            lineHeight = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.SWAHILI)
                                "${dailyVerse.bookNameSwahili} ${dailyVerse.chapter}:${dailyVerse.verseNumber}"
                            else
                                "${dailyVerse.bookNameEnglish} ${dailyVerse.chapter}:${dailyVerse.verseNumber}",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                        )

                        Text(
                            text = if (language == AppLanguage.SWAHILI) "Soma Sura Kamili →" else "Read Full Chapter →",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = RoyalBluePrimary
                            ),
                            modifier = Modifier.clickable {
                                val book = SampleData.sampleBooks.firstOrNull { it.id == dailyVerse.bookId }
                                if (book != null) {
                                    viewModel.openBibleBook(book, dailyVerse.chapter)
                                    onNavigate(NavScreen.BIBLE_CHAPTER_READER)
                                }
                            }
                        )
                    }
                }
            }
        }

        // Weekly Sabbath School Lesson Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { onNavigate(NavScreen.LESSON) }
                    .testTag("weekly_lesson_spotlight_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NavyDarkCard
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = RoyalBluePrimary
                        ) {
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "SOMO LA JUMA" else "WEEKLY LESSON",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = lesson.dateRange,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFCBD5E1)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (language == AppLanguage.SWAHILI) lesson.lessonTitleSwahili else lesson.lessonTitleEnglish,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (language == AppLanguage.SWAHILI) lesson.quarterTitleSwahili else lesson.quarterTitleEnglish,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = GoldPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x33000000),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33FFFFFF))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "Aya ya Kukariri (${lesson.memoryVerseReference}):" else "Memory Verse (${lesson.memoryVerseReference}):",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = GoldPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "\"${lesson.memoryVerseSwahili}\"" else "\"${lesson.memoryVerseEnglish}\"",
                                style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                                color = Color(0xFFF1F5F9),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onNavigate(NavScreen.LESSON) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("read_lesson_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color(0xFF071324)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.SWAHILI) "Soma Masomo 7 ya Juma" else "Study the 7 Daily Lessons",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // Section Title: Six Attractive Feature Cards
        item {
            PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            Text(
                text = AppStrings.quickFeaturesTitle(language),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // 6 Attractive Feature Cards Grid (2 rows of 3 columns)
        item {
            val features = listOf(
                FeatureCardData(
                    title = if (language == AppLanguage.SWAHILI) "Biblia" else "Bible",
                    subtitle = if (language == AppLanguage.SWAHILI) "Agano 2" else "2 Testaments",
                    icon = Icons.Default.MenuBook,
                    screen = NavScreen.BIBLE,
                    accentColor = RoyalBluePrimary,
                    tag = "card_biblia"
                ),
                FeatureCardData(
                    title = if (language == AppLanguage.SWAHILI) "Nyimbo" else "Hymns",
                    subtitle = if (language == AppLanguage.SWAHILI) "Za Kristo" else "Hymnal",
                    icon = Icons.Default.MusicNote,
                    screen = NavScreen.HYMNS,
                    accentColor = GoldPrimary,
                    tag = "card_nyimbo"
                ),
                FeatureCardData(
                    title = if (language == AppLanguage.SWAHILI) "Lesson" else "Lesson",
                    subtitle = if (language == AppLanguage.SWAHILI) "Siku 7" else "7 Days",
                    icon = Icons.Default.AutoStories,
                    screen = NavScreen.LESSON,
                    accentColor = Color(0xFF10B981),
                    tag = "card_lesson"
                ),
                FeatureCardData(
                    title = if (language == AppLanguage.SWAHILI) "Roho ya Unabii" else "Spirit of Prophecy",
                    subtitle = if (language == AppLanguage.SWAHILI) "E.G. White" else "E.G. White",
                    icon = Icons.Default.AutoStories,
                    screen = NavScreen.SPIRIT_OF_PROPHECY,
                    accentColor = Color(0xFF8B5CF6),
                    tag = "card_roho_unabii"
                ),
                FeatureCardData(
                    title = if (language == AppLanguage.SWAHILI) "Mahubiri" else "Sermons",
                    subtitle = if (language == AppLanguage.SWAHILI) "Sauti & Video" else "Audio & Video",
                    icon = Icons.Default.Mic,
                    screen = NavScreen.SERMONS,
                    accentColor = Color(0xFFEC4899),
                    tag = "card_mahubiri"
                ),
                FeatureCardData(
                    title = if (language == AppLanguage.SWAHILI) "Maombi" else "Prayer Life",
                    subtitle = if (language == AppLanguage.SWAHILI) "Kila Wakati" else "Daily Prayers",
                    icon = Icons.Default.VolunteerActivism,
                    screen = NavScreen.PRAYER,
                    accentColor = Color(0xFFF59E0B),
                    tag = "card_maombi"
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (i in 0..2) {
                        FeatureCardItem(
                            data = features[i],
                            onClick = { onNavigate(features[i].screen) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (i in 3..5) {
                        FeatureCardItem(
                            data = features[i],
                            onClick = { onNavigate(features[i].screen) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Hope Channel Tanzania Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LiveTv,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.hopeChannelSection(language),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Text(
                        text = if (language == AppLanguage.SWAHILI) "Tazama Yote →" else "View All →",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = RoyalBluePrimary
                        ),
                        modifier = Modifier.clickable { onNavigate(NavScreen.HOPE_CHANNEL) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(NavScreen.HOPE_CHANNEL) }
                        .testTag("hope_channel_home_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyDarkCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.hope_channel_banner),
                            contentDescription = "Hope Channel Tanzania Studio",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Dark gradient overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0x33000000),
                                            Color(0x99071324),
                                            Color(0xF0071324)
                                        )
                                    )
                                )
                        )

                        // Live indicator badge at top right
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFDC2626),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (language == AppLanguage.SWAHILI) "MUBASHARA" else "LIVE",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                    color = Color.White
                                )
                            }
                        }

                        // Bottom info
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(14.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "Hope Channel Tanzania" else "Hope Channel Tanzania",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = if (language == AppLanguage.SWAHILI)
                                    "Matangazo ya injili na afya masaa 24 • Bonyeza kutazama"
                                else
                                    "24/7 Gospel & health broadcast • Tap to stream",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }
        }

        // Recently Viewed Content (if any)
        if (recentlyViewed.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = AppStrings.recentlyViewed(language),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = if (language == AppLanguage.SWAHILI) "Futa" else "Clear",
                            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            modifier = Modifier.clickable { viewModel.clearHistory() }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(recentlyViewed) { item ->
                            RecentlyViewedCard(
                                item = item,
                                onClick = {
                                    when (item.type) {
                                        ContentType.BIBLE -> onNavigate(NavScreen.BIBLE)
                                        ContentType.HYMN -> onNavigate(NavScreen.HYMNS)
                                        ContentType.LESSON -> onNavigate(NavScreen.LESSON)
                                        ContentType.SERMON -> onNavigate(NavScreen.SERMONS)
                                        ContentType.EGW -> onNavigate(NavScreen.SPIRIT_OF_PROPHECY)
                                        ContentType.PRAYER -> onNavigate(NavScreen.PRAYER)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class FeatureCardData(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val screen: NavScreen,
    val accentColor: Color,
    val tag: String
)

@Composable
private fun FeatureCardItem(
    data: FeatureCardData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(115.dp)
            .clickable { onClick() }
            .testTag(data.tag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(data.accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = data.icon,
                    contentDescription = data.title,
                    tint = data.accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = data.title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Text(
                text = data.subtitle,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RecentlyViewedCard(
    item: RecentlyViewedItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = item.type.name,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    fontSize = 9.sp
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

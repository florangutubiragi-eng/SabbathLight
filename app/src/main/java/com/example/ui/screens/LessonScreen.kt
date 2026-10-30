package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.data.models.ContentType
import com.example.data.sample.SampleData
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyDarkCard
import com.example.ui.theme.RoyalBlueDark
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.theme.RoyalBlueSoft
import com.example.ui.util.AppStrings
import com.example.ui.viewmodel.SabbathViewModel

@Composable
fun LessonScreen(
    viewModel: SabbathViewModel,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lesson = SampleData.currentQuarterlyLesson
    val selectedDayIndex by viewModel.selectedDailyLessonIndex.collectAsState()
    val noteText by viewModel.studyNoteText.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val fontSizeMode by viewModel.fontSizeMode.collectAsState()

    val currentDaily = lesson.dailyLessons.getOrElse(selectedDayIndex) { lesson.dailyLessons[0] }
    val isBookmarked = bookmarks.contains("lesson_${currentDaily.id}")

    val baseFontSize = 16.sp
    val dynamicSize = (baseFontSize.value * fontSizeMode.scaleFactor).sp

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("lesson_screen"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Quarterly Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDarkCard)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = RoyalBluePrimary
                        ) {
                            Text(
                                text = "Somo #${lesson.lessonNumber}",
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
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (language == AppLanguage.SWAHILI) lesson.quarterTitleSwahili else lesson.quarterTitleEnglish,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = GoldPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Memory Verse box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x33000000),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (language == AppLanguage.SWAHILI)
                                    "Aya ya Kukariri (${lesson.memoryVerseReference}):"
                                else
                                    "Memory Text (${lesson.memoryVerseReference}):",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (language == AppLanguage.SWAHILI)
                                    "\"${lesson.memoryVerseSwahili}\""
                                else
                                    "\"${lesson.memoryVerseEnglish}\"",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFFF1F5F9)
                                )
                            )
                        }
                    }
                }
            }
        }

        // 7 Days Tabs (Scrollable)
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedDayIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = GoldPrimary,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedDayIndex]),
                        color = GoldPrimary,
                        height = 3.dp
                    )
                }
            ) {
                lesson.dailyLessons.forEachIndexed { index, day ->
                    val isSelected = (index == selectedDayIndex)
                    Tab(
                        selected = isSelected,
                        onClick = { viewModel.selectDailyLesson(index) },
                        text = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (language == AppLanguage.SWAHILI) day.dayNameSwahili else day.dayNameEnglish,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = day.dateString,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = if (isSelected) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        modifier = Modifier.testTag("tab_lesson_day_$index")
                    )
                }
            }
        }

        // Daily Lesson Details Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.SWAHILI) currentDaily.dayNameSwahili else currentDaily.dayNameEnglish,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalBluePrimary
                                )
                            )
                            Text(
                                text = if (language == AppLanguage.SWAHILI) currentDaily.titleSwahili else currentDaily.titleEnglish,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Row {
                            // Audio listen simulation
                            IconButton(
                                onClick = {
                                    val title = if (language == AppLanguage.SWAHILI) currentDaily.titleSwahili else currentDaily.titleEnglish
                                    viewModel.startAudioPlayback(
                                        title = "${currentDaily.dayNameSwahili}: $title",
                                        subtitle = "Somo la Sabato / Sabbath School",
                                        totalSeconds = 180
                                    )
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Listen",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Share lesson
                            IconButton(
                                onClick = {
                                    val text = "${currentDaily.dayNameSwahili} - ${currentDaily.titleSwahili}\n${currentDaily.contentSwahili}\n(Kupitia Sabbath Life)"
                                    val intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, text)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(intent, null))
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Bookmark lesson
                            IconButton(
                                onClick = {
                                    viewModel.toggleBookmark(
                                        id = "lesson_${currentDaily.id}",
                                        type = ContentType.LESSON,
                                        title = if (language == AppLanguage.SWAHILI) currentDaily.dayNameSwahili else currentDaily.dayNameEnglish,
                                        subtitle = if (language == AppLanguage.SWAHILI) currentDaily.titleSwahili else currentDaily.titleEnglish
                                    )
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (isBookmarked) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Scripture Reference Chips
                    if (currentDaily.scriptureReferences.isNotEmpty()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            items(currentDaily.scriptureReferences.size) { i ->
                                val ref = currentDaily.scriptureReferences[i]
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MenuBook,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = ref,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Content
                    Text(
                        text = if (language == AppLanguage.SWAHILI) currentDaily.contentSwahili else currentDaily.contentEnglish,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = dynamicSize,
                            lineHeight = (dynamicSize.value * 1.55f).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Discussion questions
                    val questions = if (language == AppLanguage.SWAHILI) currentDaily.questionsSwahili else currentDaily.questionsEnglish
                    if (questions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.HelpOutline,
                                        contentDescription = null,
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.SWAHILI) "Maswali ya Kutafakari na Kujadili:" else "Discussion & Reflection Questions:",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                questions.forEachIndexed { idx, q ->
                                    Text(
                                        text = "${idx + 1}. $q",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = (dynamicSize.value * 0.95f).sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Interactive Personal Study Notes Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == AppLanguage.SWAHILI)
                            "Maelezo Yako Binafsi ya Somo (${currentDaily.dayNameSwahili})"
                        else
                            "Your Personal Study Notes (${currentDaily.dayNameEnglish})",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == AppLanguage.SWAHILI)
                            "Andika tafakari yako, ufahamu mpya, au maamuzi ya kiroho hapa:"
                        else
                            "Record your reflections, key takeaways, or spiritual decisions here:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { viewModel.updateStudyNote(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("lesson_study_note_input"),
                        placeholder = {
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "Andika maelezo yako hapa..." else "Type your notes here...",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalBluePrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                viewModel.updateStudyNote(noteText)
                                Toast.makeText(
                                    context,
                                    if (language == AppLanguage.SWAHILI) "Maelezo yamehifadhiwa salama!" else "Study notes saved successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
                            modifier = Modifier.testTag("save_study_notes_button")
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "Hifadhi Maelezo" else "Save Notes",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

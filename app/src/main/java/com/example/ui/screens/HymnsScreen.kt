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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.data.models.Hymn
import com.example.data.models.HymnBookType
import com.example.data.models.HymnCollection
import com.example.ui.navigation.NavScreen
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyDarkCard
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.viewmodel.SabbathViewModel

private enum class HymnFilterMode {
    ALL, FAVORITES, RECENTLY_PLAYED, VERIFIED_ONLY
}

@Composable
fun HymnsScreen(
    viewModel: SabbathViewModel,
    language: AppLanguage,
    onNavigate: (NavScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.hymnSearch.collectAsState()
    val favoriteHymns by viewModel.favoriteHymns.collectAsState()
    val recentlyPlayedIds by viewModel.recentlyPlayedHymnIds.collectAsState()
    val audioState by viewModel.audioPlayerState.collectAsState()
    val customHymns by viewModel.customHymns.collectAsState()
    val adminCorrections by viewModel.adminCorrections.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()

    var filterMode by remember { mutableStateOf(HymnFilterMode.ALL) }
    // 0: All, 1: NZK (Swahili), 2: SDA Hymnal 1985 (English), 3: Other Collections, 4: Choir Songs
    var selectedCollectionIndex by remember { mutableIntStateOf(0) }
    var selectedLanguageFilter by remember { mutableStateOf<String?>(null) } // null: All, "sw": Swahili, "en": English
    var showAddSongDialog by remember { mutableStateOf(false) }
    var editingHymnForAdmin by remember { mutableStateOf<Hymn?>(null) }

    // Combined catalogue with applied admin corrections and custom hymns
    val allAvailableHymns = remember(customHymns, adminCorrections) {
        viewModel.getAllHymns()
    }

    val activeCollection = when (selectedCollectionIndex) {
        1 -> HymnCollection.NYIMBO_ZA_KRISTO
        2 -> HymnCollection.SDA_HYMNAL_1985
        3 -> HymnCollection.OTHER_ADVENTIST
        4 -> HymnCollection.CHOIR_SONGS
        else -> null
    }

    val filteredHymns = allAvailableHymns.filter { hymn ->
        val matchesCollection = if (activeCollection != null) {
            hymn.collection == activeCollection
        } else {
            true
        }

        val matchesLanguage = when (selectedLanguageFilter) {
            "sw" -> hymn.languageCode == "sw" || hymn.versesSwahili.isNotEmpty()
            "en" -> hymn.languageCode == "en" || hymn.versesEnglish.isNotEmpty()
            else -> true
        }

        val matchesFilter = when (filterMode) {
            HymnFilterMode.ALL -> true
            HymnFilterMode.FAVORITES -> favoriteHymns.contains(hymn.id)
            HymnFilterMode.RECENTLY_PLAYED -> recentlyPlayedIds.contains(hymn.id)
            HymnFilterMode.VERIFIED_ONLY -> hymn.isVerifiedEntry
        }

        val query = searchQuery.trim()
        val matchesQuery = query.isBlank() ||
                hymn.number.toString() == query ||
                hymn.number.toString().contains(query) ||
                "${hymn.collection.shortCode} ${hymn.number}".contains(query, ignoreCase = true) ||
                "${hymn.collection.shortCode}${hymn.number}".contains(query, ignoreCase = true) ||
                hymn.titleSwahili.contains(query, ignoreCase = true) ||
                hymn.titleEnglish.contains(query, ignoreCase = true) ||
                hymn.category.contains(query, ignoreCase = true) ||
                hymn.artistOrComposer.contains(query, ignoreCase = true) ||
                hymn.tuneName.contains(query, ignoreCase = true)

        matchesCollection && matchesLanguage && matchesFilter && matchesQuery
    }.let { list ->
        if (filterMode == HymnFilterMode.RECENTLY_PLAYED) {
            // Sort by recently played order
            list.sortedBy { recentlyPlayedIds.indexOf(it.id).let { idx -> if (idx >= 0) idx else 999 } }
        } else {
            // Strictly arrange every collection by its official hymn number in ascending order
            // If on 'All', sort by collection ordinal first, then ascending official number
            list.sortedWith(compareBy({ it.collection.ordinal }, { it.number }))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("hymns_screen"),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddSongDialog = true },
                containerColor = GoldPrimary,
                contentColor = Color(0xFF071324),
                shape = CircleShape,
                modifier = Modifier.testTag("add_custom_hymn_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = if (language == AppLanguage.SWAHILI) "Ongeza Wimbo" else "Add Hymn"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Search & Admin Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setHymnSearch(it) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hymn_search_input"),
                    placeholder = {
                        Text(
                            text = if (language == AppLanguage.SWAHILI) "Tafuta nambari (mf. 73, 100) au jina..." else "Search hymn # (e.g. 73, 100) or title...",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.setHymnSearch("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoyalBluePrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Admin Catalogue Mode Toggle Button
                IconButton(
                    onClick = { viewModel.toggleAdminMode() },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isAdminMode) GoldPrimary else MaterialTheme.colorScheme.surfaceVariant)
                        .testTag("admin_mode_toggle_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Catalogue Mode",
                        tint = if (isAdminMode) Color(0xFF071324) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Collection Tabs (4 Separate Verified Seventh-day Adventist Hymnal Collections)
            ScrollableTabRow(
                selectedTabIndex = selectedCollectionIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = GoldPrimary,
                edgePadding = 12.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedCollectionIndex]),
                        color = GoldPrimary,
                        height = 3.dp
                    )
                }
            ) {
                val collectionTabs = listOf(
                    if (language == AppLanguage.SWAHILI) "Mikusanyiko Yote" else "All Collections",
                    "Nyimbo za Kristo (NZK)",
                    "SDA Hymnal 1985 (SDAH)",
                    "Mikusanyiko Mingine (OAC)",
                    "Nyimbo za Kwaya (Choir)"
                )
                collectionTabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedCollectionIndex == index,
                        onClick = { selectedCollectionIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedCollectionIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedCollectionIndex == index) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.testTag("tab_collection_$index")
                    )
                }
            }

            // Filter Chips Row (Language filters + Status filters)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Language filters
                item {
                    FilterChip(
                        selected = selectedLanguageFilter == null,
                        onClick = { selectedLanguageFilter = null },
                        label = { Text(if (language == AppLanguage.SWAHILI) "Lugha Zote" else "All Languages", style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer)
                    )
                }
                item {
                    FilterChip(
                        selected = selectedLanguageFilter == "sw",
                        onClick = { selectedLanguageFilter = if (selectedLanguageFilter == "sw") null else "sw" },
                        label = { Text("Kiswahili", style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer)
                    )
                }
                item {
                    FilterChip(
                        selected = selectedLanguageFilter == "en",
                        onClick = { selectedLanguageFilter = if (selectedLanguageFilter == "en") null else "en" },
                        label = { Text("English", style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer)
                    )
                }

                // Status modes
                item {
                    FilterChip(
                        selected = filterMode == HymnFilterMode.ALL,
                        onClick = { filterMode = HymnFilterMode.ALL },
                        label = {
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "Zote (${filteredHymns.size})" else "All (${filteredHymns.size})",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.LibraryMusic, contentDescription = null, modifier = Modifier.size(14.dp))
                        },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer)
                    )
                }

                item {
                    FilterChip(
                        selected = filterMode == HymnFilterMode.FAVORITES,
                        onClick = { filterMode = HymnFilterMode.FAVORITES },
                        label = {
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "Vipendwa (${favoriteHymns.size})" else "Favorites (${favoriteHymns.size})",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (filterMode == HymnFilterMode.FAVORITES) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if (filterMode == HymnFilterMode.FAVORITES) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer)
                    )
                }

                item {
                    FilterChip(
                        selected = filterMode == HymnFilterMode.RECENTLY_PLAYED,
                        onClick = { filterMode = HymnFilterMode.RECENTLY_PLAYED },
                        label = {
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "Majuzi (${recentlyPlayedIds.size})" else "Recent (${recentlyPlayedIds.size})",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.History, contentDescription = null, modifier = Modifier.size(14.dp))
                        },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer)
                    )
                }

                item {
                    FilterChip(
                        selected = filterMode == HymnFilterMode.VERIFIED_ONLY,
                        onClick = { filterMode = if (filterMode == HymnFilterMode.VERIFIED_ONLY) HymnFilterMode.ALL else HymnFilterMode.VERIFIED_ONLY },
                        label = {
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "Zilizohakikiwa Pekee" else "Verified Only",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                        },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer)
                    )
                }
            }

            // Collection Edition Information & Audit Status Banner
            if (activeCollection != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Surface(
                                    color = GoldPrimary,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = activeCollection.shortCode,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color(0xFF071324),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = activeCollection.editionTitle,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (language == AppLanguage.SWAHILI)
                                "Taarifa ya Katalogi: Nyimbo zimepangwa kwa nambari rasmi inayopanda. Mkusanyiko haujatangazwa kuwa kamili hadi ukaguzi wa matoleo yaliyochapishwa ukamilike."
                            else
                                "Catalogue Notice: Arranged strictly by ascending official hymn numbers. Not claimed to be complete until fully checked against physical print editions.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Admin Mode Active Banner
            if (isAdminMode) {
                Surface(
                    color = GoldPrimary.copy(alpha = 0.15f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "Modi ya Msimamizi: Uhakiki na Marekebisho ya Katalogi" else "Admin Mode: Catalogue Verification & Corrections",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = GoldPrimary
                            )
                        }
                        if (adminCorrections.isNotEmpty()) {
                            Text(
                                text = "${adminCorrections.size} zilizorekebishwa",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Hymns List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredHymns, key = { it.id }) { hymn ->
                    val isFav = favoriteHymns.contains(hymn.id)
                    val isPlaying = audioState.isPlaying && audioState.currentHymn?.id == hymn.id
                    val isLoading = audioState.isLoading && audioState.currentHymn?.id == hymn.id

                    HymnCard(
                        hymn = hymn,
                        isFavorite = isFav,
                        isPlaying = isPlaying,
                        isLoading = isLoading,
                        isAdminMode = isAdminMode,
                        showEnglishPrimary = (hymn.collection == HymnCollection.SDA_HYMNAL_1985 || selectedLanguageFilter == "en"),
                        onHymnClick = {
                            viewModel.openHymn(hymn)
                            onNavigate(NavScreen.HYMN_DETAIL)
                        },
                        onPlayClick = {
                            if (isPlaying) {
                                viewModel.togglePlayPause()
                            } else {
                                viewModel.playHymn(hymn, filteredHymns)
                            }
                        },
                        onFavoriteToggle = {
                            viewModel.toggleFavoriteHymn(hymn.id)
                        },
                        onAdminEditClick = {
                            editingHymnForAdmin = hymn
                        }
                    )
                }

                if (filteredHymns.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (language == AppLanguage.SWAHILI) "Hakuna wimbo uliopatikana kwenye katalogi hii" else "No hymns found in this catalogue",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(onClick = {
                                filterMode = HymnFilterMode.ALL
                                selectedCollectionIndex = 0
                                selectedLanguageFilter = null
                                viewModel.setHymnSearch("")
                            }) {
                                Text(if (language == AppLanguage.SWAHILI) "Onyesha Mikusanyiko Yote" else "Show All Collections")
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Custom Hymn Dialog (Allows adding a new hymn entry into any collection)
    if (showAddSongDialog) {
        val nextNum = (filteredHymns.maxOfOrNull { it.number } ?: 300) + 1
        AddCustomHymnDialog(
            language = language,
            suggestedNextNumber = nextNum,
            defaultCollection = activeCollection ?: HymnCollection.NYIMBO_ZA_KRISTO,
            onDismiss = { showAddSongDialog = false },
            onAddHymn = { number, titleSwahili, titleEnglish, category, collection, editionName, verses, chorus, versesEn, chorusEn, artist, tune, scripture, audioUrl, verified, notes ->
                viewModel.addCustomHymn(
                    number = number,
                    titleSwahili = titleSwahili,
                    titleEnglish = titleEnglish,
                    category = category,
                    bookType = when (collection) {
                        HymnCollection.NYIMBO_ZA_KRISTO -> HymnBookType.NYIMBO_ZA_KRISTO
                        HymnCollection.SDA_HYMNAL_1985 -> HymnBookType.SDA_HYMNAL_1985
                        HymnCollection.OTHER_ADVENTIST -> HymnBookType.NYIMBO_ZA_KISABATO
                        HymnCollection.CHOIR_SONGS -> HymnBookType.CHOIR_SONGS
                    },
                    collection = collection,
                    editionName = editionName,
                    isVerifiedEntry = verified,
                    verificationNotes = notes,
                    languageCode = collection.languageCode,
                    versesSwahili = verses,
                    chorusSwahili = chorus,
                    versesEnglish = versesEn,
                    chorusEnglish = chorusEn,
                    artistOrComposer = artist,
                    tuneName = tune,
                    scriptureRef = scripture,
                    audioUrl = audioUrl
                )
                showAddSongDialog = false
            }
        )
    }

    // Admin Catalogue Correction & Verification Dialog
    editingHymnForAdmin?.let { hymnToEdit ->
        AdminHymnAuditDialog(
            hymn = hymnToEdit,
            language = language,
            onDismiss = { editingHymnForAdmin = null },
            onSaveCorrection = { correctedHymn ->
                viewModel.saveHymnCorrection(correctedHymn)
                editingHymnForAdmin = null
            },
            onResetToDefault = {
                viewModel.resetHymnCorrection(hymnToEdit.id)
                editingHymnForAdmin = null
            }
        )
    }
}

@Composable
private fun HymnCard(
    hymn: Hymn,
    isFavorite: Boolean,
    isPlaying: Boolean,
    isLoading: Boolean = false,
    isAdminMode: Boolean = false,
    showEnglishPrimary: Boolean,
    onHymnClick: () -> Unit,
    onPlayClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onAdminEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onHymnClick() }
            .testTag("hymn_card_${hymn.collection.shortCode}_${hymn.number}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying || isLoading) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPlaying || isLoading) GoldPrimary else MaterialTheme.colorScheme.outlineVariant
        )
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
                // Official Number badge with Collection Short Code
                Column(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isPlaying || isLoading) GoldPrimary else GoldPrimary.copy(alpha = 0.15f)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = hymn.collection.shortCode,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                        color = if (isPlaying || isLoading) Color(0xFF071324) else GoldPrimary
                    )
                    Text(
                        text = "#${hymn.number}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                        color = if (isPlaying || isLoading) Color(0xFF071324) else GoldPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    val primaryTitle = if (showEnglishPrimary && hymn.titleEnglish.isNotBlank()) hymn.titleEnglish else hymn.titleSwahili
                    val secondaryTitle = if (showEnglishPrimary) hymn.titleSwahili else hymn.titleEnglish

                    Text(
                        text = primaryTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (secondaryTitle.isNotBlank()) {
                        Text(
                            text = secondaryTitle,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    val subtitleParts = buildList {
                        add(hymn.collection.titleSwahili)
                        if (hymn.category.isNotBlank()) add(hymn.category)
                        if (hymn.artistOrComposer.isNotBlank()) add(hymn.artistOrComposer)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = subtitleParts.joinToString(" • "),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                color = RoyalBluePrimary
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (hymn.isVerifiedEntry) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified Catalogue Entry",
                                tint = GoldPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Admin quick-edit button
                if (isAdminMode) {
                    IconButton(
                        onClick = onAdminEditClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Catalogue Entry",
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Quick Play Button
                IconButton(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isPlaying || isLoading) GoldPrimary else RoyalBluePrimary.copy(alpha = 0.12f))
                        .testTag("quick_play_${hymn.collection.shortCode}_${hymn.number}")
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color(0xFF071324),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play Hymn",
                            tint = if (isPlaying) Color(0xFF071324) else RoyalBluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Favorite Toggle
                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Toggle Favorite",
                        tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Admin Function for Correcting and Verifying Catalogue Entries:
 * Allows inspecting published edition details, official hymn numbers, titles, categories,
 * audio URLs, stanzas, and saving audit notes.
 */
@Composable
private fun AdminHymnAuditDialog(
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
    var selectedCollection by remember { mutableStateOf(hymn.collection) }
    var editionName by remember { mutableStateOf(hymn.editionName.ifBlank { hymn.collection.editionTitle }) }
    var artistComposer by remember { mutableStateOf(hymn.artistOrComposer) }
    var tuneName by remember { mutableStateOf(hymn.tuneName) }
    var scriptureRef by remember { mutableStateOf(hymn.scriptureRef) }
    var audioUrl by remember { mutableStateOf(hymn.audioUrl) }
    var isVerified by remember { mutableStateOf(hymn.isVerifiedEntry) }
    var verificationNotes by remember { mutableStateOf(hymn.verificationNotes) }
    var stanzasSwahiliText by remember { mutableStateOf(hymn.versesSwahili.joinToString("\n\n")) }
    var chorusSwahiliText by remember { mutableStateOf(hymn.chorusSwahili ?: "") }
    var stanzasEnglishText by remember { mutableStateOf(hymn.versesEnglish.joinToString("\n\n")) }
    var chorusEnglishText by remember { mutableStateOf(hymn.chorusEnglish ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = GoldPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isSwahili) "Uhakiki wa Katalogi (${hymn.collection.shortCode} #${hymn.number})" else "Catalogue Audit (${hymn.collection.shortCode} #${hymn.number})",
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
                        "Kazi ya Msimamizi: Sahihisha na uhakiki taarifa za wimbo kwa mujibu wa toleo halisi lililochapishwa."
                    else
                        "Admin Function: Verify and correct catalogue entry against official published print edition.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Collection Selector Chips
                Text(
                    text = if (isSwahili) "Mkusanyiko / Kitabu cha Nyimbo:" else "Hymnal Collection:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    HymnCollection.values().forEach { col ->
                        FilterChip(
                            selected = selectedCollection == col,
                            onClick = {
                                selectedCollection = col
                                editionName = col.editionTitle
                            },
                            label = { Text(col.shortCode, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                OutlinedTextField(
                    value = numberStr,
                    onValueChange = { numberStr = it.filter { char -> char.isDigit() } },
                    label = { Text(if (isSwahili) "Nambari Rasmi ya Wimbo *" else "Official Hymn Number *") },
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
                    label = { Text(if (isSwahili) "Jina kwa Kiingereza (English)" else "Title (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = editionName,
                    onValueChange = { editionName = it },
                    label = { Text(if (isSwahili) "Toleo Lililochapishwa (Edition)" else "Published Edition Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text(if (isSwahili) "Jamii / Kundi (Category)" else "Category") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = artistComposer,
                    onValueChange = { artistComposer = it },
                    label = { Text(if (isSwahili) "Mtunzi / Mtunzi wa Nyimbo" else "Author / Composer") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = tuneName,
                    onValueChange = { tuneName = it },
                    label = { Text(if (isSwahili) "Melodi / Jina la Tune" else "Tune Name") },
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

                // Verification Checkbox & Notes
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isVerified,
                        onCheckedChange = { isVerified = it }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSwahili) "Imethibitishwa na Msimamizi (Verified)" else "Marked as Verified Entry",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                OutlinedTextField(
                    value = verificationNotes,
                    onValueChange = { verificationNotes = it },
                    label = { Text(if (isSwahili) "Maelezo ya Uhakiki (Audit Notes)" else "Verification / Audit Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                OutlinedTextField(
                    value = chorusSwahiliText,
                    onValueChange = { chorusSwahiliText = it },
                    label = { Text(if (isSwahili) "Kiitikio (Chorus - Kiswahili)" else "Chorus (Swahili)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                OutlinedTextField(
                    value = stanzasSwahiliText,
                    onValueChange = { stanzasSwahiliText = it },
                    label = { Text(if (isSwahili) "Betiza za Kiswahili (Tenganisha kwa mstari mtupu)" else "Swahili Stanzas") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                OutlinedTextField(
                    value = chorusEnglishText,
                    onValueChange = { chorusEnglishText = it },
                    label = { Text(if (isSwahili) "Kiitikio cha Kiingereza (Chorus - English)" else "Chorus (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                OutlinedTextField(
                    value = stanzasEnglishText,
                    onValueChange = { stanzasEnglishText = it },
                    label = { Text(if (isSwahili) "Betiza za Kiingereza (English Stanzas)" else "English Stanzas") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalNumber = numberStr.toIntOrNull() ?: hymn.number
                    val finalVersesSwahili = stanzasSwahiliText.split("\n\n").map { it.trim() }.filter { it.isNotBlank() }
                    val finalVersesEnglish = stanzasEnglishText.split("\n\n").map { it.trim() }.filter { it.isNotBlank() }

                    val corrected = hymn.copy(
                        number = finalNumber,
                        titleSwahili = titleSwahili.trim(),
                        titleEnglish = titleEnglish.trim(),
                        collection = selectedCollection,
                        bookType = when (selectedCollection) {
                            HymnCollection.NYIMBO_ZA_KRISTO -> HymnBookType.NYIMBO_ZA_KRISTO
                            HymnCollection.SDA_HYMNAL_1985 -> HymnBookType.SDA_HYMNAL_1985
                            HymnCollection.OTHER_ADVENTIST -> HymnBookType.NYIMBO_ZA_KISABATO
                            HymnCollection.CHOIR_SONGS -> HymnBookType.CHOIR_SONGS
                        },
                        editionName = editionName.trim(),
                        category = category.trim(),
                        artistOrComposer = artistComposer.trim(),
                        tuneName = tuneName.trim(),
                        scriptureRef = scriptureRef.trim(),
                        audioUrl = audioUrl.trim(),
                        isVerifiedEntry = isVerified,
                        verificationNotes = verificationNotes.trim(),
                        versesSwahili = if (finalVersesSwahili.isEmpty() && stanzasSwahiliText.isNotBlank()) listOf(stanzasSwahiliText.trim()) else finalVersesSwahili,
                        chorusSwahili = chorusSwahiliText.trim().takeIf { it.isNotBlank() },
                        versesEnglish = if (finalVersesEnglish.isEmpty() && stanzasEnglishText.isNotBlank()) listOf(stanzasEnglishText.trim()) else finalVersesEnglish,
                        chorusEnglish = chorusEnglishText.trim().takeIf { it.isNotBlank() }
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

@Composable
private fun AddCustomHymnDialog(
    language: AppLanguage,
    suggestedNextNumber: Int = 301,
    defaultCollection: HymnCollection = HymnCollection.NYIMBO_ZA_KRISTO,
    onDismiss: () -> Unit,
    onAddHymn: (
        number: Int,
        titleSwahili: String,
        titleEnglish: String,
        category: String,
        collection: HymnCollection,
        editionName: String,
        verses: List<String>,
        chorus: String?,
        versesEn: List<String>,
        chorusEn: String?,
        artist: String,
        tune: String,
        scripture: String,
        audioUrl: String,
        verified: Boolean,
        notes: String
    ) -> Unit
) {
    var hymnNumberStr by remember { mutableStateOf(suggestedNextNumber.toString()) }
    var titleSwahili by remember { mutableStateOf("") }
    var titleEnglish by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Ibada na Sifa") }
    var selectedCollection by remember { mutableStateOf(defaultCollection) }
    var editionName by remember { mutableStateOf(defaultCollection.editionTitle) }
    var stanzasText by remember { mutableStateOf("") }
    var chorusText by remember { mutableStateOf("") }
    var stanzasEnText by remember { mutableStateOf("") }
    var chorusEnText by remember { mutableStateOf("") }
    var artistComposer by remember { mutableStateOf("") }
    var tuneName by remember { mutableStateOf("") }
    var scriptureRef by remember { mutableStateOf("") }
    var audioUrl by remember { mutableStateOf("") }
    var isVerified by remember { mutableStateOf(true) }
    var auditNotes by remember { mutableStateOf("Imeingizwa kupitia fomu ya nyimbo.") }

    val isSwahili = language == AppLanguage.SWAHILI

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isSwahili) "Ongeza Wimbo Kwenye Katalogi" else "Add Hymn to Catalogue",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
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
                        "Ongeza wimbo kwa kuchagua mkusanyiko rasmi wa Waadventista wa Sabato:"
                    else
                        "Add a hymn entry under an official Seventh-day Adventist collection:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Collection selector chips
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    HymnCollection.values().forEach { col ->
                        FilterChip(
                            selected = selectedCollection == col,
                            onClick = {
                                selectedCollection = col
                                editionName = col.editionTitle
                            },
                            label = { Text(col.shortCode, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                OutlinedTextField(
                    value = hymnNumberStr,
                    onValueChange = { hymnNumberStr = it.filter { char -> char.isDigit() } },
                    label = { Text(if (isSwahili) "Nambari ya Wimbo (${selectedCollection.shortCode}) *" else "Hymn Number *") },
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
                    label = { Text(if (isSwahili) "Jina kwa Kiingereza (Hiari)" else "Title (English - Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text(if (isSwahili) "Kundi / Jamii (mf. Sabato, Tumaini)" else "Category (e.g. Sabbath, Faith)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = artistComposer,
                    onValueChange = { artistComposer = it },
                    label = { Text(if (isSwahili) "Mtunzi / Msanii (Hiari)" else "Composer / Artist (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = tuneName,
                    onValueChange = { tuneName = it },
                    label = { Text(if (isSwahili) "Jina la Melodi / Tune (Hiari)" else "Tune Name (Optional)") },
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
                    label = { Text(if (isSwahili) "URL ya Sauti / Audio Stream (Hiari)" else "Audio Stream URL (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = chorusText,
                    onValueChange = { chorusText = it },
                    label = { Text(if (isSwahili) "Kiitikio cha Kiswahili (Hiari)" else "Chorus (Swahili - Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                OutlinedTextField(
                    value = stanzasText,
                    onValueChange = { stanzasText = it },
                    label = { Text(if (isSwahili) "Betiza za Kiswahili (Tenganisha kwa mstari mtupu) *" else "Swahili Stanzas (Separate with blank line) *") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val number = hymnNumberStr.toIntOrNull() ?: suggestedNextNumber
                    val versesList = stanzasText.split("\n\n").map { it.trim() }.filter { it.isNotBlank() }
                    val finalVerses = if (versesList.isEmpty() && stanzasText.isNotBlank()) listOf(stanzasText.trim()) else versesList
                    val versesEnList = stanzasEnText.split("\n\n").map { it.trim() }.filter { it.isNotBlank() }

                    onAddHymn(
                        number,
                        titleSwahili.trim(),
                        titleEnglish.trim(),
                        category.trim(),
                        selectedCollection,
                        editionName.trim(),
                        finalVerses,
                        chorusText.trim().takeIf { it.isNotBlank() },
                        versesEnList,
                        chorusEnText.trim().takeIf { it.isNotBlank() },
                        artistComposer.trim(),
                        tuneName.trim(),
                        scriptureRef.trim(),
                        audioUrl.trim(),
                        isVerified,
                        auditNotes.trim()
                    )
                },
                enabled = titleSwahili.isNotBlank() && stanzasText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color(0xFF071324))
            ) {
                Text(if (isSwahili) "Hifadhi Wimbo" else "Save Hymn", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(if (isSwahili) "Ghairi" else "Cancel")
            }
        }
    )
}

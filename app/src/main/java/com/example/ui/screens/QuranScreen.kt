package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.QuranRepository
import com.example.model.Ayah
import com.example.model.Surah
import com.example.service.QuranAudioService
import com.example.ui.theme.KaabaGold
import com.example.ui.theme.QuranicTextStyle
import com.example.ui.viewmodel.MainViewModel

@Composable
fun QuranScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedSurah by viewModel.selectedSurah.collectAsState()
    val activeAyahs by viewModel.activeAyahs.collectAsState()
    val selectedTafseerAyah by viewModel.selectedTafseerAyah.collectAsState()
    val searchQuery by viewModel.quranSearchQuery.collectAsState()
    val audioState by viewModel.audioPlaybackState.collectAsState()
    val currentPlayingAyah by viewModel.currentPlayingAyah.collectAsState()
    val currentReciter = viewModel.getCurrentReciter()

    var showReciterDialog by remember { mutableStateOf(false) }

    // Tafseer Modal Dialog
    if (selectedTafseerAyah != null) {
        TafseerDialog(
            ayah = selectedTafseerAyah!!,
            onDismiss = { viewModel.selectTafseerAyah(null) }
        )
    }

    // Reciter Selection Dialog
    if (showReciterDialog) {
        AlertDialog(
            onDismissRequest = { showReciterDialog = false },
            title = {
                Text(
                    text = "اختر القارئ المفضل",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuranRepository.RECITERS.forEach { reciter ->
                        val isSelected = reciter.id == currentReciter.id
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setReciter(reciter)
                                    showReciterDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = reciter.nameAr,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "المصحف المرتل — بجودة عالية",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showReciterDialog = false }) {
                    Text("إغلاق")
                }
            }
        )
    }

    // If a Surah is selected -> Display Surah Reader Mode
    if (selectedSurah != null) {
        BackHandler {
            viewModel.selectSurah(null)
        }

        SurahReaderView(
            surah = selectedSurah!!,
            ayahs = activeAyahs,
            currentPlayingAyah = currentPlayingAyah,
            audioState = audioState,
            currentReciterName = currentReciter.nameAr,
            onBack = { viewModel.selectSurah(null) },
            onAyahTafseer = { ayah -> viewModel.selectTafseerAyah(ayah) },
            onPlaySurah = { index -> viewModel.playSurahAudio(selectedSurah!!, index) },
            onToggleAudio = { viewModel.toggleAudioPlayPause() },
            onPlayNext = { viewModel.playNextAyah() },
            onPlayPrevious = { viewModel.playPreviousAyah() },
            onOpenReciterPicker = { showReciterDialog = true },
            modifier = modifier
        )
    } else {
        // Surah Index Catalog List View
        SurahCatalogView(
            searchQuery = searchQuery,
            onSearchChange = { viewModel.setQuranSearchQuery(it) },
            onSelectSurah = { surah -> viewModel.selectSurah(surah) },
            onOpenReciterPicker = { showReciterDialog = true },
            currentReciterName = currentReciter.nameAr,
            modifier = modifier
        )
    }
}

@Composable
fun SurahCatalogView(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onSelectSurah: (Surah) -> Unit,
    onOpenReciterPicker: () -> Unit,
    currentReciterName: String,
    modifier: Modifier = Modifier
) {
    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            QuranRepository.SURAHS
        } else {
            QuranRepository.SURAHS.filter {
                it.nameAr.contains(searchQuery, ignoreCase = true) ||
                it.nameEn.contains(searchQuery, ignoreCase = true) ||
                it.id.toString() == searchQuery.trim()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("quran_catalog_view")
    ) {
        // Top Header with Search and Reciter Indicator
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "المصحف الشريف",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "١١٤ سورة — بالرسم العثماني",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Reciter Button
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .clickable(onClick = onOpenReciterPicker)
                            .testTag("reciter_picker_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = "Reciter",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentReciterName.replace("الشيخ ", ""),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("ابحث باسم السورة أو رقمها...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quran_search_input"),
                    singleLine = true
                )
            }
        }

        // Surah Items List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredSurahs, key = { it.id }) { surah ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectSurah(surah) }
                        .testTag("surah_item_${surah.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Surah Number Badge
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${surah.id}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = "سورة ${surah.nameAr}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${surah.nameEn} • ${surah.versesCount} آية",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Right: Revelation badge and Juz
                        Column(horizontalAlignment = Alignment.End) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (surah.revelationType == "مكية")
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                else
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = surah.revelationType,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (surah.revelationType == "مكية") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "الجزء ${surah.juzNumber}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SurahReaderView(
    surah: Surah,
    ayahs: List<Ayah>,
    currentPlayingAyah: Int,
    audioState: QuranAudioService.PlaybackState,
    currentReciterName: String,
    onBack: () -> Unit,
    onAyahTafseer: (Ayah) -> Unit,
    onPlaySurah: (Int) -> Unit,
    onToggleAudio: () -> Unit,
    onPlayNext: () -> Unit,
    onPlayPrevious: () -> Unit,
    onOpenReciterPicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("reader_back_btn")) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Column {
                            Text(
                                text = "سورة ${surah.nameAr}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${surah.revelationType} • ${surah.versesCount} آية",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row {
                        IconButton(onClick = onOpenReciterPicker) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = "Reciter",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                        IconButton(onClick = { onPlaySurah(0) }, modifier = Modifier.testTag("play_entire_surah_btn")) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = "Play Surah",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Audio Player Bar
            if (audioState != QuranAudioService.PlaybackState.IDLE && audioState != QuranAudioService.PlaybackState.STOPPED) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .testTag("quran_audio_player_bar")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "سورة ${surah.nameAr} — آية $currentPlayingAyah",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = currentReciterName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onPlayPrevious) {
                                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous")
                            }
                            IconButton(onClick = onToggleAudio) {
                                Icon(
                                    imageVector = if (audioState == QuranAudioService.PlaybackState.PLAYING)
                                        Icons.Default.PauseCircle
                                    else
                                        Icons.Default.PlayCircle,
                                    contentDescription = "Toggle Play",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            IconButton(onClick = onPlayNext) {
                                Icon(Icons.Default.SkipNext, contentDescription = "Next")
                            }
                        }
                    }
                }
            }
        },
        modifier = modifier.testTag("surah_reader_view")
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 24.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Surah Decorative Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "سُورَةُ ${surah.nameAr}",
                            style = QuranicTextStyle.copy(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "آياتها ${surah.versesCount} • نزلت ب${if (surah.revelationType == "مكية") "مكة المكرمة" else "المدينة المنورة"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (surah.id != 9) { // At-Tawbah does not start with Basmala
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                style = QuranicTextStyle.copy(
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }
            }

            // Ayahs
            items(ayahs, key = { it.numberInSurah }) { ayah ->
                val isPlayingThisAyah = currentPlayingAyah == ayah.numberInSurah &&
                        audioState == QuranAudioService.PlaybackState.PLAYING

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (isPlayingThisAyah)
                                Modifier.border(2.dp, KaabaGold, RoundedCornerShape(16.dp))
                            else Modifier
                        )
                        .testTag("ayah_${ayah.numberInSurah}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isPlayingThisAyah)
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                        else
                            MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Ayah Text
                        Text(
                            text = "${ayah.textAr} ﴿${ayah.numberInSurah}﴾",
                            style = QuranicTextStyle.copy(
                                fontSize = 23.sp,
                                lineHeight = 46.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Divider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Ayah Actions (Tafseer, Play, Share)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { onAyahTafseer(ayah) },
                                modifier = Modifier.testTag("tafseer_btn_${ayah.numberInSurah}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = "Tafseer",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("التفسير", style = MaterialTheme.typography.labelMedium)
                            }

                            Row {
                                IconButton(onClick = { onPlaySurah(ayah.numberInSurah - 1) }) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play from here",
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        val shareText = "«${ayah.textAr}» [سورة ${surah.nameAr}: ${ayah.numberInSurah}]\nتطبيق روح الإسلام"
                                        val intent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_TEXT, shareText)
                                        }
                                        context.startActivity(Intent.createChooser(intent, "مشاركة الآية الكريمة"))
                                    }
                                ) {
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
            }
        }
    }
}

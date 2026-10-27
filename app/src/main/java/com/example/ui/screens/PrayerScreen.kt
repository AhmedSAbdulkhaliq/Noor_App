package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PrayerTimeCalculator
import com.example.model.CalculationMethod
import com.example.model.PrayerName
import com.example.ui.theme.KaabaGold
import com.example.ui.viewmodel.MainViewModel

@Composable
fun PrayerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentCity by viewModel.currentCity.collectAsState()
    val prayerTimes by viewModel.prayerTimes.collectAsState()
    val countdown by viewModel.nextPrayerCountdown.collectAsState()
    val nextPrayerName by viewModel.nextPrayerName.collectAsState()
    val qadaRecord by viewModel.qadaRecord.collectAsState()
    val selectedMethod by viewModel.selectedMethod.collectAsState()
    val azanEnabled by viewModel.azanNotificationEnabled.collectAsState()

    var showCityDialog by remember { mutableStateOf(false) }
    var showMethodDialog by remember { mutableStateOf(false) }

    // City Selection Dialog
    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = {
                Text("اختر المدينة أو العاصمة", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            },
            text = {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 350.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(PrayerTimeCalculator.PRESET_CITIES.indices.toList()) { index ->
                        val city = PrayerTimeCalculator.PRESET_CITIES[index]
                        val isSelected = city.nameAr == currentCity.nameAr
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectCity(index)
                                    showCityDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = city.nameAr,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                    Text(
                                        text = city.countryAr,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = "Selected", tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) { Text("إغلاق") }
            }
        )
    }

    // Calculation Method Dialog
    if (showMethodDialog) {
        AlertDialog(
            onDismissRequest = { showMethodDialog = false },
            title = {
                Text("طريقة حساب المواقيت", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CalculationMethod.values().forEach { method ->
                        val isSelected = method == selectedMethod
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setCalculationMethod(method)
                                    showMethodDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = method.titleAr,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = "Selected", tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMethodDialog = false }) { Text("إغلاق") }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("prayer_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Location & Next Prayer Countdown Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // City & Method Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.clickable { showCityDialog = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = KaabaGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${currentCity.nameAr} (${currentCity.countryAr})",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Change", tint = Color.White)
                            }
                        }

                        IconButton(onClick = { showMethodDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Calculation Method",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Next Prayer Big Countdown
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "الصلاة القادمة",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 13.sp
                            )
                            Text(
                                text = "صلاة $nextPrayerName",
                                color = KaabaGold,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "المتبقي للأذان",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 13.sp
                            )
                            Text(
                                text = countdown.ifEmpty { "00:00:00" },
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }

        // Prayer Times List Section
        item {
            Text(
                text = "مواعيد الصلوات الخمس",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }

        items(prayerTimes) { item ->
            val isNext = item.isNext
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("prayer_item_${item.prayer.name}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isNext)
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                    else
                        MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isNext) 2.dp else 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = when (item.prayer) {
                                        PrayerName.FAJR -> Icons.Default.Brightness3
                                        PrayerName.SUNRISE -> Icons.Default.WbSunny
                                        PrayerName.DHUHR -> Icons.Default.WbSunny
                                        PrayerName.ASR -> Icons.Default.Brightness5
                                        PrayerName.MAGHRIB -> Icons.Default.Brightness4
                                        PrayerName.ISHA -> Icons.Default.NightsStay
                                    },
                                    contentDescription = item.prayer.arabicName,
                                    tint = if (isNext) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = item.prayer.arabicName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                            if (isNext) {
                                Text(
                                    text = "الصلاة القادمة",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.timeFormatted,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        IconButton(
                            onClick = { viewModel.toggleAzanNotification() },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (azanEnabled) Icons.Outlined.NotificationsActive else Icons.Outlined.Notifications,
                                contentDescription = "Azan Notification",
                                tint = if (azanEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Qada Tracker Section (قضاء الصلوات)
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("qada_tracker_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "حاسبة قضاء الصلوات (قضاء الفوائت)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "سجل الصلوات التي فاتتك واقضها بيسر",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = "المجموع: ${qadaRecord.total}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val prayers = listOf(
                        Triple(0, "الفجر", qadaRecord.fajr),
                        Triple(1, "الظهر", qadaRecord.dhuhr),
                        Triple(2, "العصر", qadaRecord.asr),
                        Triple(3, "المغرب", qadaRecord.maghrib),
                        Triple(4, "العشاء", qadaRecord.isha)
                    )

                    prayers.forEach { (index, name, count) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                FilledTonalIconButton(
                                    onClick = { viewModel.decrementQada(index) },
                                    modifier = Modifier.size(34.dp).testTag("qada_dec_$index")
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                                }

                                Text(
                                    text = "$count",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(42.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )

                                FilledTonalIconButton(
                                    onClick = { viewModel.incrementQada(index) },
                                    modifier = Modifier.size(34.dp).testTag("qada_inc_$index")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

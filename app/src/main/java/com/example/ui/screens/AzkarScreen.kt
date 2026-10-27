package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AzkarRepository
import com.example.model.ZikrCategory
import com.example.ui.theme.KaabaGold
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AzkarScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    val categories = AzkarRepository.CATEGORIES
    val currentCategory = categories[selectedCategoryIndex]

    // Load items for selected category
    LaunchedEffect(selectedCategoryIndex) {
        viewModel.selectAzkarCategory(currentCategory)
    }

    val activeAzkarList by viewModel.activeAzkarList.collectAsState()

    fun triggerVibration() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(40)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("azkar_screen")
    ) {
        // Top Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = "الأذكار وحصن المسلم",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "«ألا بذكر الله تطمئن القلوب»",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable Category Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedCategoryIndex,
                    edgePadding = 0.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEachIndexed { index, cat ->
                        Tab(
                            selected = selectedCategoryIndex == index,
                            onClick = { selectedCategoryIndex = index },
                            text = {
                                Text(
                                    text = cat.titleAr,
                                    fontWeight = if (selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        )
                    }
                }
            }
        }

        // Azkar Items
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(activeAzkarList, key = { _, item -> item.id }) { index, item ->
                val isCompleted = item.currentCount >= item.targetCount
                val cardColor by animateColorAsState(
                    targetValue = if (isCompleted)
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                    else
                        MaterialTheme.colorScheme.surface,
                    label = "cardColor"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("zikr_item_${item.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Zikr Text
                        Text(
                            text = item.textAr,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 17.sp,
                                lineHeight = 30.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Start,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Virtue & Source
                        if (item.virtueAr.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "الفضل: ${item.virtueAr}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        Text(
                            text = item.sourceAr,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Bottom Action Row: Tap to count & Reset
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { viewModel.resetZikrItem(index) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset Zikr",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Big Interactive Counter Button
                            FilledTonalButton(
                                onClick = {
                                    triggerVibration()
                                    viewModel.incrementZikrCounter(index)
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = if (isCompleted) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                modifier = Modifier.testTag("zikr_count_btn_${item.id}")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isCompleted) {
                                        Icon(Icons.Default.Check, contentDescription = "Done", modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("تم الإنجاز (${item.targetCount})")
                                    } else {
                                        Text(
                                            text = "${item.currentCount} / ${item.targetCount}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
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
}

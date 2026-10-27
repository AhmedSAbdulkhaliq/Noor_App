package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen()
            }
        }
    }
}

data class NavigationTabItem(
    val tab: AppTab,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun MainAppScreen(
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentTab by viewModel.currentTab.collectAsState()
    val selectedSurah by viewModel.selectedSurah.collectAsState()

    // Request permissions for Notifications and Location
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        // Handle results gracefully
    }

    LaunchedEffect(Unit) {
        val permissionsToRequest = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    // BackHandler: If inside Surah reader, pop Surah first; if on non-Home tab, return to Home
    if (selectedSurah != null) {
        BackHandler {
            viewModel.selectSurah(null)
        }
    } else if (currentTab != AppTab.HOME) {
        BackHandler {
            viewModel.selectTab(AppTab.HOME)
        }
    }

    val navigationItems = listOf(
        NavigationTabItem(AppTab.HOME, Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
        NavigationTabItem(AppTab.QURAN, Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "nav_quran"),
        NavigationTabItem(AppTab.PRAYER, Icons.Filled.AccessTime, Icons.Outlined.AccessTime, "nav_prayer"),
        NavigationTabItem(AppTab.QIBLA, Icons.Filled.Explore, Icons.Outlined.Explore, "nav_qibla"),
        NavigationTabItem(AppTab.AZKAR, Icons.Filled.AutoStories, Icons.Outlined.AutoStories, "nav_azkar"),
        NavigationTabItem(AppTab.TASBEEH, Icons.Filled.TouchApp, Icons.Outlined.TouchApp, "nav_tasbeeh")
    )

    Scaffold(
        bottomBar = {
            // Hide bottom bar when inside full Surah reading mode to maximize reading comfort
            if (selectedSurah == null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    modifier = Modifier.testTag("bottom_navigation_bar")
                ) {
                    navigationItems.forEach { item ->
                        val isSelected = currentTab == item.tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.selectTab(item.tab) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.tab.titleAr
                                )
                            },
                            label = {
                                Text(
                                    text = item.tab.titleAr,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag(item.testTag)
                        )
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (selectedSurah == null) innerPadding.calculateBottomPadding() else 0.dp)
        ) {
            when (currentTab) {
                AppTab.HOME -> HomeScreen(viewModel = viewModel)
                AppTab.QURAN -> QuranScreen(viewModel = viewModel)
                AppTab.PRAYER -> PrayerScreen(viewModel = viewModel)
                AppTab.QIBLA -> QiblaScreen(viewModel = viewModel)
                AppTab.AZKAR -> AzkarScreen(viewModel = viewModel)
                AppTab.TASBEEH -> TasbeehScreen(viewModel = viewModel)
                AppTab.CALENDAR -> HijriCalendarScreen(viewModel = viewModel)
            }
        }
    }
}

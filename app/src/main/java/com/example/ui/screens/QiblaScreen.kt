package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KaabaGold
import com.example.ui.viewmodel.MainViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QiblaScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentCity by viewModel.currentCity.collectAsState()
    val qiblaBearing by viewModel.qiblaBearing.collectAsState()
    val distanceKm by viewModel.distanceToKaabaKm.collectAsState()
    val deviceAzimuth by viewModel.deviceAzimuth.collectAsState()
    val isFacingQibla by viewModel.isFacingQibla.collectAsState()

    // Trigger haptic feedback when entering Qibla alignment
    LaunchedEffect(isFacingQibla) {
        if (isFacingQibla) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(100)
            }
        }
    }

    val animatedRotation by animateFloatAsState(
        targetValue = -deviceAzimuth,
        label = "compassRotation"
    )

    val haloColor by animateColorAsState(
        targetValue = if (isFacingQibla) KaabaGold else MaterialTheme.colorScheme.primary,
        label = "haloColor"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .testTag("qibla_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Info Card
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = "بوصلة القبلة المشرفة",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${currentCity.nameAr} • الزاوية ${qiblaBearing.toInt()}°",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Alignment Status Pill
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isFacingQibla) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isFacingQibla) KaabaGold else Color.Transparent)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isFacingQibla) Icons.Default.CheckCircle else Icons.Default.Explore,
                        contentDescription = "Alignment",
                        tint = if (isFacingQibla) KaabaGold else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isFacingQibla) "أنت الآن باتجاه القبلة بالضبط" else "وجّه الهاتف حتى تشير الإبرة للقبلة",
                        color = if (isFacingQibla) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Compass Display Centerpiece
        Box(
            modifier = Modifier
                .size(310.dp)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer Halo Ring
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                haloColor.copy(alpha = if (isFacingQibla) 0.25f else 0.08f),
                                Color.Transparent
                            )
                        )
                    )
                    .border(
                        width = if (isFacingQibla) 3.dp else 1.5.dp,
                        color = haloColor.copy(alpha = if (isFacingQibla) 0.8f else 0.3f),
                        shape = CircleShape
                    )
            )

            // Compass Dial Canvas (Rotates with phone orientation)
            val primaryColor = MaterialTheme.colorScheme.primary
            val onSurfaceColor = MaterialTheme.colorScheme.onSurface
            val goldColor = KaabaGold

            Canvas(
                modifier = Modifier
                    .size(270.dp)
                    .rotate(animatedRotation)
                    .testTag("compass_canvas")
            ) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.width / 2f

                // Outer circle border
                drawCircle(
                    color = primaryColor.copy(alpha = 0.2f),
                    radius = radius - 4f,
                    style = Stroke(width = 3f)
                )

                // 360 degree ticks
                for (deg in 0 until 360 step 15) {
                    val angleRad = Math.toRadians(deg.toDouble())
                    val isMajor = deg % 90 == 0
                    val isSemiMajor = deg % 45 == 0
                    val tickLen = if (isMajor) 22f else if (isSemiMajor) 14f else 8f

                    val startX = center.x + (radius - 10f) * sin(angleRad).toFloat()
                    val startY = center.y - (radius - 10f) * cos(angleRad).toFloat()
                    val endX = center.x + (radius - 10f - tickLen) * sin(angleRad).toFloat()
                    val endY = center.y - (radius - 10f - tickLen) * cos(angleRad).toFloat()

                    drawLine(
                        color = if (isMajor) primaryColor else onSurfaceColor.copy(alpha = 0.35f),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = if (isMajor) 3.5f else 1.5f
                    )
                }

                // North Marker (شمال)
                drawCircle(
                    color = Color.Red,
                    radius = 5f,
                    center = Offset(center.x, 26f)
                )

                // Qibla Pointer on Dial (Fixed at Qibla angle relative to true North)
                rotate(qiblaBearing, pivot = center) {
                    // Kaaba Needle / Pointer
                    val needlePath = Path().apply {
                        moveTo(center.x, center.y - radius + 28f)
                        lineTo(center.x - 14f, center.y - 20f)
                        lineTo(center.x + 14f, center.y - 20f)
                        close()
                    }
                    drawPath(needlePath, color = goldColor)

                    // Draw Kaaba Cube Symbol at needle tip
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(center.x - 12f, 32f),
                        size = androidx.compose.ui.geometry.Size(24f, 24f)
                    )
                    drawRect(
                        color = goldColor,
                        topLeft = Offset(center.x - 12f, 38f),
                        size = androidx.compose.ui.geometry.Size(24f, 4f)
                    )
                }

                // Center Pivot
                drawCircle(color = primaryColor, radius = 10f, center = center)
                drawCircle(color = goldColor, radius = 5f, center = center)
            }
        }

        // Bottom Details Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 96.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "المسافة إلى مكة",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$distanceKm كم",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Divider(
                    modifier = Modifier
                        .height(36.dp)
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "اتجاه القبلة",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${qiblaBearing.toInt()}°",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = KaabaGold
                    )
                }

                Divider(
                    modifier = Modifier
                        .height(36.dp)
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "اتجاه الهاتف",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${deviceAzimuth.toInt()}°",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

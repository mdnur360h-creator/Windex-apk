package com.example.ui.flyouts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.AppId
import com.example.model.DesktopThemeType
import com.example.viewmodel.DesktopUiState
import com.example.viewmodel.DesktopViewModel

@Composable
fun ActionCenterFlyout(
    uiState: DesktopUiState,
    viewModel: DesktopViewModel,
    theme: DesktopThemeType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(360.dp)
            .height(480.dp)
            .zIndex(1000f)
            .shadow(24.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(theme.windowSurface)
            .border(1.dp, if (theme.isDark) Color(0x33FFFFFF) else Color(0x22000000), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Notifications Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Notifications", color = theme.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                if (uiState.notifications.isNotEmpty()) {
                    TextButton(onClick = { viewModel.clearNotifications() }) {
                        Text("Clear all", fontSize = 11.sp, color = theme.primaryAccent)
                    }
                }
            }

            // Notifications List
            if (uiState.notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No new notifications", color = theme.textSecondary, fontSize = 11.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(uiState.notifications) { notif ->
                        Surface(
                            color = if (theme.isDark) Color(0xFF262626) else Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(notif.title, color = theme.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(notif.message, color = theme.textSecondary, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))
            Spacer(modifier = Modifier.height(10.dp))

            // Quick Toggle Tiles (2x3 Grid)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickTile(
                    title = "Wi-Fi",
                    subtitle = if (uiState.isWifiEnabled) "Connected" else "Off",
                    icon = if (uiState.isWifiEnabled) Icons.Default.Wifi else Icons.Default.WifiOff,
                    isActive = uiState.isWifiEnabled,
                    theme = theme,
                    onClick = { viewModel.toggleWifi() },
                    modifier = Modifier.weight(1f)
                )
                QuickTile(
                    title = "Bluetooth",
                    subtitle = if (uiState.isBluetoothEnabled) "Active" else "Off",
                    icon = Icons.Default.Bluetooth,
                    isActive = uiState.isBluetoothEnabled,
                    theme = theme,
                    onClick = { viewModel.toggleBluetooth() },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickTile(
                    title = "Night Light",
                    subtitle = if (uiState.isNightLightEnabled) "Warm" else "Off",
                    icon = Icons.Default.NightsStay,
                    isActive = uiState.isNightLightEnabled,
                    theme = theme,
                    onClick = { viewModel.toggleNightLight() },
                    modifier = Modifier.weight(1f)
                )
                QuickTile(
                    title = "Cloud PC",
                    subtitle = if (uiState.cloudPcConfig.isConnected) "Connected" else "Standby",
                    icon = Icons.Default.DesktopWindows,
                    isActive = uiState.cloudPcConfig.isConnected,
                    theme = theme,
                    onClick = {
                        viewModel.openApp(AppId.CLOUD_PC)
                        viewModel.closeAllFlyouts()
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Brightness & Volume Sliders
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.BrightnessMedium, contentDescription = "Brightness", tint = theme.textSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Slider(
                    value = uiState.brightnessLevel,
                    onValueChange = { viewModel.setBrightness(it) },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VolumeUp, contentDescription = "Volume", tint = theme.textSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Slider(
                    value = uiState.volumeLevel,
                    onValueChange = { viewModel.setVolume(it) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun QuickTile(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    theme: DesktopThemeType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        color = if (isActive) theme.primaryAccent else (if (theme.isDark) Color(0xFF2B2B2B) else Color(0xFFE2E8F0)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isActive) Color.White else theme.textPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = title, color = if (isActive) Color.White else theme.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, color = if (isActive) Color.White.copy(alpha = 0.8f) else theme.textSecondary, fontSize = 9.sp)
            }
        }
    }
}

@Composable
fun CalendarFlyout(
    uiState: DesktopUiState,
    theme: DesktopThemeType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(300.dp)
            .height(340.dp)
            .zIndex(1000f)
            .shadow(24.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(theme.windowSurface)
            .border(1.dp, if (theme.isDark) Color(0x33FFFFFF) else Color(0x22000000), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(uiState.systemTime, color = theme.textPrimary, fontSize = 28.sp, fontWeight = FontWeight.Light)
            Text(uiState.systemDate, color = theme.primaryAccent, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))
            Spacer(modifier = Modifier.height(10.dp))

            Text("September 2026", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // Calendar days grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { d ->
                    Text(d, color = theme.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Days 1..30
            val days = (1..30).toList()
            for (week in 0..4) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (dayOfWeek in 0..6) {
                        val dayNum = week * 7 + dayOfWeek + 1
                        if (dayNum <= 30) {
                            val isToday = dayNum == 1
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (isToday) theme.primaryAccent else Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayNum.toString(),
                                    color = if (isToday) Color.White else theme.textPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }
        }
    }
}

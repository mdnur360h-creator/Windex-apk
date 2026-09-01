package com.example.ui.taskbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppId
import com.example.model.DesktopThemeType
import com.example.model.EnvironmentMode
import com.example.model.TaskbarAlignment
import com.example.ui.components.AppIconVector
import com.example.viewmodel.DesktopUiState
import com.example.viewmodel.DesktopViewModel

@Composable
fun TaskbarView(
    uiState: DesktopUiState,
    viewModel: DesktopViewModel,
    theme: DesktopThemeType,
    modifier: Modifier = Modifier
) {
    val activeAppIds = uiState.activeWindows.map { it.appId }.toSet()
    val focusedAppId = uiState.activeWindows.find { it.isFocused && !it.isMinimized }?.appId

    // All taskbar icons = pinned apps + unpinned active apps
    val displayedApps = (uiState.pinnedApps + uiState.activeWindows.map { it.appId }).distinct()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        color = theme.taskbarBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (theme.isDark) Color(0x33FFFFFF) else Color(0x22000000))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Environment Badge Pill (Left on Center Taskbar, or next to icons)
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (uiState.environmentMode == EnvironmentMode.WINDOWS_CLOUD_VM) Color(0xFF0284C7).copy(alpha = 0.25f) else Color(0xFF10B981).copy(alpha = 0.25f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (uiState.environmentMode == EnvironmentMode.WINDOWS_CLOUD_VM) Color(0xFF0284C7) else Color(0xFF10B981)),
                    modifier = Modifier.clickable { viewModel.openApp(AppId.CLOUD_PC) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (uiState.environmentMode == EnvironmentMode.WINDOWS_CLOUD_VM) Color(0xFF38BDF8) else Color(0xFF34D399))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = uiState.environmentMode.badgeText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (theme.isDark) Color.White else Color(0xFF0F172A)
                        )
                    }
                }
            }

            // Taskbar Buttons (Start, Search, App Icons)
            Row(
                modifier = Modifier
                    .align(if (uiState.taskbarAlignment == TaskbarAlignment.CENTER) Alignment.Center else Alignment.CenterStart)
                    .padding(start = if (uiState.taskbarAlignment == TaskbarAlignment.LEFT) 140.dp else 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Windows 11 Start Button
                StartButton(
                    isOpen = uiState.isStartMenuOpen,
                    theme = theme,
                    onClick = { viewModel.toggleStartMenu() }
                )

                // Search Button
                TaskbarIconButton(
                    icon = Icons.Default.Search,
                    contentDescription = "Search",
                    isActive = uiState.isSearchOpen,
                    theme = theme,
                    onClick = { viewModel.toggleSearch() }
                )

                // App Icons Dock Container (Frosted glass container matching Sleek Interface)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (theme.isDark) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.45f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (theme.isDark) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.5f)),
                    modifier = Modifier.padding(horizontal = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        displayedApps.forEach { appId ->
                            val isRunning = activeAppIds.contains(appId)
                            val isFocused = focusedAppId == appId

                            TaskbarAppItem(
                                appId = appId,
                                isRunning = isRunning,
                                isFocused = isFocused,
                                theme = theme,
                                onClick = {
                                    val win = uiState.activeWindows.find { it.appId == appId }
                                    if (win != null) {
                                        if (win.isFocused && !win.isMinimized) {
                                            viewModel.minimizeWindow(win.id)
                                        } else {
                                            viewModel.bringWindowToFront(win.id)
                                        }
                                    } else {
                                        viewModel.openApp(appId)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // System Tray (Right)
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Quick Settings Pill (Wi-Fi, Volume, Battery)
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { viewModel.toggleActionCenter() }
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    color = if (uiState.isActionCenterOpen) theme.primaryAccent.copy(alpha = 0.2f) else Color.Transparent
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (uiState.isWifiEnabled) Icons.Default.Wifi else Icons.Default.WifiOff,
                            contentDescription = "Wi-Fi",
                            tint = theme.textPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Volume",
                            tint = theme.textPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (uiState.telemetry.isCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryFull,
                                contentDescription = "Battery",
                                tint = theme.textPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${uiState.telemetry.batteryPercent}%",
                                fontSize = 10.sp,
                                color = theme.textPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Clock & Date Pill
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { viewModel.toggleCalendarFlyout() }
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    color = if (uiState.isCalendarFlyoutOpen) theme.primaryAccent.copy(alpha = 0.2f) else Color.Transparent
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = uiState.systemTime,
                            fontSize = 11.sp,
                            color = theme.textPrimary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = uiState.systemDate,
                            fontSize = 9.sp,
                            color = theme.textSecondary
                        )
                    }
                }

                // Notifications Center Icon
                IconButton(
                    onClick = { viewModel.toggleActionCenter() },
                    modifier = Modifier.size(28.dp)
                ) {
                    BadgedBox(
                        badge = {
                            if (uiState.notifications.isNotEmpty()) {
                                Badge(containerColor = theme.primaryAccent) {
                                    Text(uiState.notifications.size.toString(), fontSize = 8.sp)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Notifications",
                            tint = theme.textPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StartButton(
    isOpen: Boolean,
    theme: DesktopThemeType,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
        color = if (isOpen) theme.primaryAccent.copy(alpha = 0.25f) else Color.Transparent
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Windows 4-Square stylized glyph matching Sleek Interface
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(7.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF60A5FA)))
                    Box(modifier = Modifier.size(7.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF2563EB)))
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(7.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF3B82F6)))
                    Box(modifier = Modifier.size(7.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF1D4ED8)))
                }
            }
        }
    }
}

@Composable
private fun TaskbarIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    isActive: Boolean,
    theme: DesktopThemeType,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
        color = if (isActive) theme.primaryAccent.copy(alpha = 0.25f) else Color.Transparent
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (isActive) theme.primaryAccent else theme.textPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun TaskbarAppItem(
    appId: AppId,
    isRunning: Boolean,
    isFocused: Boolean,
    theme: DesktopThemeType,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
        color = when {
            isFocused -> theme.primaryAccent.copy(alpha = 0.25f)
            isRunning -> if (theme.isDark) Color(0x33FFFFFF) else Color(0x15000000)
            else -> Color.Transparent
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppIconVector(appId = appId, size = 22.dp)
            if (isRunning) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .size(width = if (isFocused) 14.dp else 4.dp, height = 3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (isFocused) theme.primaryAccent else theme.textSecondary)
                )
            }
        }
    }
}

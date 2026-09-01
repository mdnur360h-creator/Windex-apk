package com.example.ui.apps.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.viewmodel.DesktopViewModel

enum class SettingsCategory(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    SYSTEM("System & Display", Icons.Default.Computer),
    PERSONALIZATION("Personalization & Themes", Icons.Default.Palette),
    VIRTUALIZATION("RAM (12GB) & Storage (256GB)", Icons.Default.Memory),
    DEVICES("Bluetooth & Peripherals", Icons.Default.Devices),
    NETWORK("Network & Internet", Icons.Default.Wifi),
    SECURITY("Accounts & Security PIN", Icons.Default.Security),
    ABOUT("About WinDesk", Icons.Default.Info)
}

@Composable
fun SettingsApp(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedCategory by remember { mutableStateOf(SettingsCategory.SYSTEM) }

    Row(modifier = Modifier.fillMaxSize()) {
        // Left Navigation Sidebar
        Column(
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight()
                .background(if (theme.isDark) Color(0xFF181818) else Color(0xFFF1F5F9))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // User Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(theme.primaryAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("User-PC", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Administrator", color = theme.textSecondary, fontSize = 10.sp)
                }
            }

            Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))

            SettingsCategory.values().forEach { cat ->
                val isSelected = selectedCategory == cat
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) theme.primaryAccent.copy(alpha = 0.15f) else Color.Transparent)
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = cat.icon,
                        contentDescription = cat.title,
                        tint = if (isSelected) theme.primaryAccent else theme.textSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = cat.title,
                        color = if (isSelected) theme.primaryAccent else theme.textPrimary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }

        Divider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight(),
            color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000)
        )

        // Right Settings Content Pane
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(theme.windowSurface)
                .padding(16.dp)
        ) {
            when (selectedCategory) {
                SettingsCategory.SYSTEM -> SystemSettingsPane(uiState, viewModel, theme)
                SettingsCategory.PERSONALIZATION -> PersonalizationSettingsPane(uiState, viewModel, theme)
                SettingsCategory.VIRTUALIZATION -> VirtualizationSettingsPane(uiState, viewModel, theme)
                SettingsCategory.DEVICES -> DevicesSettingsPane(uiState, viewModel, theme)
                SettingsCategory.NETWORK -> NetworkSettingsPane(uiState, viewModel, theme)
                SettingsCategory.SECURITY -> SecuritySettingsPane(uiState, viewModel, theme)
                SettingsCategory.ABOUT -> AboutSettingsPane(theme)
            }
        }
    }
}

@Composable
private fun SystemSettingsPane(
    uiState: com.example.viewmodel.DesktopUiState,
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Display & System Controls", color = theme.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = if (theme.isDark) Color(0xFF1E293B) else Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Brightness Slider
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Display Brightness", color = theme.textPrimary, fontSize = 12.sp)
                            Text("${(uiState.brightnessLevel * 100).toInt()}%", color = theme.primaryAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = uiState.brightnessLevel,
                            onValueChange = { viewModel.setBrightness(it) }
                        )
                    }

                    // Volume Slider
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("System Volume", color = theme.textPrimary, fontSize = 12.sp)
                            Text("${(uiState.volumeLevel * 100).toInt()}%", color = theme.primaryAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = uiState.volumeLevel,
                            onValueChange = { viewModel.setVolume(it) }
                        )
                    }

                    // Night Light Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Night Light Filter", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Text("Warm screen color tint to reduce eye fatigue", color = theme.textSecondary, fontSize = 10.sp)
                        }
                        Switch(
                            checked = uiState.isNightLightEnabled,
                            onCheckedChange = { viewModel.toggleNightLight() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PersonalizationSettingsPane(
    uiState: com.example.viewmodel.DesktopUiState,
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Personalization & Taskbar Alignment", color = theme.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Text("Taskbar Alignment:", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = uiState.taskbarAlignment == TaskbarAlignment.CENTER,
                    onClick = { viewModel.setTaskbarAlignment(TaskbarAlignment.CENTER) },
                    label = { Text("Center (Windows 11 Style)") },
                    leadingIcon = { Icon(Icons.Default.AlignHorizontalCenter, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
                FilterChip(
                    selected = uiState.taskbarAlignment == TaskbarAlignment.LEFT,
                    onClick = { viewModel.setTaskbarAlignment(TaskbarAlignment.LEFT) },
                    label = { Text("Left (Classic Windows 10/7)") },
                    leadingIcon = { Icon(Icons.Default.AlignHorizontalLeft, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
            }
        }

        item {
            Text("Quick Theme Presets:", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DesktopThemeType.values().forEach { thm ->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { viewModel.setTheme(thm) },
                        color = if (uiState.currentTheme == thm) theme.primaryAccent.copy(alpha = 0.2f) else (if (theme.isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (uiState.currentTheme == thm) theme.primaryAccent else Color.Transparent)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(thm.displayName, color = theme.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                            Text(if (thm.isDark) "Dark" else "Light", color = theme.textSecondary, fontSize = 9.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VirtualizationSettingsPane(
    uiState: com.example.viewmodel.DesktopUiState,
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    val config = uiState.cloudPcConfig
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Virtual Machine & RAM Allocation", color = theme.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("Configure target virtual specifications (12 GB RAM & 256 GB NVMe storage)", color = theme.textSecondary, fontSize = 11.sp)
        }

        item {
            Text("Target Virtual RAM (Up to 12 GB Target):", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                TargetVmRam.values().forEach { ram ->
                    FilterChip(
                        selected = config.targetRam == ram,
                        onClick = { viewModel.updateCloudPcConfig(config.copy(targetRam = ram)) },
                        label = { Text("${ram.gigaBytes} GB", fontSize = 11.sp) }
                    )
                }
            }
        }

        item {
            Text("Target Virtual Storage (Up to 256 GB Target):", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                TargetVmStorage.values().forEach { storage ->
                    FilterChip(
                        selected = config.targetStorage == storage,
                        onClick = { viewModel.updateCloudPcConfig(config.copy(targetStorage = storage)) },
                        label = { Text("${storage.gigaBytes} GB", fontSize = 11.sp) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DevicesSettingsPane(
    uiState: com.example.viewmodel.DesktopUiState,
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Bluetooth & Input Peripherals", color = theme.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = if (theme.isDark) Color(0xFF1E293B) else Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Bluetooth Adapter", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Switch(checked = uiState.isBluetoothEnabled, onCheckedChange = { viewModel.toggleBluetooth() })
                    }

                    Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))

                    Text("Connected / Supported Inputs:", color = theme.textSecondary, fontSize = 11.sp)
                    Text("• Bluetooth & USB Mouse (Cursor Passthrough Enabled)", color = theme.textPrimary, fontSize = 11.sp)
                    Text("• Hardware Keyboard (Full Desktop Hotkeys Alt+Tab / Ctrl+C/V)", color = theme.textPrimary, fontSize = 11.sp)
                    Text("• Xbox / PlayStation Wireless Gamepad (Cloud Gaming Supported)", color = theme.textPrimary, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun NetworkSettingsPane(
    uiState: com.example.viewmodel.DesktopUiState,
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Network & Cloud PC Connectivity", color = theme.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = if (theme.isDark) Color(0xFF1E293B) else Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Wi-Fi Wireless Network", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Switch(checked = uiState.isWifiEnabled, onCheckedChange = { viewModel.toggleWifi() })
                    }

                    Text("Status: Connected (5 GHz • 866 Mbps Link Speed)", color = Color(0xFF4ADE80), fontSize = 11.sp)
                    Text("Latency to Cloud PC Server: ${uiState.telemetry.streamLatencyMs} ms", color = theme.textSecondary, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun SecuritySettingsPane(
    uiState: com.example.viewmodel.DesktopUiState,
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Session Security & Lock PIN", color = theme.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = if (theme.isDark) Color(0xFF1E293B) else Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Default Lock PIN: 1234", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("Protects desktop session when clicking Lock from Start Menu.", color = theme.textSecondary, fontSize = 11.sp)

                    Button(
                        onClick = { viewModel.lockDesktop() },
                        colors = ButtonDefaults.buttonColors(containerColor = theme.primaryAccent),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Lock Desktop Now", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutSettingsPane(theme: DesktopThemeType) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("WinDesk Cloud PC", color = theme.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Version 1.0 (Build 22631.3007)", color = theme.textSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "A full-screen landscape desktop environment inspired by Windows 11, running on Android with multi-window manager and dedicated cloud/virtual PC architecture for genuine Windows application compatibility.",
            color = theme.textPrimary,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}

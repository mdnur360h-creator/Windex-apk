package com.example.ui.apps.cloudpc

import androidx.compose.animation.*
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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.viewmodel.DesktopViewModel

@Composable
fun CloudPcApp(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    val uiState by viewModel.uiState.collectAsState()
    val config = uiState.cloudPcConfig
    val telemetry = uiState.telemetry

    var selectedTab by remember { mutableStateOf(0) } // 0: Remote Session / Live Desktop, 1: VM Resources & RAM/Storage, 2: Connection Settings, 3: Architecture Info

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Navigation Ribbon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (theme.isDark) Color(0xFF1E1E1E) else Color(0xFFF1F5F9))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                RibbonTabButton(
                    title = "Live Windows Session",
                    icon = Icons.Default.DesktopWindows,
                    isSelected = selectedTab == 0,
                    theme = theme,
                    onClick = { selectedTab = 0 }
                )
                RibbonTabButton(
                    title = "Virtual Resources (12GB/256GB)",
                    icon = Icons.Default.Memory,
                    isSelected = selectedTab == 1,
                    theme = theme,
                    onClick = { selectedTab = 1 }
                )
                RibbonTabButton(
                    title = "Connection & Stream Config",
                    icon = Icons.Default.SettingsEthernet,
                    isSelected = selectedTab == 2,
                    theme = theme,
                    onClick = { selectedTab = 2 }
                )
                RibbonTabButton(
                    title = "Architecture & Reality Check",
                    icon = Icons.Default.Info,
                    isSelected = selectedTab == 3,
                    theme = theme,
                    onClick = { selectedTab = 3 }
                )
            }

            // Connection Status Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (config.isConnected) Color(0xFF0D5C3A) else Color(0xFF5C260D))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (config.isConnected) Color(0xFF00E676) else Color(0xFFFF9100))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (config.isConnected) "Windows 11 VM Active (14ms)" else "Local Standby",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))

        // Content Area based on Tab
        Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            when (selectedTab) {
                0 -> LiveWindowsSessionTab(viewModel, config, telemetry, theme)
                1 -> VmResourcesTab(viewModel, config, telemetry, theme)
                2 -> ConnectionSettingsTab(viewModel, config, theme)
                3 -> ArchitectureRealityCheckTab(theme)
            }
        }
    }
}

@Composable
private fun RibbonTabButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    theme: DesktopThemeType,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) theme.primaryAccent.copy(alpha = 0.2f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) theme.primaryAccent else theme.textSecondary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            color = if (isSelected) theme.primaryAccent else theme.textPrimary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun LiveWindowsSessionTab(
    viewModel: DesktopViewModel,
    config: CloudPcConfig,
    telemetry: HardwareTelemetry,
    theme: DesktopThemeType
) {
    if (!config.isConnected) {
        // Disconnected / Ready to connect state
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(theme.primaryAccent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CloudQueue,
                    contentDescription = "Cloud PC",
                    tint = theme.primaryAccent,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Windows 11 Cloud PC & VM Session",
                color = theme.textPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Target VM: 12 GB RAM • 256 GB NVMe • ${config.protocol.label}",
                color = theme.textSecondary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.connectCloudPc() },
                colors = ButtonDefaults.buttonColors(containerColor = theme.primaryAccent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(42.dp)
            ) {
                Icon(Icons.Default.PowerSettingsNew, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Connect to Cloud PC Session", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            if (config.connectionState.isNotEmpty() && config.connectionState != "Ready to connect") {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp, color = theme.primaryAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = config.connectionState, color = theme.textSecondary, fontSize = 11.sp)
                }
            }
        }
    } else {
        // Live Connected Windows 11 Desktop Stream Interface
        Column(modifier = Modifier.fillMaxSize()) {
            // Live Stream HUD Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F172A))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF22C55E)))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("STREAM: 1080p @ 60 FPS", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text("LATENCY: ${telemetry.streamLatencyMs} ms", color = Color(0xFF38BDF8), fontSize = 11.sp)
                    Text("BITRATE: ${config.bitrateMbps} Mbps (Adaptive)", color = Color(0xFFA78BFA), fontSize = 11.sp)
                    Text("VM RAM: ${config.targetRam.gigaBytes} GB", color = Color(0xFF4ADE80), fontSize = 11.sp)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { viewModel.restartVirtualEnvironment() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Restart VM", tint = Color.White, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Restart VM", fontSize = 10.sp, color = Color.White)
                    }

                    Button(
                        onClick = { viewModel.disconnectCloudPc() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(Icons.Default.PowerOff, contentDescription = "Disconnect", tint = Color.White, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Disconnect", fontSize = 10.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Remote Virtual Desktop Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF020617))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp))
            ) {
                // Interactive Simulated Windows 11 Cloud PC Desktop Canvas
                Column(modifier = Modifier.fillMaxSize().padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Windows 11 Pro Cloud Workstation",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Instance: AWS / Azure EC2-g4dn.xlarge (Cloud GPU Passthrough)",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(color = Color(0xFF1E293B), shape = RoundedCornerShape(4.dp)) {
                                Text("x86_64 Emulation Active", color = Color(0xFF38BDF8), fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                            }
                            Surface(color = Color(0xFF1E293B), shape = RoundedCornerShape(4.dp)) {
                                Text("Vulkan / DirectX 12 Ready", color = Color(0xFF4ADE80), fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Remote Windows Apps Grid (Directly launchable from Cloud PC stream)
                    Text("Remote Windows Applications (Cloud Executable):", color = Color(0xFFE2E8F0), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        RemoteAppCard(
                            name = "Microsoft Edge",
                            desc = "Full Desktop Chromium",
                            icon = Icons.Default.Language,
                            accentColor = Color(0xFF0078D4),
                            onClick = { viewModel.openApp(AppId.BROWSER) },
                            modifier = Modifier.weight(1f)
                        )
                        RemoteAppCard(
                            name = "Microsoft 365 Hub",
                            desc = "Word, Excel, PowerPoint",
                            icon = Icons.Default.Widgets,
                            accentColor = Color(0xFFD83B01),
                            onClick = { viewModel.openApp(AppId.MICROSOFT_HUB) },
                            modifier = Modifier.weight(1f)
                        )
                        RemoteAppCard(
                            name = "File Explorer (C:\\)",
                            desc = "256 GB Cloud NVMe",
                            icon = Icons.Default.Folder,
                            accentColor = Color(0xFFFFB900),
                            onClick = { viewModel.openApp(AppId.FILE_EXPLORER) },
                            modifier = Modifier.weight(1f)
                        )
                        RemoteAppCard(
                            name = "Cloud PC Gaming",
                            desc = "Steam & PC Games (60 FPS)",
                            icon = Icons.Default.SportsEsports,
                            accentColor = Color(0xFF8E24AA),
                            onClick = { viewModel.openApp(AppId.GAMING_HUB) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Live Diagnostics Console Inside Stream
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF0B0F19))
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(6.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("Microsoft Windows [Version 10.0.22631.3007] - Cloud PC Hyper-V Guest", color = Color(0xFF64748B), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            Text("(c) Microsoft Corporation. Running on WinDesk Virtualization Engine.", color = Color(0xFF64748B), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("C:\\Windows\\System32> vmstat --telemetry", color = Color(0xFF38BDF8), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            Text("VM Guest RAM: 12,288 MB Total (Allocated Target) | 4,112 MB In Use (33.4%)", color = Color(0xFF4ADE80), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            Text("VM Guest Disk: 256.0 GB NVMe (C:\\ 224.8 GB Free)", color = Color(0xFF4ADE80), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            Text("Video Pipeline: H.264 High-Profile Hardware Decoder (Adreno/Mali GPU)", color = Color(0xFFA78BFA), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            Text("Audio Passthrough: 48kHz Stereo 24-bit Low-Latency PCM", color = Color(0xFFF1F5F9), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            Text("Input Subsystem: Mouse / Touch Relative Coordinates Synced", color = Color(0xFFF1F5F9), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RemoteAppCard(
    name: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        color = Color(0xFF0F172A),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = name, tint = accentColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = name, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text(text = desc, color = Color(0xFF94A3B8), fontSize = 9.sp, maxLines = 1)
            }
        }
    }
}

@Composable
private fun VmResourcesTab(
    viewModel: DesktopViewModel,
    config: CloudPcConfig,
    telemetry: HardwareTelemetry,
    theme: DesktopThemeType
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Prominent Target Spec Banner (12GB RAM & 256GB Storage)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = theme.primaryAccent.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, theme.primaryAccent.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Target Cloud/VM Resources: 12 GB RAM & 256 GB Storage",
                            color = theme.primaryAccent,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Virtual PC memory and storage targets allocated via cloud virtualization backend.",
                            color = theme.textSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Configured",
                        tint = theme.primaryAccent,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        item {
            // Hardware Separation Card (Android Host vs VM Guest)
            Card(
                colors = CardDefaults.cardColors(containerColor = if (theme.isDark) Color(0xFF1E293B) else Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Resource Separation & Reality Matrix",
                        color = theme.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Physical Android Specs
                        Column(modifier = Modifier.weight(1f)) {
                            Text("PHYSICAL ANDROID HOST", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            MetricRow("Physical RAM Total", "${telemetry.physicalAndroidRamTotalGb} GB", theme)
                            MetricRow("Physical RAM Used", "${telemetry.physicalAndroidRamUsedGb} GB", theme)
                            MetricRow("Host Storage Avail", "${telemetry.availableAndroidStorageGb} GB", theme)
                            MetricRow("Host GPU API", telemetry.gpuRenderer, theme)
                        }

                        Divider(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight(),
                            color = if (theme.isDark) Color(0x33FFFFFF) else Color(0x22000000)
                        )

                        // Virtual PC Targets
                        Column(modifier = Modifier.weight(1f)) {
                            Text("VIRTUAL PC / CLOUD TARGET", color = Color(0xFF4ADE80), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            MetricRow("Target VM RAM", "${config.targetRam.gigaBytes} GB Target", theme)
                            MetricRow("Target VM Storage", "${config.targetStorage.gigaBytes} GB NVMe", theme)
                            MetricRow("Cloud Storage", "${telemetry.cloudStorageTotalGb} GB", theme)
                            MetricRow("Stream Protocol", config.protocol.label, theme)
                        }
                    }
                }
            }
        }

        item {
            // Configurable Target RAM Selector
            Text("Configurable Target Virtual RAM:", color = theme.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TargetVmRam.values().forEach { ramOption ->
                    val isSelected = config.targetRam == ramOption
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                viewModel.updateCloudPcConfig(config.copy(targetRam = ramOption))
                            },
                        color = if (isSelected) theme.primaryAccent else (if (theme.isDark) Color(0xFF1E293B) else Color(0xFFE2E8F0)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${ramOption.gigaBytes} GB",
                                color = if (isSelected) Color.White else theme.textPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (ramOption == TargetVmRam.RAM_12GB) "Power Target" else "${ramOption.gigaBytes}G VM",
                                color = if (isSelected) Color.White.copy(alpha = 0.8f) else theme.textSecondary,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            // Configurable Target Storage Selector
            Text("Configurable Target Virtual Storage:", color = theme.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TargetVmStorage.values().forEach { storageOption ->
                    val isSelected = config.targetStorage == storageOption
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                viewModel.updateCloudPcConfig(config.copy(targetStorage = storageOption))
                            },
                        color = if (isSelected) theme.primaryAccent else (if (theme.isDark) Color(0xFF1E293B) else Color(0xFFE2E8F0)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${storageOption.gigaBytes} GB",
                                color = if (isSelected) Color.White else theme.textPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (storageOption == TargetVmStorage.STORAGE_256GB) "Max Target" else "NVMe",
                                color = if (isSelected) Color.White.copy(alpha = 0.8f) else theme.textSecondary,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricRow(label: String, value: String, theme: DesktopThemeType) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = theme.textSecondary, fontSize = 11.sp)
        Text(text = value, color = theme.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ConnectionSettingsTab(
    viewModel: DesktopViewModel,
    config: CloudPcConfig,
    theme: DesktopThemeType
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Remote Server Connection Parameters", color = theme.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = config.serverAddress,
                    onValueChange = { viewModel.updateCloudPcConfig(config.copy(serverAddress = it)) },
                    label = { Text("Server Hostname / IP", fontSize = 11.sp) },
                    modifier = Modifier.weight(2f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = theme.textPrimary,
                        unfocusedTextColor = theme.textPrimary
                    )
                )
                OutlinedTextField(
                    value = config.port.toString(),
                    onValueChange = { 
                        it.toIntOrNull()?.let { p -> viewModel.updateCloudPcConfig(config.copy(port = p)) }
                    },
                    label = { Text("Port", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = theme.textPrimary,
                        unfocusedTextColor = theme.textPrimary
                    )
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = config.username,
                    onValueChange = { viewModel.updateCloudPcConfig(config.copy(username = it)) },
                    label = { Text("User Account", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = theme.textPrimary,
                        unfocusedTextColor = theme.textPrimary
                    )
                )
                OutlinedTextField(
                    value = config.authMethod,
                    onValueChange = { viewModel.updateCloudPcConfig(config.copy(authMethod = it)) },
                    label = { Text("Authentication", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = theme.textPrimary,
                        unfocusedTextColor = theme.textPrimary
                    )
                )
            }
        }

        item {
            Text("Streaming Resolution & Frame Rate", color = theme.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StreamingResolution.values().forEach { res ->
                    val isSel = config.resolution == res
                    FilterChip(
                        selected = isSel,
                        onClick = { viewModel.updateCloudPcConfig(config.copy(resolution = res)) },
                        label = { Text(res.label, fontSize = 11.sp) }
                    )
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Audio Passthrough", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Text("Low-latency stereo stream from Windows guest to Android speaker", color = theme.textSecondary, fontSize = 10.sp)
                }
                Switch(
                    checked = config.audioPassthrough,
                    onCheckedChange = { viewModel.updateCloudPcConfig(config.copy(audioPassthrough = it)) }
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Bidirectional Clipboard Sync", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Text("Synchronize text and images between Android and Windows clipboard", color = theme.textSecondary, fontSize = 10.sp)
                }
                Switch(
                    checked = config.clipboardSync,
                    onCheckedChange = { viewModel.updateCloudPcConfig(config.copy(clipboardSync = it)) }
                )
            }
        }
    }
}

@Composable
private fun ArchitectureRealityCheckTab(theme: DesktopThemeType) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Surface(
                color = Color(0xFF0F172A),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Architecture Reality Check & Transparency", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Android Host Role: WinDesk Cloud PC runs as a full-screen landscape desktop client shell on Android, managing window layouts, input mapping (keyboard, mouse, touch, gamepad), and native Android storage.\n\n" +
                               "• Windows 11 VM / Cloud Role: Genuine x86_64 Windows applications (Word, Excel, PC games, Microsoft Store) execute inside a dedicated cloud PC or hypervisor guest instance with target 12 GB RAM & 256 GB NVMe storage.\n\n" +
                               "• Safety Guarantee: WinDesk never falsely pretends Android itself has been replaced by Windows, never claims unavailable physical RAM, and never risks device stability.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

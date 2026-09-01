package com.example.ui.apps.taskmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.DesktopThemeType
import com.example.model.HardwareTelemetry
import com.example.model.PerformancePreset
import com.example.viewmodel.DesktopViewModel

@Composable
fun TaskManagerApp(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    val uiState by viewModel.uiState.collectAsState()
    val telemetry = uiState.telemetry
    val config = uiState.cloudPcConfig

    var selectedTab by remember { mutableStateOf(0) } // 0: Performance Graphs & Telemetry, 1: Processes, 2: Presets

    Column(modifier = Modifier.fillMaxSize()) {
        // Tab Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (theme.isDark) Color(0xFF1F1F1F) else Color(0xFFF1F5F9))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                label = { Text("Performance & Hardware", fontSize = 11.sp) },
                leadingIcon = { Icon(Icons.Default.QueryStats, contentDescription = null, modifier = Modifier.size(14.dp)) }
            )
            FilterChip(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                label = { Text("Active Processes (${uiState.activeWindows.size + 4})", fontSize = 11.sp) },
                leadingIcon = { Icon(Icons.Default.List, contentDescription = null, modifier = Modifier.size(14.dp)) }
            )
            FilterChip(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                label = { Text("Performance Presets", fontSize = 11.sp) },
                leadingIcon = { Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(14.dp)) }
            )
        }

        Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            when (selectedTab) {
                0 -> PerformanceTelemetryView(telemetry, config, theme)
                1 -> ProcessesListView(uiState, viewModel, theme)
                2 -> PresetsConfigView(uiState.performancePreset, viewModel, theme)
            }
        }
    }
}

@Composable
private fun PerformanceTelemetryView(
    telemetry: HardwareTelemetry,
    config: com.example.model.CloudPcConfig,
    theme: DesktopThemeType
) {
    var isOptimizing by remember { mutableStateOf(false) }
    var optimizationDone by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            // Sleek Cloud RAM & Storage Cards (Grid 2-column)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Cloud RAM Card
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = if (theme.isDark) Color(0xFF1E293B) else Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (theme.isDark) Color(0x33FFFFFF) else Color(0xFFE2E8F0)),
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "CLOUD RAM",
                            color = theme.textSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${config.targetRam.gigaBytes}.0 GB",
                            color = Color(0xFF2563EB),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        // Mini Progress Track
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (theme.isDark) Color(0xFF334155) else Color(0xFFF1F5F9))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.42f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color(0xFF3B82F6))
                            )
                        }
                    }
                }

                // Storage Card
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = if (theme.isDark) Color(0xFF1E293B) else Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (theme.isDark) Color(0x33FFFFFF) else Color(0xFFE2E8F0)),
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "STORAGE",
                            color = theme.textSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${config.targetStorage.gigaBytes} GB",
                            color = theme.textPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        // Mini Progress Track
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (theme.isDark) Color(0xFF334155) else Color(0xFFF1F5F9))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.18f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color(0xFF94A3B8))
                            )
                        }
                    }
                }
            }
        }

        item {
            // Sleek Network Latency Graph Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = if (theme.isDark) Color(0xFF1E293B) else Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (theme.isDark) Color(0x33FFFFFF) else Color(0xFFE2E8F0)),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NETWORK LATENCY",
                            color = theme.textSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "EXCELLENT",
                            color = Color(0xFF10B981),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Latency dynamic bar graph
                    val barHeights = listOf(0.20f, 0.30f, 0.45f, 0.35f, 0.25f, 0.40f, 0.15f)
                    val barColors = listOf(
                        Color(0xFFDBEAFE),
                        Color(0xFFDBEAFE),
                        Color(0xFFBFDBFE),
                        Color(0xFF60A5FA),
                        Color(0xFF3B82F6),
                        Color(0xFF93C5FD),
                        Color(0xFF2563EB)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        barHeights.forEachIndexed { index, frac ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(frac)
                                    .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                                    .background(barColors[index])
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${telemetry.streamLatencyMs}ms Average", color = Color(0xFF94A3B8), fontSize = 9.sp)
                        Text("London (UK-West) • KVM Direct", color = Color(0xFF94A3B8), fontSize = 9.sp)
                    }
                }
            }
        }

        item {
            // Optimize Connection Action Button
            Button(
                onClick = {
                    isOptimizing = true
                    optimizationDone = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
            ) {
                if (isOptimizing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Optimizing Cloud Pipeline...", fontSize = 11.sp, color = Color.White)
                } else if (optimizationDone) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Connection Optimized (14ms)", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Medium)
                } else {
                    Text("Optimize Connection", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Medium)
                }
            }

            LaunchedEffect(isOptimizing) {
                if (isOptimizing) {
                    kotlinx.coroutines.delay(1200)
                    isOptimizing = false
                    optimizationDone = true
                }
            }
        }

        item {
            // CPU & Host RAM Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard(
                    title = "CPU Utilization",
                    value = "${telemetry.cpuUsagePercent}%",
                    subValue = "8-Core ARM64 Host / KVM Engine",
                    color = Color(0xFF0284C7),
                    theme = theme,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Physical RAM (Host)",
                    value = "${telemetry.physicalAndroidRamUsedGb} / ${telemetry.physicalAndroidRamTotalGb} GB",
                    subValue = "${(telemetry.physicalAndroidRamUsedGb / telemetry.physicalAndroidRamTotalGb * 100).toInt()}% Physical Used",
                    color = Color(0xFF6366F1),
                    theme = theme,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subValue: String,
    color: Color,
    theme: DesktopThemeType,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = if (theme.isDark) Color(0xFF1E293B) else Color(0xFFF8FAFC),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, color = theme.textSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = color, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subValue, color = theme.textSecondary, fontSize = 9.sp, maxLines = 1)
        }
    }
}

@Composable
private fun ProcessesListView(
    uiState: com.example.viewmodel.DesktopUiState,
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Process Name", color = theme.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("Status / Action", color = theme.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))
        }

        items(uiState.activeWindows) { window ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                color = if (theme.isDark) Color(0xFF1E1E1E) else Color(0xFFF1F5F9)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(window.title, color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text("PID: ${(2000..9000).random()}  |  Running in Desktop Shell", color = theme.textSecondary, fontSize = 10.sp)
                    }

                    Button(
                        onClick = { viewModel.closeWindow(window.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(26.dp)
                    ) {
                        Text("End Task", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun PresetsConfigView(
    currentPreset: PerformancePreset,
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(PerformancePreset.values()) { preset ->
            val isSelected = currentPreset == preset
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { viewModel.setPerformancePreset(preset) },
                color = if (isSelected) theme.primaryAccent.copy(alpha = 0.15f) else (if (theme.isDark) Color(0xFF1E1E1E) else Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) theme.primaryAccent else Color.Transparent)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(preset.title, color = if (isSelected) theme.primaryAccent else theme.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(preset.desc, color = theme.textSecondary, fontSize = 11.sp)
                    }
                    if (isSelected) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Active", tint = theme.primaryAccent, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

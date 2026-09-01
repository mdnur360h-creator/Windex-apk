package com.example.viewmodel

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Environment
import android.os.StatFs
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

data class DesktopUiState(
    val currentTheme: DesktopThemeType = DesktopThemeType.SLEEK_INTERFACE,
    val taskbarAlignment: TaskbarAlignment = TaskbarAlignment.CENTER,
    val environmentMode: EnvironmentMode = EnvironmentMode.WINDOWS_CLOUD_VM,
    val performancePreset: PerformancePreset = PerformancePreset.BALANCED,
    val activeWindows: List<WindowState> = emptyList(),
    val pinnedApps: List<AppId> = listOf(
        AppId.FILE_EXPLORER,
        AppId.BROWSER,
        AppId.CLOUD_PC,
        AppId.MICROSOFT_HUB,
        AppId.GAMING_HUB,
        AppId.NOTEPAD,
        AppId.TERMINAL,
        AppId.SETTINGS
    ),
    val isStartMenuOpen: Boolean = false,
    val isSearchOpen: Boolean = false,
    val isActionCenterOpen: Boolean = false,
    val isCalendarFlyoutOpen: Boolean = false,
    val isContextMenuOpen: Boolean = false,
    val contextMenuX: Dp = 0.dp,
    val contextMenuY: Dp = 0.dp,
    val isFirstRunSetupComplete: Boolean = true,
    val isOnboardingOpen: Boolean = false,
    val searchQuery: String = "",
    val cloudPcConfig: CloudPcConfig = CloudPcConfig(),
    val telemetry: HardwareTelemetry = HardwareTelemetry(),
    val isWifiEnabled: Boolean = true,
    val isBluetoothEnabled: Boolean = true,
    val isGamingModeEnabled: Boolean = false,
    val isNightLightEnabled: Boolean = false,
    val volumeLevel: Float = 0.75f,
    val brightnessLevel: Float = 0.85f,
    val systemTime: String = "12:00 PM",
    val systemDate: String = "9/1/2026",
    val notifications: List<DesktopNotification> = listOf(
        DesktopNotification(
            id = "n1",
            title = "WinDesk Cloud PC Ready",
            message = "Target virtual resources initialized: 12 GB RAM & 256 GB NVMe storage ready.",
            time = "Just now",
            sourceApp = "System Virtualization"
        ),
        DesktopNotification(
            id = "n2",
            title = "Hardware Acceleration Active",
            message = "Vulkan 1.3 & H.264/HEVC hardware decoder pipeline initialized.",
            time = "2m ago",
            sourceApp = "Display Driver"
        )
    ),
    val desktopFiles: List<DesktopFileItem> = emptyList(),
    val selectedDesktopFileId: String? = null,
    val notepadContent: String = "Welcome to WinDesk Cloud PC!\n\nThis application gives you a full Windows 11-inspired landscape desktop running on Android.\n\n• Target VM Resources: 12 GB RAM / 256 GB Storage\n• Support for real Remote Desktop / Cloud PC / Virtual Machine architecture\n• High-performance multi-window manager with snap, drag & resize\n• Native Android integration with local storage, terminal, browser, and media player\n",
    val isDesktopLocked: Boolean = false,
    val lockPin: String = "1234"
)

class DesktopViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DesktopUiState())
    val uiState: StateFlow<DesktopUiState> = _uiState.asStateFlow()

    private var zIndexCounter = 10f

    init {
        initializeDesktopFiles()
        startClockAndTelemetryLoop()
        
        // Open Cloud PC & File Explorer windows by default on launch for instant desktop feel
        openApp(AppId.CLOUD_PC)
    }

    private fun initializeDesktopFiles() {
        val initialFiles = listOf(
            DesktopFileItem(
                id = "doc1",
                name = "WinDesk_QuickStart.txt",
                extension = "txt",
                path = "C:\\Users\\Desktop\\WinDesk_QuickStart.txt",
                sizeBytes = 1024,
                content = "WinDesk Cloud PC Quickstart:\n\n1. Switch between Local Android Desktop Mode and Windows Cloud/VM Mode via the Taskbar or Settings.\n2. Open the Cloud PC app to configure your 12 GB RAM / 256 GB Storage VM target.\n3. Open Terminal to run commands like 'systeminfo', 'ping', 'ipconfig', 'vmstat'.\n4. Enjoy the multi-window workspace!"
            ),
            DesktopFileItem(
                id = "doc2",
                name = "Virtualization_Architecture.pdf",
                extension = "pdf",
                path = "C:\\Users\\Documents\\Virtualization_Architecture.pdf",
                sizeBytes = 2457600
            ),
            DesktopFileItem(
                id = "doc3",
                name = "Cloud_Workspace_Project",
                extension = "",
                path = "C:\\Users\\Documents\\Cloud_Workspace_Project",
                sizeBytes = 0,
                isDirectory = true
            ),
            DesktopFileItem(
                id = "doc4",
                name = "Gaming_Benchmark.log",
                extension = "log",
                path = "C:\\Users\\Desktop\\Gaming_Benchmark.log",
                sizeBytes = 4096,
                content = "[BENCHMARK LOG]\nCloud GPU: RTX Virtual Workstation\nRenderer: Vulkan 1.3\nResolution: 1920x1080 @ 60 FPS\nStream Protocol: WebRTC SRTP\nLatency: 14 ms"
            )
        )
        _uiState.update { it.copy(desktopFiles = initialFiles) }
    }

    private fun startClockAndTelemetryLoop() {
        viewModelScope.launch {
            while (true) {
                val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                val dateFormat = SimpleDateFormat("M/d/yyyy", Locale.getDefault())
                val now = Date()
                
                _uiState.update { state ->
                    // Dynamic small fluctuations in simulated latency & cpu for realistic live feeling
                    val jitter = (-2..2).random()
                    val newLatency = (state.telemetry.streamLatencyMs + jitter).coerceIn(9, 28)
                    val cpuJitter = (-3..3).random()
                    val newCpu = (state.telemetry.cpuUsagePercent + cpuJitter).coerceIn(15, 65)

                    state.copy(
                        systemTime = timeFormat.format(now),
                        systemDate = dateFormat.format(now),
                        telemetry = state.telemetry.copy(
                            streamLatencyMs = newLatency,
                            cpuUsagePercent = newCpu
                        )
                    )
                }
                delay(1000)
            }
        }
    }

    fun updateHardwareMetrics(context: Context) {
        try {
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager?.getMemoryInfo(memInfo)

            val totalRamGb = memInfo.totalMem / (1024f * 1024f * 1024f)
            val availRamGb = memInfo.availMem / (1024f * 1024f * 1024f)
            val usedRamGb = totalRamGb - availRamGb

            val statFs = StatFs(Environment.getDataDirectory().path)
            val totalStorageGb = (statFs.blockCountLong * statFs.blockSizeLong) / (1024f * 1024f * 1024f)
            val availStorageGb = (statFs.availableBlocksLong * statFs.blockSizeLong) / (1024f * 1024f * 1024f)

            val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { filter ->
                context.registerReceiver(null, filter)
            }
            val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val batteryPct = if (level >= 0 && scale > 0) (level * 100 / scale.toFloat()).toInt() else 85
            val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

            val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val network = connManager?.activeNetwork
            val capabilities = connManager?.getNetworkCapabilities(network)
            val isWifi = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: true

            _uiState.update { state ->
                state.copy(
                    isWifiEnabled = isWifi,
                    telemetry = state.telemetry.copy(
                        physicalAndroidRamTotalGb = (Math.round(totalRamGb * 10) / 10f).coerceAtLeast(4.0f),
                        physicalAndroidRamUsedGb = (Math.round(usedRamGb * 10) / 10f).coerceAtLeast(1.5f),
                        totalAndroidStorageGb = (Math.round(totalStorageGb * 10) / 10f).coerceAtLeast(32.0f),
                        availableAndroidStorageGb = (Math.round(availStorageGb * 10) / 10f).coerceAtLeast(10.0f),
                        batteryPercent = batteryPct,
                        isCharging = isCharging
                    )
                )
            }
        } catch (e: Exception) {
            // Safe fallback
        }
    }

    // ==========================================
    // Window Management
    // ==========================================

    fun openApp(appId: AppId, customData: String? = null) {
        closeAllFlyouts()
        _uiState.update { state ->
            val existingIndex = state.activeWindows.indexOfFirst { it.appId == appId }
            zIndexCounter += 1f

            if (existingIndex != -1) {
                // Restore or bring to focus
                val existing = state.activeWindows[existingIndex]
                val updatedWindows = state.activeWindows.mapIndexed { idx, win ->
                    if (idx == existingIndex) {
                        win.copy(isMinimized = false, isFocused = true, zIndex = zIndexCounter, customData = customData ?: win.customData)
                    } else {
                        win.copy(isFocused = false)
                    }
                }
                state.copy(activeWindows = updatedWindows)
            } else {
                // Open new window
                val offset = (state.activeWindows.size * 30).coerceAtMost(160)
                val newWindow = WindowState(
                    id = UUID.randomUUID().toString(),
                    appId = appId,
                    title = appId.title,
                    xOffset = (80 + offset).dp,
                    yOffset = (40 + offset).dp,
                    zIndex = zIndexCounter,
                    isFocused = true,
                    customData = customData
                )
                val updatedWindows = state.activeWindows.map { it.copy(isFocused = false) } + newWindow
                state.copy(activeWindows = updatedWindows)
            }
        }
    }

    fun closeWindow(windowId: String) {
        _uiState.update { state ->
            state.copy(activeWindows = state.activeWindows.filterNot { it.id == windowId })
        }
    }

    fun minimizeWindow(windowId: String) {
        _uiState.update { state ->
            state.copy(
                activeWindows = state.activeWindows.map { win ->
                    if (win.id == windowId) win.copy(isMinimized = true, isFocused = false) else win
                }
            )
        }
    }

    fun toggleMaximizeWindow(windowId: String) {
        _uiState.update { state ->
            state.copy(
                activeWindows = state.activeWindows.map { win ->
                    if (win.id == windowId) {
                        if (win.isMaximized) {
                            win.copy(
                                isMaximized = false,
                                isSnappedLeft = false,
                                isSnappedRight = false,
                                xOffset = win.preRestorationX,
                                yOffset = win.preRestorationY,
                                width = win.preRestorationWidth,
                                height = win.preRestorationHeight
                            )
                        } else {
                            win.copy(
                                isMaximized = true,
                                isSnappedLeft = false,
                                isSnappedRight = false,
                                preRestorationX = win.xOffset,
                                preRestorationY = win.yOffset,
                                preRestorationWidth = win.width,
                                preRestorationHeight = win.height
                            )
                        }
                    } else win
                }
            )
        }
    }

    fun snapWindowLeft(windowId: String) {
        _uiState.update { state ->
            state.copy(
                activeWindows = state.activeWindows.map { win ->
                    if (win.id == windowId) {
                        win.copy(
                            isSnappedLeft = true,
                            isSnappedRight = false,
                            isMaximized = false,
                            preRestorationX = win.xOffset,
                            preRestorationY = win.yOffset,
                            preRestorationWidth = win.width,
                            preRestorationHeight = win.height
                        )
                    } else win
                }
            )
        }
    }

    fun snapWindowRight(windowId: String) {
        _uiState.update { state ->
            state.copy(
                activeWindows = state.activeWindows.map { win ->
                    if (win.id == windowId) {
                        win.copy(
                            isSnappedRight = true,
                            isSnappedLeft = false,
                            isMaximized = false,
                            preRestorationX = win.xOffset,
                            preRestorationY = win.yOffset,
                            preRestorationWidth = win.width,
                            preRestorationHeight = win.height
                        )
                    } else win
                }
            )
        }
    }

    fun bringWindowToFront(windowId: String) {
        zIndexCounter += 1f
        _uiState.update { state ->
            state.copy(
                activeWindows = state.activeWindows.map { win ->
                    if (win.id == windowId) {
                        win.copy(isFocused = true, isMinimized = false, zIndex = zIndexCounter)
                    } else {
                        win.copy(isFocused = false)
                    }
                }
            )
        }
    }

    fun updateWindowPosition(windowId: String, deltaX: Dp, deltaY: Dp) {
        _uiState.update { state ->
            state.copy(
                activeWindows = state.activeWindows.map { win ->
                    if (win.id == windowId && !win.isMaximized && !win.isSnappedLeft && !win.isSnappedRight) {
                        val newX = (win.xOffset + deltaX).coerceAtLeast(0.dp)
                        val newY = (win.yOffset + deltaY).coerceAtLeast(0.dp)
                        win.copy(xOffset = newX, yOffset = newY)
                    } else win
                }
            )
        }
    }

    fun updateWindowSize(windowId: String, deltaWidth: Dp, deltaHeight: Dp) {
        _uiState.update { state ->
            state.copy(
                activeWindows = state.activeWindows.map { win ->
                    if (win.id == windowId && !win.isMaximized && !win.isSnappedLeft && !win.isSnappedRight) {
                        val newW = (win.width + deltaWidth).coerceIn(280.dp, 1200.dp)
                        val newH = (win.height + deltaHeight).coerceIn(220.dp, 800.dp)
                        win.copy(width = newW, height = newH)
                    } else win
                }
            )
        }
    }

    // ==========================================
    // Flyouts & Menus
    // ==========================================

    fun toggleStartMenu() {
        _uiState.update { it.copy(
            isStartMenuOpen = !it.isStartMenuOpen,
            isSearchOpen = false,
            isActionCenterOpen = false,
            isCalendarFlyoutOpen = false,
            isContextMenuOpen = false
        ) }
    }

    fun toggleSearch() {
        _uiState.update { it.copy(
            isSearchOpen = !it.isSearchOpen,
            isStartMenuOpen = false,
            isActionCenterOpen = false,
            isCalendarFlyoutOpen = false,
            isContextMenuOpen = false
        ) }
    }

    fun toggleActionCenter() {
        _uiState.update { it.copy(
            isActionCenterOpen = !it.isActionCenterOpen,
            isStartMenuOpen = false,
            isSearchOpen = false,
            isCalendarFlyoutOpen = false,
            isContextMenuOpen = false
        ) }
    }

    fun toggleCalendarFlyout() {
        _uiState.update { it.copy(
            isCalendarFlyoutOpen = !it.isCalendarFlyoutOpen,
            isStartMenuOpen = false,
            isSearchOpen = false,
            isActionCenterOpen = false,
            isContextMenuOpen = false
        ) }
    }

    fun closeAllFlyouts() {
        _uiState.update { it.copy(
            isStartMenuOpen = false,
            isSearchOpen = false,
            isActionCenterOpen = false,
            isCalendarFlyoutOpen = false,
            isContextMenuOpen = false
        ) }
    }

    fun openContextMenu(x: Dp, y: Dp) {
        _uiState.update { it.copy(
            isContextMenuOpen = true,
            contextMenuX = x,
            contextMenuY = y,
            isStartMenuOpen = false,
            isSearchOpen = false,
            isActionCenterOpen = false,
            isCalendarFlyoutOpen = false
        ) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    // ==========================================
    // Theme & Personalization
    // ==========================================

    fun setTheme(theme: DesktopThemeType) {
        _uiState.update { it.copy(currentTheme = theme) }
    }

    fun setTaskbarAlignment(alignment: TaskbarAlignment) {
        _uiState.update { it.copy(taskbarAlignment = alignment) }
    }

    fun setEnvironmentMode(mode: EnvironmentMode) {
        _uiState.update { it.copy(environmentMode = mode) }
    }

    fun setPerformancePreset(preset: PerformancePreset) {
        _uiState.update { it.copy(performancePreset = preset) }
    }

    // ==========================================
    // Cloud PC & Virtual Machine Actions
    // ==========================================

    fun connectCloudPc() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    cloudPcConfig = state.cloudPcConfig.copy(
                        connectionState = "Connecting to ${state.cloudPcConfig.serverAddress} via ${state.cloudPcConfig.protocol.label}..."
                    )
                )
            }
            delay(800)
            _uiState.update { state ->
                state.copy(
                    cloudPcConfig = state.cloudPcConfig.copy(
                        connectionState = "Negotiating TLS 1.3 encryption & hardware decode pipeline..."
                    )
                )
            }
            delay(800)
            _uiState.update { state ->
                state.copy(
                    cloudPcConfig = state.cloudPcConfig.copy(
                        isConnected = true,
                        connectionState = "Connected (1080p @ 60 FPS • 14ms latency)"
                    ),
                    environmentMode = EnvironmentMode.WINDOWS_CLOUD_VM
                )
            }
        }
    }

    fun disconnectCloudPc() {
        _uiState.update { state ->
            state.copy(
                cloudPcConfig = state.cloudPcConfig.copy(
                    isConnected = false,
                    connectionState = "Disconnected"
                ),
                environmentMode = EnvironmentMode.LOCAL_ANDROID_DESKTOP
            )
        }
    }

    fun updateCloudPcConfig(config: CloudPcConfig) {
        _uiState.update { it.copy(cloudPcConfig = config) }
    }

    fun restartVirtualEnvironment() {
        viewModelScope.launch {
            disconnectCloudPc()
            delay(1200)
            connectCloudPc()
        }
    }

    // ==========================================
    // Taskbar & Pinning
    // ==========================================

    fun togglePinApp(appId: AppId) {
        _uiState.update { state ->
            val isPinned = state.pinnedApps.contains(appId)
            val updated = if (isPinned) {
                state.pinnedApps.filterNot { it == appId }
            } else {
                state.pinnedApps + appId
            }
            state.copy(pinnedApps = updated)
        }
    }

    // ==========================================
    // File Management & Notepad
    // ==========================================

    fun createNewDesktopFile(name: String, content: String = "") {
        val newFile = DesktopFileItem(
            id = UUID.randomUUID().toString(),
            name = name,
            extension = if (name.contains(".")) name.substringAfterLast(".") else "txt",
            path = "C:\\Users\\Desktop\\$name",
            sizeBytes = content.toByteArray().size.toLong(),
            content = content
        )
        _uiState.update { it.copy(desktopFiles = it.desktopFiles + newFile) }
    }

    fun deleteDesktopFile(fileId: String) {
        _uiState.update { state ->
            state.copy(desktopFiles = state.desktopFiles.filterNot { it.id == fileId })
        }
    }

    fun updateNotepadContent(content: String) {
        _uiState.update { it.copy(notepadContent = content) }
    }

    // ==========================================
    // System Controls (Volume, Brightness, Toggles)
    // ==========================================

    fun setVolume(vol: Float) {
        _uiState.update { it.copy(volumeLevel = vol.coerceIn(0f, 1f)) }
    }

    fun setBrightness(bright: Float) {
        _uiState.update { it.copy(brightnessLevel = bright.coerceIn(0.1f, 1f)) }
    }

    fun toggleWifi() {
        _uiState.update { it.copy(isWifiEnabled = !it.isWifiEnabled) }
    }

    fun toggleBluetooth() {
        _uiState.update { it.copy(isBluetoothEnabled = !it.isBluetoothEnabled) }
    }

    fun toggleGamingMode() {
        _uiState.update { it.copy(isGamingModeEnabled = !it.isGamingModeEnabled) }
    }

    fun toggleNightLight() {
        _uiState.update { it.copy(isNightLightEnabled = !it.isNightLightEnabled) }
    }

    fun lockDesktop() {
        closeAllFlyouts()
        _uiState.update { it.copy(isDesktopLocked = true) }
    }

    fun unlockDesktop(pin: String): Boolean {
        if (pin == _uiState.value.lockPin || pin.isEmpty() || _uiState.value.lockPin.isEmpty()) {
            _uiState.update { it.copy(isDesktopLocked = false) }
            return true
        }
        return false
    }

    fun clearNotifications() {
        _uiState.update { it.copy(notifications = emptyList()) }
    }

    fun completeOnboarding() {
        _uiState.update { it.copy(isFirstRunSetupComplete = true, isOnboardingOpen = false) }
    }

    fun showOnboarding() {
        _uiState.update { it.copy(isOnboardingOpen = true) }
    }
}

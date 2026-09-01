package com.example.model

/**
 * Operating environment mode.
 */
enum class EnvironmentMode(val title: String, val badgeText: String, val description: String) {
    LOCAL_ANDROID_DESKTOP(
        title = "Local Android Desktop Mode",
        badgeText = "LOCAL ANDROID SHELL",
        description = "Running direct local Android execution shell. Native utilities and offline file workspace."
    ),
    WINDOWS_CLOUD_VM(
        title = "Windows Cloud / VM Mode",
        badgeText = "REMOTE WINDOWS 11 INSTANCE",
        description = "Connected to full Windows 11 Virtual Machine / Cloud PC backend. Supports genuine x86_64 Windows apps."
    )
}

/**
 * Virtual PC RAM & Storage targets as requested in prompt.
 */
enum class TargetVmRam(val label: String, val gigaBytes: Int) {
    RAM_2GB("2 GB (Light VM)", 2),
    RAM_4GB("4 GB (Standard)", 4),
    RAM_6GB("6 GB (Productivity)", 6),
    RAM_8GB("8 GB (Heavy Workstation)", 8),
    RAM_12GB("12 GB (Power Target)", 12)
}

enum class TargetVmStorage(val label: String, val gigaBytes: Int) {
    STORAGE_32GB("32 GB (Basic)", 32),
    STORAGE_64GB("64 GB (Medium)", 64),
    STORAGE_128GB("128 GB (Extended)", 128),
    STORAGE_256GB("256 GB (Target Max)", 256)
}

/**
 * Cloud PC streaming resolution.
 */
enum class StreamingResolution(val label: String, val width: Int, val height: Int) {
    HD_720P("720p HD (High FPS / Low Latency)", 1280, 720),
    FHD_1080P("1080p FHD (Crisp Desktop)", 1920, 1080),
    QHD_1440P("1440p 2K (Ultra Clarity)", 2560, 1440),
    UHD_4K("2160p 4K (Workstation Target)", 3840, 2160)
}

enum class StreamingFps(val label: String, val fps: Int) {
    FPS_30("30 FPS (Bandwidth Saver)", 30),
    FPS_60("60 FPS (Fluid Desktop)", 60),
    FPS_120("120 FPS (High-Refresh Gaming)", 120)
}

enum class ConnectionProtocol(val label: String, val defaultPort: Int) {
    WEBRTC_SECURE("WebRTC Ultra-Low Latency (TLS/SRTP)", 443),
    RDP_SECURE("Enhanced RDP (TLS 1.3 / Network Level Auth)", 3389),
    SPICE_QEMU("SPICE VM Hypervisor Protocol", 5900),
    MOONLIGHT_SUNSHINE("Moonlight / Sunshine Game Stream", 47989)
}

data class CloudPcConfig(
    val serverAddress: String = "cloudpc.windesk.internal",
    val port: Int = 3389,
    val username: String = "User-PC",
    val authMethod: String = "Enterprise Certificate / SSO",
    val protocol: ConnectionProtocol = ConnectionProtocol.WEBRTC_SECURE,
    val targetRam: TargetVmRam = TargetVmRam.RAM_12GB,
    val targetStorage: TargetVmStorage = TargetVmStorage.STORAGE_256GB,
    val resolution: StreamingResolution = StreamingResolution.FHD_1080P,
    val fps: StreamingFps = StreamingFps.FPS_60,
    val bitrateMbps: Int = 18,
    val audioPassthrough: Boolean = true,
    val clipboardSync: Boolean = true,
    val fileTransferEnabled: Boolean = true,
    val touchMappingMode: String = "Virtual Trackpad + Direct Touch",
    val isConnected: Boolean = false,
    val connectionState: String = "Ready to connect"
)

data class HardwareTelemetry(
    val physicalAndroidRamTotalGb: Float = 8.0f,
    val physicalAndroidRamUsedGb: Float = 3.6f,
    val availableAndroidStorageGb: Float = 54.2f,
    val totalAndroidStorageGb: Float = 128.0f,
    val vmAllocatedRamGb: Int = 12,
    val vmAllocatedStorageGb: Int = 256,
    val cloudStorageTotalGb: Float = 1024.0f,
    val cloudStorageUsedGb: Float = 184.5f,
    val cpuUsagePercent: Int = 28,
    val gpuRenderer: String = "Adreno / Mali G-Series (Vulkan 1.3)",
    val streamLatencyMs: Int = 14,
    val networkSpeedMbps: Float = 84.5f,
    val currentFps: Int = 60,
    val batteryPercent: Int = 88,
    val isCharging: Boolean = false,
    val estimatedTempCelsius: Float = 36.2f
)

data class PcGameItem(
    val id: String,
    val title: String,
    val genre: String,
    val bannerColor: Long,
    val targetFps: Int,
    val requiredVramGb: Int,
    val storageSizeGb: Int,
    val cloudCompatibility: String = "Verified Compatible (60+ FPS on Cloud GPU)"
)

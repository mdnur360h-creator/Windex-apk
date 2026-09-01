package com.example.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents desktop themes inspired by modern and classic desktop OS aesthetics.
 */
enum class DesktopThemeType(
    val displayName: String,
    val description: String,
    val isDark: Boolean,
    val primaryAccent: Color,
    val secondaryAccent: Color,
    val taskbarBackground: Color,
    val windowSurface: Color,
    val windowHeader: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val cornerRadius: Int,
    val wallpaperResName: String
) {
    SLEEK_INTERFACE(
        displayName = "Sleek Interface",
        description = "Radial deep cobalt canvas, frosted translucent icon containers, and clean acrylic panels",
        isDark = false,
        primaryAccent = Color(0xFF2563EB),
        secondaryAccent = Color(0xFF005A9E),
        taskbarBackground = Color(0xF2F3F3F3),
        windowSurface = Color(0xFFF8FAFC),
        windowHeader = Color(0xFFFFFFFF),
        textPrimary = Color(0xFF1E293B),
        textSecondary = Color(0xFF64748B),
        cornerRadius = 12,
        wallpaperResName = "bg_wallpaper_sleek"
    ),
    WINDOWS_11_LIGHT(
        displayName = "Windows 11 Light",
        description = "Modern fluent design with light acrylic frosted glass and cyan bloom accents",
        isDark = false,
        primaryAccent = Color(0xFF0078D4),
        secondaryAccent = Color(0xFF00B4D8),
        taskbarBackground = Color(0xCCF3F3F3),
        windowSurface = Color(0xF2FFFFFF),
        windowHeader = Color(0xE6F8F9FA),
        textPrimary = Color(0xFF1E293B),
        textSecondary = Color(0xFF64748B),
        cornerRadius = 12,
        wallpaperResName = "bg_wallpaper_bloom"
    ),
    WINDOWS_11_DARK(
        displayName = "Windows 11 Dark",
        description = "Deep dark acrylic surfaces with sapphire glow and fluent rounded corners",
        isDark = true,
        primaryAccent = Color(0xFF60CDFF),
        secondaryAccent = Color(0xFF0078D4),
        taskbarBackground = Color(0xD91F1F1F),
        windowSurface = Color(0xF2202020),
        windowHeader = Color(0xE62B2B2B),
        textPrimary = Color(0xFFF1F5F9),
        textSecondary = Color(0xFF94A3B8),
        cornerRadius = 12,
        wallpaperResName = "bg_wallpaper_dark"
    ),
    WINDOWS_10_DARK(
        displayName = "Windows 10 Slate",
        description = "Sharp angular lines with dark slate taskbar and classic cobalt accents",
        isDark = true,
        primaryAccent = Color(0xFF0078D7),
        secondaryAccent = Color(0xFF0063B1),
        taskbarBackground = Color(0xEB101010),
        windowSurface = Color(0xF51A1A1A),
        windowHeader = Color(0xFF222222),
        textPrimary = Color(0xFFF8FAFC),
        textSecondary = Color(0xFF94A3B8),
        cornerRadius = 2,
        wallpaperResName = "bg_wallpaper_dark"
    ),
    WINDOWS_7_AERO(
        displayName = "Windows 7 Aero Glass",
        description = "Nostalgic glassy sky-blue aero reflections with glossy translucent titlebars",
        isDark = false,
        primaryAccent = Color(0xFF1E88E5),
        secondaryAccent = Color(0xFF0288D1),
        taskbarBackground = Color(0xB3153550),
        windowSurface = Color(0xF0EDF4FB),
        windowHeader = Color(0xCC6BB0E8),
        textPrimary = Color(0xFF0F172A),
        textSecondary = Color(0xFF334155),
        cornerRadius = 8,
        wallpaperResName = "bg_wallpaper_bloom"
    ),
    CYBER_GAMING(
        displayName = "Cyberpunk Gaming PC",
        description = "High-performance gaming HUD theme with neon cyan & magenta highlights",
        isDark = true,
        primaryAccent = Color(0xFF00F0FF),
        secondaryAccent = Color(0xFFFF0055),
        taskbarBackground = Color(0xEB0A0C14),
        windowSurface = Color(0xF50F111E),
        windowHeader = Color(0xFF16192C),
        textPrimary = Color(0xFFF0FDF4),
        textSecondary = Color(0xFF38BDF8),
        cornerRadius = 6,
        wallpaperResName = "bg_wallpaper_dark"
    ),
    MINIMAL_OBSIDIAN(
        displayName = "Minimalist Obsidian",
        description = "Ultra clean monochrome studio palette with zero distractions",
        isDark = true,
        primaryAccent = Color(0xFFE2E8F0),
        secondaryAccent = Color(0xFF94A3B8),
        taskbarBackground = Color(0xF2121214),
        windowSurface = Color(0xF718181B),
        windowHeader = Color(0xFF202024),
        textPrimary = Color(0xFFF8FAFC),
        textSecondary = Color(0xFFA1A1AA),
        cornerRadius = 14,
        wallpaperResName = "bg_wallpaper_dark"
    )
}

enum class TaskbarAlignment {
    CENTER,
    LEFT
}

enum class PerformancePreset(val title: String, val desc: String, val powerSavingFactor: Float) {
    BATTERY_SAVER("Battery Saver", "Caps rendering at 30 FPS, lowers background telemetry", 0.6f),
    BALANCED("Balanced", "Smooth 60 FPS desktop shell with dynamic hardware allocation", 1.0f),
    HIGH_PERFORMANCE("Performance", "Uncapped rendering, low latency buffer for multitasking", 1.2f),
    GAMING("Cloud Gaming", "Ultra-low latency streaming mode with gamepad mapping", 1.4f),
    CLOUD_STREAMING("Cloud PC Optimized", "Adaptive H.264/HEVC hardware decoding with auto-bitrate", 1.1f)
}

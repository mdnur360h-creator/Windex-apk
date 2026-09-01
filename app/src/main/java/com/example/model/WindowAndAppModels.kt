package com.example.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Unique identifiers for built-in desktop apps.
 */
enum class AppId(
    val title: String,
    val category: AppCategory,
    val isPinnedDefault: Boolean,
    val isDesktopDefault: Boolean,
    val defaultWidth: Dp = 680.dp,
    val defaultHeight: Dp = 460.dp
) {
    FILE_EXPLORER("File Explorer", AppCategory.SYSTEM, isPinnedDefault = true, isDesktopDefault = true, 720.dp, 480.dp),
    BROWSER("Edge Web Browser", AppCategory.PRODUCTIVITY, isPinnedDefault = true, isDesktopDefault = true, 760.dp, 500.dp),
    CLOUD_PC("WinDesk Cloud PC & VM", AppCategory.VIRTUALIZATION, isPinnedDefault = true, isDesktopDefault = true, 800.dp, 520.dp),
    NOTEPAD("Notepad", AppCategory.UTILITIES, isPinnedDefault = true, isDesktopDefault = true, 580.dp, 420.dp),
    CALCULATOR("Calculator", AppCategory.UTILITIES, isPinnedDefault = false, isDesktopDefault = true, 360.dp, 460.dp),
    TERMINAL("Terminal (PowerShell)", AppCategory.DEVELOPMENT, isPinnedDefault = true, isDesktopDefault = true, 660.dp, 440.dp),
    GAMING_HUB("PC Game Launcher", AppCategory.GAMING, isPinnedDefault = true, isDesktopDefault = true, 780.dp, 520.dp),
    MEDIA_PLAYER("Media Player", AppCategory.MEDIA, isPinnedDefault = false, isDesktopDefault = true, 640.dp, 420.dp),
    MICROSOFT_HUB("Microsoft 365 & Store Hub", AppCategory.PRODUCTIVITY, isPinnedDefault = true, isDesktopDefault = true, 740.dp, 490.dp),
    TASK_MANAGER("Task Manager", AppCategory.SYSTEM, isPinnedDefault = false, isDesktopDefault = false, 680.dp, 440.dp),
    SETTINGS("Settings", AppCategory.SYSTEM, isPinnedDefault = true, isDesktopDefault = true, 760.dp, 500.dp),
    THEME_CENTER("Theme Center", AppCategory.PERSONALIZATION, isPinnedDefault = false, isDesktopDefault = true, 640.dp, 460.dp),
    RECYCLE_BIN("Recycle Bin", AppCategory.SYSTEM, isPinnedDefault = false, isDesktopDefault = true, 580.dp, 400.dp),
    RESOURCE_MANAGER("Hardware & RAM Manager", AppCategory.SYSTEM, isPinnedDefault = false, isDesktopDefault = true, 700.dp, 480.dp)
}

enum class AppCategory(val label: String) {
    SYSTEM("System & Core"),
    PRODUCTIVITY("Productivity & Office"),
    VIRTUALIZATION("Cloud PC & VM"),
    UTILITIES("Tools & Utilities"),
    DEVELOPMENT("Developer & CLI"),
    GAMING("PC Gaming & Streaming"),
    MEDIA("Media & Audio"),
    PERSONALIZATION("Personalization")
}

/**
 * State representing an active window on the desktop.
 */
data class WindowState(
    val id: String,
    val appId: AppId,
    val title: String = appId.title,
    val xOffset: Dp = 100.dp,
    val yOffset: Dp = 50.dp,
    val width: Dp = appId.defaultWidth,
    val height: Dp = appId.defaultHeight,
    val isMinimized: Boolean = false,
    val isMaximized: Boolean = false,
    val isSnappedLeft: Boolean = false,
    val isSnappedRight: Boolean = false,
    val zIndex: Float = 1f,
    val isFocused: Boolean = true,
    // Saved bounds before maximize/snap to restore properly
    val preRestorationX: Dp = 100.dp,
    val preRestorationY: Dp = 50.dp,
    val preRestorationWidth: Dp = appId.defaultWidth,
    val preRestorationHeight: Dp = appId.defaultHeight,
    val customData: String? = null
)

/**
 * Represents a desktop file item for the File Explorer.
 */
data class DesktopFileItem(
    val id: String,
    val name: String,
    val extension: String = "",
    val path: String,
    val sizeBytes: Long,
    val modifiedTimestamp: Long = System.currentTimeMillis(),
    val isDirectory: Boolean = false,
    val isSystemFolder: Boolean = false,
    val isCloudStorage: Boolean = false,
    val content: String = ""
)

/**
 * Represents system notification.
 */
data class DesktopNotification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val sourceApp: String,
    val isRead: Boolean = false
)

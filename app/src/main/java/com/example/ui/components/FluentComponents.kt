package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.AppId
import com.example.model.DesktopThemeType

/**
 * Returns fluent-styled vector icon matching the Windows 11 desktop app aesthetic.
 */
@Composable
fun AppIconVector(appId: AppId, size: Dp = 28.dp, modifier: Modifier = Modifier) {
    val (icon, bgGradient) = when (appId) {
        AppId.FILE_EXPLORER -> Icons.Filled.Folder to listOf(Color(0xFFFFD54F), Color(0xFFFFB300))
        AppId.BROWSER -> Icons.Filled.Language to listOf(Color(0xFF0078D4), Color(0xFF00C7FD))
        AppId.CLOUD_PC -> Icons.Filled.Cloud to listOf(Color(0xFF0078D4), Color(0xFF50E6FF))
        AppId.NOTEPAD -> Icons.Filled.EditNote to listOf(Color(0xFF0078D4), Color(0xFF26A69A))
        AppId.CALCULATOR -> Icons.Filled.Calculate to listOf(Color(0xFF00897B), Color(0xFF4DB6AC))
        AppId.TERMINAL -> Icons.Filled.Terminal to listOf(Color(0xFF263238), Color(0xFF37474F))
        AppId.GAMING_HUB -> Icons.Filled.SportsEsports to listOf(Color(0xFF7C4DFF), Color(0xFFFF4081))
        AppId.MEDIA_PLAYER -> Icons.Filled.PlayCircle to listOf(Color(0xFFFF5252), Color(0xFFFF7043))
        AppId.MICROSOFT_HUB -> Icons.Filled.Widgets to listOf(Color(0xFF0078D4), Color(0xFF1E88E5))
        AppId.TASK_MANAGER -> Icons.Filled.QueryStats to listOf(Color(0xFF00ACC1), Color(0xFF00838F))
        AppId.SETTINGS -> Icons.Filled.Settings to listOf(Color(0xFF78909C), Color(0xFF546E7A))
        AppId.THEME_CENTER -> Icons.Filled.Palette to listOf(Color(0xFFAB47BC), Color(0xFF8E24AA))
        AppId.RECYCLE_BIN -> Icons.Filled.DeleteOutline to listOf(Color(0xFF90A4AE), Color(0xFF607D8B))
        AppId.RESOURCE_MANAGER -> Icons.Filled.Memory to listOf(Color(0xFF00B0FF), Color(0xFF0091EA))
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.26f))
            .background(Brush.linearGradient(bgGradient)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = appId.title,
            tint = Color.White,
            modifier = Modifier.size(size * 0.62f)
        )
    }
}

/**
 * Fluent acrylic surface container with glass border and drop shadow.
 */
@Composable
fun FluentSurface(
    modifier: Modifier = Modifier,
    theme: DesktopThemeType,
    cornerRadius: Dp = theme.cornerRadius.dp,
    elevation: Dp = 8.dp,
    borderAlpha: Float = 0.15f,
    content: @Composable BoxScope.() -> Unit
) {
    val borderColor = if (theme.isDark) Color(0xFFFFFFFF).copy(alpha = borderAlpha) else Color(0xFF000000).copy(alpha = borderAlpha)
    
    Box(
        modifier = modifier
            .shadow(elevation, RoundedCornerShape(cornerRadius), ambientColor = Color.Black.copy(alpha = 0.3f))
            .clip(RoundedCornerShape(cornerRadius))
            .background(theme.windowSurface)
            .border(1.dp, borderColor, RoundedCornerShape(cornerRadius)),
        content = content
    )
}

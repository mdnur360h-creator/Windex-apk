package com.example.ui.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.AppId
import com.example.model.DesktopThemeType
import com.example.viewmodel.DesktopViewModel

@Composable
fun DesktopContextMenu(
    x: Dp,
    y: Dp,
    theme: DesktopThemeType,
    viewModel: DesktopViewModel,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .offset(x = x.coerceAtMost(500.dp), y = y.coerceAtMost(300.dp))
            .width(220.dp)
            .zIndex(999f)
            .shadow(16.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(theme.windowSurface)
            .border(1.dp, if (theme.isDark) Color(0x33FFFFFF) else Color(0x22000000), RoundedCornerShape(8.dp))
            .padding(6.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            ContextMenuItem(
                title = "View (Medium Icons)",
                icon = Icons.Default.GridView,
                theme = theme,
                onClick = { onDismiss() }
            )
            ContextMenuItem(
                title = "Sort by Name",
                icon = Icons.Default.SortByAlpha,
                theme = theme,
                onClick = { onDismiss() }
            )
            ContextMenuItem(
                title = "Refresh Desktop",
                icon = Icons.Default.Refresh,
                theme = theme,
                onClick = { onDismiss() }
            )

            Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))

            ContextMenuItem(
                title = "New Text Document",
                icon = Icons.Default.NoteAdd,
                theme = theme,
                onClick = {
                    viewModel.createNewDesktopFile("New_Document.txt", "Created from context menu.")
                    onDismiss()
                }
            )
            ContextMenuItem(
                title = "Open in Terminal",
                icon = Icons.Default.Terminal,
                theme = theme,
                onClick = {
                    viewModel.openApp(AppId.TERMINAL)
                    onDismiss()
                }
            )

            Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))

            ContextMenuItem(
                title = "Display Settings",
                icon = Icons.Default.DisplaySettings,
                theme = theme,
                onClick = {
                    viewModel.openApp(AppId.SETTINGS)
                    onDismiss()
                }
            )
            ContextMenuItem(
                title = "Personalize",
                icon = Icons.Default.Palette,
                theme = theme,
                onClick = {
                    viewModel.openApp(AppId.THEME_CENTER)
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private fun ContextMenuItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    theme: DesktopThemeType,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = theme.textSecondary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = title, color = theme.textPrimary, fontSize = 11.sp)
    }
}

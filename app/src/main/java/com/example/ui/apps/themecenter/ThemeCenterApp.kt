package com.example.ui.apps.themecenter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DesktopThemeType
import com.example.viewmodel.DesktopViewModel

@Composable
fun ThemeCenterApp(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (theme.isDark) Color(0xFF181818) else Color(0xFFF8FAFC))
            .padding(14.dp)
    ) {
        Text("Desktop Theme Center", color = theme.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text("Choose from authentic Windows and custom desktop operating system aesthetics.", color = theme.textSecondary, fontSize = 11.sp)

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 180.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(DesktopThemeType.values()) { thm ->
                val isSelected = uiState.currentTheme == thm

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { viewModel.setTheme(thm) },
                    color = if (theme.isDark) Color(0xFF242424) else Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected) 2.dp else 1.dp,
                        if (isSelected) theme.primaryAccent else (if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Palette preview swatches
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.weight(1f).height(24.dp).clip(RoundedCornerShape(4.dp)).background(thm.primaryAccent))
                            Box(modifier = Modifier.weight(1f).height(24.dp).clip(RoundedCornerShape(4.dp)).background(thm.taskbarBackground))
                            Box(modifier = Modifier.weight(1f).height(24.dp).clip(RoundedCornerShape(4.dp)).background(thm.windowSurface))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(thm.displayName, color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(theme.primaryAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = "Active", tint = Color.White, modifier = Modifier.size(12.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(thm.description, color = theme.textSecondary, fontSize = 10.sp, maxLines = 2)
                    }
                }
            }
        }
    }
}

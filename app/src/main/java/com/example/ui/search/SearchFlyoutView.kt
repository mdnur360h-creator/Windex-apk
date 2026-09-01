package com.example.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.AppId
import com.example.model.DesktopThemeType
import com.example.ui.components.AppIconVector
import com.example.viewmodel.DesktopViewModel

@Composable
fun SearchFlyoutView(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    val allApps = remember { AppId.values().toList() }

    val matchedApps = remember(query) {
        if (query.isBlank()) allApps
        else allApps.filter { it.title.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .width(520.dp)
            .height(480.dp)
            .zIndex(1000f)
            .shadow(24.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(theme.windowSurface)
            .border(1.dp, if (theme.isDark) Color(0x33FFFFFF) else Color(0x22000000), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search apps, files, web, and settings...", fontSize = 12.sp) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = theme.primaryAccent, modifier = Modifier.size(18.dp)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = theme.textPrimary,
                    unfocusedTextColor = theme.textPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (query.isBlank()) "Top Apps & Results" else "Search Results (${matchedApps.size})",
                color = theme.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(matchedApps) { app ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                viewModel.openApp(app)
                                viewModel.closeAllFlyouts()
                            },
                        color = Color.Transparent
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppIconVector(appId = app, size = 26.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(app.title, color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Text("Desktop Application", color = theme.textSecondary, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

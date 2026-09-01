package com.example.ui.startmenu

import androidx.compose.animation.*
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.AppId
import com.example.model.DesktopThemeType
import com.example.ui.components.AppIconVector
import com.example.viewmodel.DesktopViewModel

@Composable
fun StartMenuView(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showPowerMenu by remember { mutableStateOf(false) }

    val pinnedApps = remember {
        listOf(
            AppId.CLOUD_PC,
            AppId.FILE_EXPLORER,
            AppId.BROWSER,
            AppId.MICROSOFT_HUB,
            AppId.GAMING_HUB,
            AppId.NOTEPAD,
            AppId.CALCULATOR,
            AppId.TERMINAL,
            AppId.TASK_MANAGER,
            AppId.SETTINGS,
            AppId.THEME_CENTER,
            AppId.MEDIA_PLAYER
        )
    }

    val recommendedItems = remember {
        listOf(
            Triple("Cloud_PC_Architecture_Specs.txt", "Modified 10m ago", Icons.Default.Description),
            Triple("Virtual_Memory_12GB_Config.json", "Modified 2h ago", Icons.Default.Code),
            Triple("Cyberpunk_2077_Savegame.dat", "Cloud synced", Icons.Default.SportsEsports)
        )
    }

    Box(
        modifier = modifier
            .width(520.dp)
            .height(540.dp)
            .zIndex(1000f)
            .shadow(24.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(theme.windowSurface)
            .border(1.dp, if (theme.isDark) Color(0x33FFFFFF) else Color(0x22000000), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search Box at Top of Start Menu
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Type here to search apps, settings, and documents", fontSize = 12.sp) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = theme.textSecondary, modifier = Modifier.size(18.dp)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = theme.textPrimary,
                    unfocusedTextColor = theme.textPrimary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Pinned Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Pinned", color = theme.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                TextButton(onClick = { viewModel.openApp(AppId.SETTINGS) }) {
                    Text("All apps >", fontSize = 11.sp, color = theme.primaryAccent)
                }
            }

            // Pinned Apps Grid (6 columns x 2 rows)
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                items(pinnedApps) { app ->
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                viewModel.openApp(app)
                                viewModel.closeAllFlyouts()
                            }
                            .padding(vertical = 6.dp, horizontal = 2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppIconVector(appId = app, size = 32.dp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = app.title,
                            color = theme.textPrimary,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Recommended Section Header
            Text("Recommended", color = theme.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                recommendedItems.forEach { item ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                viewModel.openApp(AppId.FILE_EXPLORER)
                                viewModel.closeAllFlyouts()
                            },
                        color = Color.Transparent
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = item.third, contentDescription = null, tint = theme.primaryAccent, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(item.first, color = theme.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                Text(item.second, color = theme.textSecondary, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))
            Spacer(modifier = Modifier.height(8.dp))

            // User Profile & Power Controls Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { viewModel.openApp(AppId.SETTINGS) }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(theme.primaryAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "User", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("User-PC", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                // Power Button with Dropdown
                Box {
                    IconButton(onClick = { showPowerMenu = !showPowerMenu }) {
                        Icon(Icons.Default.PowerSettingsNew, contentDescription = "Power", tint = theme.textPrimary, modifier = Modifier.size(20.dp))
                    }

                    DropdownMenu(
                        expanded = showPowerMenu,
                        onDismissRequest = { showPowerMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Lock Desktop Session") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            onClick = {
                                showPowerMenu = false
                                viewModel.lockDesktop()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Restart Virtual Machine (VM)") },
                            leadingIcon = { Icon(Icons.Default.Refresh, contentDescription = null) },
                            onClick = {
                                showPowerMenu = false
                                viewModel.restartVirtualEnvironment()
                                viewModel.closeAllFlyouts()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Disconnect Cloud PC Session") },
                            leadingIcon = { Icon(Icons.Default.CloudOff, contentDescription = null) },
                            onClick = {
                                showPowerMenu = false
                                viewModel.disconnectCloudPc()
                                viewModel.closeAllFlyouts()
                            }
                        )
                    }
                }
            }
        }
    }
}

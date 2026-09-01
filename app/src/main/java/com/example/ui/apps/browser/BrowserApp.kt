package com.example.ui.apps.browser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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

data class WebBookmark(val title: String, val url: String, val iconColor: Color)

@Composable
fun BrowserApp(
    theme: DesktopThemeType
) {
    var urlText by remember { mutableStateOf("https://microsoft365.com/apps") }
    var currentUrl by remember { mutableStateOf("https://microsoft365.com/apps") }
    var pageTitle by remember { mutableStateOf("Microsoft 365 Cloud Workspace") }
    var isLoading by remember { mutableStateOf(false) }

    val bookmarks = listOf(
        WebBookmark("Microsoft 365", "https://microsoft365.com/apps", Color(0xFF0078D4)),
        WebBookmark("Windows Cloud Portal", "https://windows365.microsoft.com", Color(0xFF00B4D8)),
        WebBookmark("Microsoft Store", "https://apps.microsoft.com", Color(0xFF00897B)),
        WebBookmark("Xbox Cloud Gaming", "https://xbox.com/play", Color(0xFF107C10)),
        WebBookmark("GitHub Desktop", "https://github.com", Color(0xFF24292E)),
        WebBookmark("Google Search", "https://google.com", Color(0xFFEA4335))
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Tab Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (theme.isDark) Color(0xFF1B1B1B) else Color(0xFFE2E8F0))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .width(220.dp),
                color = if (theme.isDark) Color(0xFF2B2B2B) else Color(0xFFFFFFFF)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = theme.primaryAccent, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(pageTitle, color = theme.textPrimary, fontSize = 11.sp, maxLines = 1)
                    }
                    Icon(Icons.Default.Close, contentDescription = null, tint = theme.textSecondary, modifier = Modifier.size(12.dp))
                }
            }

            IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Add, contentDescription = "New Tab", tint = theme.textSecondary, modifier = Modifier.size(14.dp))
            }
        }

        // Address Bar & Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (theme.isDark) Color(0xFF242424) else Color(0xFFF1F5F9))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IconButton(onClick = {}, modifier = Modifier.size(26.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = theme.textSecondary, modifier = Modifier.size(14.dp))
            }
            IconButton(onClick = {}, modifier = Modifier.size(26.dp)) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Forward", tint = theme.textSecondary, modifier = Modifier.size(14.dp))
            }
            IconButton(onClick = { currentUrl = urlText }, modifier = Modifier.size(26.dp)) {
                Icon(Icons.Default.Refresh, contentDescription = "Reload", tint = theme.textSecondary, modifier = Modifier.size(14.dp))
            }

            // URL Input Box
            OutlinedTextField(
                value = urlText,
                onValueChange = { urlText = it },
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Secure", tint = Color(0xFF107C10), modifier = Modifier.size(12.dp)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = theme.textPrimary,
                    unfocusedTextColor = theme.textPrimary
                )
            )

            IconButton(onClick = {}, modifier = Modifier.size(26.dp)) {
                Icon(Icons.Default.StarBorder, contentDescription = "Favorite", tint = theme.textSecondary, modifier = Modifier.size(14.dp))
            }
        }

        // Bookmarks Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (theme.isDark) Color(0xFF1E1E1E) else Color(0xFFF8FAFC))
                .padding(horizontal = 8.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            bookmarks.forEach { bm ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            urlText = bm.url
                            currentUrl = bm.url
                            pageTitle = bm.title
                        },
                    color = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(bm.iconColor))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(bm.title, color = theme.textSecondary, fontSize = 10.sp)
                    }
                }
            }
        }

        Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))

        // Web Content Viewer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (theme.isDark) Color(0xFF121212) else Color(0xFFFFFFFF))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Surface(
                    color = theme.primaryAccent.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, theme.primaryAccent.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CloudDone, contentDescription = null, tint = theme.primaryAccent, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Edge Cloud Browser Engine • Full HTML5/Wasm • Hardware Acceleration Active",
                            color = theme.textPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(pageTitle, color = theme.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Navigated to: $currentUrl", color = theme.textSecondary, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Launcher Cards in Browser
                Text("Official Cloud & Web Services:", color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = if (theme.isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Microsoft 365 Web", color = Color(0xFF0078D4), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Access Word, Excel, PowerPoint, OneDrive & Outlook in cloud desktop.", color = theme.textSecondary, fontSize = 10.sp)
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = if (theme.isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Microsoft Store Web", color = Color(0xFF00897B), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Browse verified applications and tools for Windows cloud PC.", color = theme.textSecondary, fontSize = 10.sp)
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = if (theme.isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Xbox Cloud Gaming", color = Color(0xFF107C10), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Stream PC and console games at 60 FPS with low latency.", color = theme.textSecondary, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

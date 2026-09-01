package com.example.ui.apps.microsofthub

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.model.AppId
import com.example.model.DesktopThemeType
import com.example.viewmodel.DesktopViewModel

data class MsAppItem(
    val name: String,
    val description: String,
    val color: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val launchAction: String
)

@Composable
fun MicrosoftHubApp(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    val msApps = listOf(
        MsAppItem("Microsoft Word", "Documents & Writing via Cloud PC / Office Online", Color(0xFF185ABD), Icons.Default.Description, "word"),
        MsAppItem("Microsoft Excel", "Spreadsheets & Data Models via Cloud PC", Color(0xFF107C41), Icons.Default.TableChart, "excel"),
        MsAppItem("Microsoft PowerPoint", "Slide Presentations & Animations", Color(0xFFC43E1C), Icons.Default.Slideshow, "ppt"),
        MsAppItem("Microsoft OneNote", "Digital Notebook & Ink Capture", Color(0xFF7719AA), Icons.Default.StickyNote2, "onenote"),
        MsAppItem("Microsoft Teams", "Enterprise Meetings & Chat Client", Color(0xFF464EB8), Icons.Default.Groups, "teams"),
        MsAppItem("Microsoft Edge", "Full Chromium Cloud Desktop Browser", Color(0xFF0078D4), Icons.Default.Language, "edge"),
        MsAppItem("Paint 3D", "Canvas Drawing & Image Editing", Color(0xFFE91E63), Icons.Default.Brush, "paint"),
        MsAppItem("Microsoft Store", "Official Software & Store Repository", Color(0xFF00897B), Icons.Default.Storefront, "store")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (theme.isDark) Color(0xFF1B1B1B) else Color(0xFFF8FAFC))
            .padding(14.dp)
    ) {
        // Explanatory Banner (Strict Compliance with Scope & Transparency)
        Surface(
            color = theme.primaryAccent.copy(alpha = 0.1f),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, theme.primaryAccent.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = theme.primaryAccent, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Legitimate Microsoft Application Hub: Access licensed Microsoft 365, Office Online, and Microsoft Store apps seamlessly through your Windows Cloud PC session or web environment.",
                    color = theme.textPrimary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Productivity & Official Store Applications",
            color = theme.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 180.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(msApps) { app ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            if (app.launchAction == "edge") {
                                viewModel.openApp(AppId.BROWSER)
                            } else {
                                viewModel.openApp(AppId.CLOUD_PC)
                            }
                        },
                    color = if (theme.isDark) Color(0xFF262626) else Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(app.color),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = app.icon, contentDescription = app.name, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(app.name, color = theme.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(app.description, color = theme.textSecondary, fontSize = 10.sp, maxLines = 2)
                    }
                }
            }
        }
    }
}

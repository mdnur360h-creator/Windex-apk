package com.example.ui.desktop

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppId
import com.example.model.DesktopThemeType
import com.example.ui.components.AppIconVector
import com.example.viewmodel.DesktopViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DesktopIconsView(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType,
    modifier: Modifier = Modifier
) {
    val desktopApps = remember {
        listOf(
            AppId.CLOUD_PC,
            AppId.FILE_EXPLORER,
            AppId.BROWSER,
            AppId.MICROSOFT_HUB,
            AppId.GAMING_HUB,
            AppId.TERMINAL,
            AppId.NOTEPAD,
            AppId.CALCULATOR,
            AppId.TASK_MANAGER,
            AppId.SETTINGS,
            AppId.THEME_CENTER,
            AppId.RECYCLE_BIN
        )
    }

    var selectedAppId by remember { mutableStateOf<AppId?>(null) }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        selectedAppId = null
                        viewModel.closeAllFlyouts()
                    },
                    onLongPress = { offset ->
                        with(density) {
                            viewModel.openContextMenu(offset.x.toDp(), offset.y.toDp())
                        }
                    }
                )
            }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            desktopApps.forEach { app ->
                val isSelected = selectedAppId == app

                Column(
                    modifier = Modifier
                        .width(72.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when {
                                isSelected -> Color.White.copy(alpha = 0.25f)
                                theme == DesktopThemeType.SLEEK_INTERFACE -> Color.Transparent
                                else -> Color.Transparent
                            }
                        )
                        .border(
                            1.dp,
                            if (isSelected) Color.White.copy(alpha = 0.5f) else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .combinedClickable(
                            onClick = {
                                if (selectedAppId == app) {
                                    viewModel.openApp(app)
                                } else {
                                    selectedAppId = app
                                }
                            },
                            onDoubleClick = { viewModel.openApp(app) },
                            onLongClick = { selectedAppId = app }
                        )
                        .padding(vertical = 4.dp, horizontal = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Frosted Glass Icon Tile Container
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) Color.White.copy(alpha = 0.3f)
                                else Color.White.copy(alpha = 0.15f)
                            )
                            .border(
                                1.dp,
                                if (isSelected) Color.White.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.2f),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AppIconVector(appId = app, size = 26.dp)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = app.title,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color = Color.Black.copy(alpha = 0.6f),
                                blurRadius = 5f
                            )
                        )
                    )
                }
            }
        }
    }
}

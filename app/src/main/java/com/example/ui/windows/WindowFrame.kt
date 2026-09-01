package com.example.ui.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.DesktopThemeType
import com.example.model.WindowState
import com.example.ui.components.AppIconVector

@Composable
fun WindowFrame(
    window: WindowState,
    theme: DesktopThemeType,
    containerWidth: Dp,
    containerHeight: Dp,
    onFocus: () -> Unit,
    onClose: () -> Unit,
    onMinimize: () -> Unit,
    onToggleMaximize: () -> Unit,
    onSnapLeft: () -> Unit,
    onSnapRight: () -> Unit,
    onMove: (deltaX: Dp, deltaY: Dp) -> Unit,
    onResize: (deltaW: Dp, deltaH: Dp) -> Unit,
    content: @Composable () -> Unit
) {
    if (window.isMinimized) return

    val density = LocalDensity.current
    val isSnapped = window.isSnappedLeft || window.isSnappedRight
    val isMaximized = window.isMaximized

    // Compute effective bounds
    val currentWidth = when {
        isMaximized -> containerWidth
        isSnapped -> containerWidth / 2
        else -> window.width.coerceAtMost(containerWidth)
    }

    val currentHeight = when {
        isMaximized || isSnapped -> containerHeight
        else -> window.height.coerceAtMost(containerHeight)
    }

    val currentX = when {
        isMaximized -> 0.dp
        window.isSnappedLeft -> 0.dp
        window.isSnappedRight -> containerWidth / 2
        else -> window.xOffset.coerceIn(0.dp, (containerWidth - 100.dp).coerceAtLeast(0.dp))
    }

    val currentY = when {
        isMaximized || isSnapped -> 0.dp
        else -> window.yOffset.coerceIn(0.dp, (containerHeight - 80.dp).coerceAtLeast(0.dp))
    }

    val cornerRadius = if (isMaximized || isSnapped) 0.dp else theme.cornerRadius.dp
    val borderColor = if (window.isFocused) {
        theme.primaryAccent.copy(alpha = 0.6f)
    } else {
        if (theme.isDark) Color(0x33FFFFFF) else Color(0x22000000)
    }

    var showSnapMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .offset(x = currentX, y = currentY)
            .size(width = currentWidth, height = currentHeight)
            .zIndex(window.zIndex)
            .shadow(if (isMaximized || isSnapped) 0.dp else 16.dp, RoundedCornerShape(cornerRadius), ambientColor = Color.Black.copy(alpha = 0.4f))
            .clip(RoundedCornerShape(cornerRadius))
            .background(theme.windowSurface)
            .border(if (isMaximized || isSnapped) 0.dp else 1.dp, borderColor, RoundedCornerShape(cornerRadius))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onFocus() }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Window Title Bar (Drag Area)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .background(if (window.isFocused) theme.windowHeader else theme.windowHeader.copy(alpha = 0.7f))
                    .pointerInput(window.id, isMaximized, isSnapped) {
                        if (!isMaximized && !isSnapped) {
                            detectDragGestures(
                                onDragStart = { onFocus() },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    with(density) {
                                        onMove(dragAmount.x.toDp(), dragAmount.y.toDp())
                                    }
                                }
                            )
                        }
                    }
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Title & Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    AppIconVector(appId = window.appId, size = 18.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = window.title,
                        color = theme.textPrimary,
                        fontSize = 12.sp,
                        fontWeight = if (window.isFocused) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Window Control Buttons (Minimize, Snap/Maximize, Close)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Minimize Button
                    IconButton(
                        onClick = onMinimize,
                        modifier = Modifier.size(32.dp, 28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Minimize,
                            contentDescription = "Minimize",
                            tint = theme.textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    // Maximize / Restore Button with Snap dropdown toggle
                    Box {
                        IconButton(
                            onClick = onToggleMaximize,
                            modifier = Modifier.size(32.dp, 28.dp)
                        ) {
                            Icon(
                                imageVector = if (isMaximized) Icons.Default.FilterNone else Icons.Default.CropSquare,
                                contentDescription = if (isMaximized) "Restore" else "Maximize",
                                tint = theme.textSecondary,
                                modifier = Modifier.size(13.dp)
                            )
                        }

                        // Snap layout mini popup menu
                        DropdownMenu(
                            expanded = showSnapMenu,
                            onDismissRequest = { showSnapMenu = false },
                            modifier = Modifier.background(theme.windowSurface)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Snap Left (50%)", color = theme.textPrimary, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Default.AlignHorizontalLeft, contentDescription = null, tint = theme.primaryAccent) },
                                onClick = {
                                    showSnapMenu = false
                                    onSnapLeft()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Snap Right (50%)", color = theme.textPrimary, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Default.AlignHorizontalRight, contentDescription = null, tint = theme.primaryAccent) },
                                onClick = {
                                    showSnapMenu = false
                                    onSnapRight()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Maximize Full Screen", color = theme.textPrimary, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Default.Fullscreen, contentDescription = null, tint = theme.primaryAccent) },
                                onClick = {
                                    showSnapMenu = false
                                    onToggleMaximize()
                                }
                            )
                        }
                    }

                    // Close Button
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp, 28.dp)
                            .clip(RoundedCornerShape(4.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = if (window.isFocused) Color(0xFFE53935) else theme.textSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            // Window Content Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(theme.windowSurface)
            ) {
                content()
            }
        }

        // Bottom-right resize handle (when not maximized / snapped)
        if (!isMaximized && !isSnapped) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(20.dp)
                    .pointerInput(window.id) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            with(density) {
                                onResize(dragAmount.x.toDp(), dragAmount.y.toDp())
                            }
                        }
                    }
                    .padding(4.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Icon(
                    imageVector = Icons.Default.NorthWest, // Diagonal resize representation
                    contentDescription = "Resize Window",
                    tint = theme.textSecondary.copy(alpha = 0.5f),
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }
}

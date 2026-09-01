package com.example.ui.desktop

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.R
import com.example.model.DesktopThemeType
import com.example.model.TaskbarAlignment
import com.example.ui.flyouts.ActionCenterFlyout
import com.example.ui.flyouts.CalendarFlyout
import com.example.ui.lockscreen.LockScreenView
import com.example.ui.onboarding.OnboardingWizard
import com.example.ui.search.SearchFlyoutView
import com.example.ui.startmenu.StartMenuView
import com.example.ui.taskbar.TaskbarView
import com.example.ui.windows.WindowManagerView
import com.example.viewmodel.DesktopViewModel

@Composable
fun DesktopScreen(
    viewModel: DesktopViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentTheme = uiState.currentTheme

    val wallpaperRes = when (currentTheme) {
        DesktopThemeType.WINDOWS_11_DARK, DesktopThemeType.WINDOWS_10_DARK, DesktopThemeType.MINIMAL_OBSIDIAN -> R.drawable.bg_wallpaper_dark
        else -> R.drawable.bg_wallpaper_bloom
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    viewModel.closeAllFlyouts()
                })
            }
    ) {
        // Desktop Wallpaper
        if (currentTheme == DesktopThemeType.SLEEK_INTERFACE) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF2E71D3),
                                Color(0xFF005A9E),
                                Color(0xFF004A87)
                            ),
                            radius = 1400f
                        )
                    )
            )
        } else {
            Image(
                painter = painterResource(id = wallpaperRes),
                contentDescription = "Desktop Wallpaper",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Night Light Ambient Filter Overlay
        if (uiState.isNightLightEnabled) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFB74D).copy(alpha = 0.12f))
            )
        }

        // Main Desktop Layout (Desktop Area + Bottom Taskbar)
        Column(modifier = Modifier.fillMaxSize()) {
            // Desktop Workspace Canvas (Icons + Windows)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Desktop Shortcut Icons
                DesktopIconsView(
                    viewModel = viewModel,
                    theme = currentTheme,
                    modifier = Modifier.fillMaxSize()
                )

                // Floating Windows Manager
                WindowManagerView(
                    activeWindows = uiState.activeWindows,
                    theme = currentTheme,
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )

                // Desktop Right-Click Context Menu
                if (uiState.isContextMenuOpen) {
                    DesktopContextMenu(
                        x = uiState.contextMenuX,
                        y = uiState.contextMenuY,
                        theme = currentTheme,
                        viewModel = viewModel,
                        onDismiss = { viewModel.closeAllFlyouts() }
                    )
                }
            }

            // Bottom Taskbar (Windows 11 Centered or Classic Left)
            TaskbarView(
                uiState = uiState,
                viewModel = viewModel,
                theme = currentTheme
            )
        }

        // Flyout Overlays (Anchored above Taskbar)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 54.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Windows 11 Start Menu
            AnimatedVisibility(
                visible = uiState.isStartMenuOpen,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
                modifier = Modifier.align(
                    if (uiState.taskbarAlignment == TaskbarAlignment.CENTER) Alignment.BottomCenter else Alignment.BottomStart
                ).padding(start = if (uiState.taskbarAlignment == TaskbarAlignment.LEFT) 12.dp else 0.dp)
            ) {
                StartMenuView(viewModel = viewModel, theme = currentTheme)
            }

            // Search Flyout
            AnimatedVisibility(
                visible = uiState.isSearchOpen,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
                modifier = Modifier.align(
                    if (uiState.taskbarAlignment == TaskbarAlignment.CENTER) Alignment.BottomCenter else Alignment.BottomStart
                ).padding(start = if (uiState.taskbarAlignment == TaskbarAlignment.LEFT) 48.dp else 0.dp)
            ) {
                SearchFlyoutView(viewModel = viewModel, theme = currentTheme)
            }

            // Action Center / Quick Settings Flyout (Bottom Right)
            AnimatedVisibility(
                visible = uiState.isActionCenterOpen,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 12.dp)
            ) {
                ActionCenterFlyout(uiState = uiState, viewModel = viewModel, theme = currentTheme)
            }

            // Calendar Flyout (Bottom Right)
            AnimatedVisibility(
                visible = uiState.isCalendarFlyoutOpen,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 80.dp)
            ) {
                CalendarFlyout(uiState = uiState, theme = currentTheme)
            }
        }

        // Lock Screen Overlay
        if (uiState.isDesktopLocked) {
            Box(modifier = Modifier.fillMaxSize().zIndex(9999f)) {
                LockScreenView(viewModel = viewModel, theme = currentTheme)
            }
        }

        // Onboarding First-Run Wizard
        if (uiState.isOnboardingOpen) {
            Box(modifier = Modifier.fillMaxSize().zIndex(9998f)) {
                OnboardingWizard(viewModel = viewModel, theme = currentTheme)
            }
        }
    }
}

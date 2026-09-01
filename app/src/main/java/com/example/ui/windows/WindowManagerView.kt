package com.example.ui.windows

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.AppId
import com.example.model.DesktopThemeType
import com.example.model.WindowState
import com.example.ui.apps.browser.BrowserApp
import com.example.ui.apps.calculator.CalculatorApp
import com.example.ui.apps.cloudpc.CloudPcApp
import com.example.ui.apps.fileexplorer.FileExplorerApp
import com.example.ui.apps.gaming.GamingHubApp
import com.example.ui.apps.mediaplayer.MediaPlayerApp
import com.example.ui.apps.microsofthub.MicrosoftHubApp
import com.example.ui.apps.notepad.NotepadApp
import com.example.ui.apps.settings.SettingsApp
import com.example.ui.apps.taskmanager.TaskManagerApp
import com.example.ui.apps.terminal.TerminalApp
import com.example.ui.apps.themecenter.ThemeCenterApp
import com.example.viewmodel.DesktopViewModel

@Composable
fun WindowManagerView(
    activeWindows: List<WindowState>,
    theme: DesktopThemeType,
    viewModel: DesktopViewModel,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val containerWidth = maxWidth
        val containerHeight = maxHeight

        activeWindows.forEach { window ->
            WindowFrame(
                window = window,
                theme = theme,
                containerWidth = containerWidth,
                containerHeight = containerHeight,
                onFocus = { viewModel.bringWindowToFront(window.id) },
                onClose = { viewModel.closeWindow(window.id) },
                onMinimize = { viewModel.minimizeWindow(window.id) },
                onToggleMaximize = { viewModel.toggleMaximizeWindow(window.id) },
                onSnapLeft = { viewModel.snapWindowLeft(window.id) },
                onSnapRight = { viewModel.snapWindowRight(window.id) },
                onMove = { dx, dy -> viewModel.updateWindowPosition(window.id, dx, dy) },
                onResize = { dw, dh -> viewModel.updateWindowSize(window.id, dw, dh) }
            ) {
                // Dispatch App Component Content
                when (window.appId) {
                    AppId.CLOUD_PC -> CloudPcApp(viewModel, theme)
                    AppId.FILE_EXPLORER, AppId.RECYCLE_BIN -> FileExplorerApp(viewModel, theme)
                    AppId.BROWSER -> BrowserApp(theme)
                    AppId.NOTEPAD -> NotepadApp(viewModel, theme)
                    AppId.CALCULATOR -> CalculatorApp(theme)
                    AppId.TERMINAL -> TerminalApp(viewModel, theme)
                    AppId.GAMING_HUB -> GamingHubApp(viewModel, theme)
                    AppId.MEDIA_PLAYER -> MediaPlayerApp(theme)
                    AppId.MICROSOFT_HUB -> MicrosoftHubApp(viewModel, theme)
                    AppId.TASK_MANAGER, AppId.RESOURCE_MANAGER -> TaskManagerApp(viewModel, theme)
                    AppId.SETTINGS -> SettingsApp(viewModel, theme)
                    AppId.THEME_CENTER -> ThemeCenterApp(viewModel, theme)
                }
            }
        }
    }
}

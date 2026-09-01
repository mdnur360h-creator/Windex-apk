package com.example.ui.apps.fileexplorer

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppId
import com.example.model.DesktopFileItem
import com.example.model.DesktopThemeType
import com.example.viewmodel.DesktopViewModel

enum class ExplorerSection(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    HOME("Home / Quick Access", Icons.Default.Home),
    DOCUMENTS("Documents", Icons.Default.Description),
    DOWNLOADS("Downloads", Icons.Default.Download),
    PICTURES("Pictures", Icons.Default.Image),
    VIDEOS("Videos", Icons.Default.VideoLibrary),
    MUSIC("Music", Icons.Default.LibraryMusic),
    C_DRIVE("Windows (C:) [256 GB]", Icons.Default.Storage),
    ANDROID_STORAGE("Android Host Storage", Icons.Default.SdCard),
    CLOUD_DRIVE("Cloud PC Drive", Icons.Default.Cloud)
}

@Composable
fun FileExplorerApp(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentSection by remember { mutableStateOf(ExplorerSection.HOME) }
    var searchQuery by remember { mutableStateOf("") }
    var isGridView by remember { mutableStateOf(true) }
    var selectedFileId by remember { mutableStateOf<String?>(null) }
    var showNewFileDialog by remember { mutableStateOf(false) }
    var newFileName by remember { mutableStateOf("New_Note.txt") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Windows 11 Ribbon Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (theme.isDark) Color(0xFF1F1F1F) else Color(0xFFF1F5F9))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                // New Item Button
                TextButton(
                    onClick = { showNewFileDialog = true },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "New", tint = theme.primaryAccent, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New", fontSize = 11.sp, color = theme.textPrimary)
                }

                // Delete Button
                TextButton(
                    onClick = {
                        selectedFileId?.let { viewModel.deleteDesktopFile(it); selectedFileId = null }
                    },
                    enabled = selectedFileId != null,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete", fontSize = 11.sp)
                }

                // Toggle Grid / List
                IconButton(onClick = { isGridView = !isGridView }, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                        contentDescription = "View Mode",
                        tint = theme.textSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Quick Search Box
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search files...", fontSize = 11.sp) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(14.dp)) },
                modifier = Modifier
                    .width(180.dp)
                    .height(34.dp),
                shape = RoundedCornerShape(6.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = theme.textPrimary,
                    unfocusedTextColor = theme.textPrimary
                )
            )
        }

        // Breadcrumb Address Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (theme.isDark) Color(0xFF181818) else Color(0xFFE2E8F0))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Folder, contentDescription = null, tint = theme.primaryAccent, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "This PC > ${currentSection.title}",
                fontSize = 11.sp,
                color = theme.textSecondary,
                fontWeight = FontWeight.Medium
            )
        }

        Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))

        // Main Explorer Body (Sidebar + Content Pane)
        Row(modifier = Modifier.fillMaxSize()) {
            // Left Navigation Pane
            Column(
                modifier = Modifier
                    .width(160.dp)
                    .fillMaxHeight()
                    .background(if (theme.isDark) Color(0xFF181818) else Color(0xFFF8FAFC))
                    .padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                ExplorerSection.values().forEach { section ->
                    val isSelected = currentSection == section
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) theme.primaryAccent.copy(alpha = 0.15f) else Color.Transparent)
                            .clickable { currentSection = section }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = section.icon,
                            contentDescription = section.title,
                            tint = if (isSelected) theme.primaryAccent else theme.textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = section.title,
                            fontSize = 11.sp,
                            color = if (isSelected) theme.primaryAccent else theme.textPrimary,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight(),
                color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000)
            )

            // Right Files List/Grid
            val filteredFiles = uiState.desktopFiles.filter {
                if (searchQuery.isBlank()) true else it.name.contains(searchQuery, ignoreCase = true)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                if (filteredFiles.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.FolderOpen, contentDescription = null, tint = theme.textSecondary, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("This folder is empty", color = theme.textSecondary, fontSize = 12.sp)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = if (isGridView) 100.dp else 240.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredFiles) { file ->
                            val isSelected = selectedFileId == file.id
                            FileItemCard(
                                file = file,
                                isSelected = isSelected,
                                isGridView = isGridView,
                                theme = theme,
                                onClick = { selectedFileId = file.id },
                                onDoubleClick = {
                                    if (file.extension == "txt" || file.extension == "log") {
                                        viewModel.updateNotepadContent(file.content)
                                        viewModel.openApp(AppId.NOTEPAD)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // New File Dialog
    if (showNewFileDialog) {
        AlertDialog(
            onDismissRequest = { showNewFileDialog = false },
            title = { Text("Create New Document", fontSize = 14.sp, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newFileName,
                    onValueChange = { newFileName = it },
                    label = { Text("File Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (newFileName.isNotBlank()) {
                        viewModel.createNewDesktopFile(newFileName, "New file created on WinDesk Cloud PC.")
                        showNewFileDialog = false
                    }
                }) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewFileDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun FileItemCard(
    file: DesktopFileItem,
    isSelected: Boolean,
    isGridView: Boolean,
    theme: DesktopThemeType,
    onClick: () -> Unit,
    onDoubleClick: () -> Unit
) {
    val fileIcon = when (file.extension.lowercase()) {
        "txt", "log" -> Icons.Default.Description
        "pdf" -> Icons.Default.PictureAsPdf
        "jpg", "png" -> Icons.Default.Image
        "mp3", "wav" -> Icons.Default.Audiotrack
        "mp4", "mkv" -> Icons.Default.VideoFile
        else -> if (file.isDirectory) Icons.Default.Folder else Icons.Default.InsertDriveFile
    }

    val iconTint = if (file.isDirectory) Color(0xFFFFB300) else theme.primaryAccent

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) theme.primaryAccent.copy(alpha = 0.2f) else Color.Transparent)
            .border(
                1.dp,
                if (isSelected) theme.primaryAccent else Color.Transparent,
                RoundedCornerShape(6.dp)
            )
            .clickable {
                onClick()
                onDoubleClick()
            },
        color = Color.Transparent
    ) {
        if (isGridView) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = fileIcon, contentDescription = file.name, tint = iconTint, modifier = Modifier.size(34.dp))
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = file.name,
                    color = theme.textPrimary,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = fileIcon, contentDescription = file.name, tint = iconTint, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = file.name,
                    color = theme.textPrimary,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

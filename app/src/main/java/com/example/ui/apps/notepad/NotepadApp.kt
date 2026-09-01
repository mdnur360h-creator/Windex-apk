package com.example.ui.apps.notepad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DesktopThemeType
import com.example.viewmodel.DesktopViewModel

@Composable
fun NotepadApp(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    val uiState by viewModel.uiState.collectAsState()
    var text by remember { mutableStateOf(uiState.notepadContent) }
    var fileName by remember { mutableStateOf("Untitled.txt") }
    var showSavedNotification by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.notepadContent) {
        text = uiState.notepadContent
    }

    val charCount = text.length
    val lineCount = if (text.isEmpty()) 1 else text.lines().size
    val wordCount = if (text.isBlank()) 0 else text.trim().split(Regex("\\s+")).size

    Column(modifier = Modifier.fillMaxSize()) {
        // Notepad Menu Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (theme.isDark) Color(0xFF1F1F1F) else Color(0xFFF1F5F9))
                .padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { text = ""; fileName = "Untitled.txt" }, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)) {
                Text("New", fontSize = 11.sp, color = theme.textPrimary)
            }
            TextButton(onClick = {
                viewModel.createNewDesktopFile(fileName, text)
                showSavedNotification = true
            }, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)) {
                Text("Save", fontSize = 11.sp, color = theme.textPrimary)
            }
            TextButton(onClick = {
                val sampleCode = "public class WinDesk {\n    public static void main(String[] args) {\n        System.out.println(\"Running on 12 GB VM!\");\n    }\n}"
                text = sampleCode
                fileName = "WinDesk.java"
            }, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)) {
                Text("Insert Sample", fontSize = 11.sp, color = theme.textPrimary)
            }
        }

        Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))

        // Editor Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(if (theme.isDark) Color(0xFF1A1A1A) else Color(0xFFFFFFFF))
                .padding(12.dp)
        ) {
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                    viewModel.updateNotepadContent(it)
                },
                textStyle = TextStyle(
                    color = theme.textPrimary,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 18.sp
                ),
                cursorBrush = SolidColor(theme.primaryAccent),
                modifier = Modifier.fillMaxSize()
            )
        }

        Divider(color = if (theme.isDark) Color(0x22FFFFFF) else Color(0x15000000))

        // Status Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(if (theme.isDark) Color(0xFF181818) else Color(0xFFF1F5F9))
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Ln $lineCount, Col $charCount  |  $wordCount words", color = theme.textSecondary, fontSize = 10.sp)
            Text("UTF-8  |  Windows (CRLF)  |  100%", color = theme.textSecondary, fontSize = 10.sp)
        }
    }
}

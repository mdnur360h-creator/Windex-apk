package com.example.ui.apps.terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DesktopThemeType
import com.example.viewmodel.DesktopViewModel
import kotlinx.coroutines.launch

data class TerminalLog(val text: String, val color: Color = Color(0xFFE2E8F0))

@Composable
fun TerminalApp(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    val uiState by viewModel.uiState.collectAsState()
    val telemetry = uiState.telemetry
    val config = uiState.cloudPcConfig

    var currentInput by remember { mutableStateOf("") }
    val logs = remember {
        mutableStateListOf(
            TerminalLog("Windows PowerShell", Color(0xFF38BDF8)),
            TerminalLog("Copyright (C) Microsoft Corporation. All rights reserved.", Color(0xFF94A3B8)),
            TerminalLog("WinDesk Cloud PC Virtualization Subsystem [Active]", Color(0xFF4ADE80)),
            TerminalLog("Type 'help' for a list of supported commands.", Color(0xFFCBD5E1)),
            TerminalLog("")
        )
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    fun executeCommand(rawCmd: String) {
        val cmd = rawCmd.trim()
        logs.add(TerminalLog("PS C:\\Users\\Administrator> $cmd", Color(0xFF38BDF8)))

        val parts = cmd.split(" ")
        val mainCmd = parts.firstOrNull()?.lowercase() ?: ""

        when (mainCmd) {
            "help" -> {
                logs.add(TerminalLog("Supported WinDesk Console Commands:", Color(0xFFFBBF24)))
                logs.add(TerminalLog("  systeminfo  - View Android host & VM guest hardware metrics"))
                logs.add(TerminalLog("  vmstat      - Check target RAM (12GB) and storage (256GB)"))
                logs.add(TerminalLog("  ipconfig    - View virtual network adapter & IP configuration"))
                logs.add(TerminalLog("  ping <host> - Test network latency to cloud PC server"))
                logs.add(TerminalLog("  dir / ls    - List desktop directory contents"))
                logs.add(TerminalLog("  tasklist    - Show currently active processes and windows"))
                logs.add(TerminalLog("  ver         - Display Windows 11 & WinDesk engine version"))
                logs.add(TerminalLog("  cls / clear - Clear console buffer"))
                logs.add(TerminalLog("  echo <msg>  - Print message to terminal"))
            }
            "systeminfo" -> {
                logs.add(TerminalLog("Host OS Name:                   Android 15 / 16 (Linux Kernel 6.6)", Color(0xFF38BDF8)))
                logs.add(TerminalLog("Host Physical RAM:              ${telemetry.physicalAndroidRamTotalGb} GB (${telemetry.physicalAndroidRamUsedGb} GB in use)"))
                logs.add(TerminalLog("Host Internal Storage:          ${telemetry.availableAndroidStorageGb} GB Free of ${telemetry.totalAndroidStorageGb} GB"))
                logs.add(TerminalLog("Virtual Machine Target RAM:     ${config.targetRam.gigaBytes} GB Target", Color(0xFF4ADE80)))
                logs.add(TerminalLog("Virtual Machine Target NVMe:    ${config.targetStorage.gigaBytes} GB Target", Color(0xFF4ADE80)))
                logs.add(TerminalLog("Cloud Session Status:           ${if (config.isConnected) "Connected (14ms)" else "Local Desktop Mode"}"))
                logs.add(TerminalLog("GPU Subsystem:                  ${telemetry.gpuRenderer}"))
            }
            "vmstat" -> {
                logs.add(TerminalLog("--- WinDesk Cloud PC Virtualization Specs ---", Color(0xFF4ADE80)))
                logs.add(TerminalLog("Target RAM Allocation:     ${config.targetRam.gigaBytes} GB"))
                logs.add(TerminalLog("Target Storage (C:\\):     ${config.targetStorage.gigaBytes} GB NVMe"))
                logs.add(TerminalLog("Streaming Pipeline:        ${config.resolution.label} @ ${config.fps.fps} FPS"))
                logs.add(TerminalLog("Active Protocol:           ${config.protocol.label}"))
            }
            "ipconfig" -> {
                logs.add(TerminalLog("Ethernet adapter WinDesk-vEthernet:", Color(0xFF38BDF8)))
                logs.add(TerminalLog("   Connection-specific DNS Suffix  . : internal.windesk.cloud"))
                logs.add(TerminalLog("   IPv4 Address. . . . . . . . . . . : 192.168.100.42"))
                logs.add(TerminalLog("   Subnet Mask . . . . . . . . . . . : 255.255.255.0"))
                logs.add(TerminalLog("   Default Gateway . . . . . . . . . : 192.168.100.1"))
            }
            "ping" -> {
                val target = parts.getOrNull(1) ?: "cloudpc.windesk.internal"
                logs.add(TerminalLog("Pinging $target [10.0.4.12] with 32 bytes of data:"))
                logs.add(TerminalLog("Reply from 10.0.4.12: bytes=32 time=${telemetry.streamLatencyMs}ms TTL=118"))
                logs.add(TerminalLog("Reply from 10.0.4.12: bytes=32 time=${telemetry.streamLatencyMs - 1}ms TTL=118"))
                logs.add(TerminalLog("Reply from 10.0.4.12: bytes=32 time=${telemetry.streamLatencyMs + 1}ms TTL=118"))
                logs.add(TerminalLog("Ping statistics for 10.0.4.12: Packets: Sent = 3, Received = 3, Lost = 0 (0% loss)", Color(0xFF4ADE80)))
            }
            "dir", "ls" -> {
                logs.add(TerminalLog(" Directory of C:\\Users\\Desktop", Color(0xFF38BDF8)))
                uiState.desktopFiles.forEach { file ->
                    val type = if (file.isDirectory) "<DIR>" else "     "
                    logs.add(TerminalLog("09/01/2026  12:00 PM    $type  ${file.sizeBytes}  ${file.name}"))
                }
            }
            "tasklist" -> {
                logs.add(TerminalLog("Image Name                   PID    Session Name        Mem Usage"))
                logs.add(TerminalLog("========================= ====== ================ ============"))
                logs.add(TerminalLog("System                         4 Services                  44 K"))
                logs.add(TerminalLog("windesk-host.exe            1048 Console              142,320 K"))
                logs.add(TerminalLog("dwm.exe                     1124 Console               68,412 K"))
                logs.add(TerminalLog("explorer.exe                2480 Console              184,950 K"))
                uiState.activeWindows.forEachIndexed { i, win ->
                    logs.add(TerminalLog("${win.appId.name.lowercase()}.exe".padEnd(25) + " ${(3000 + i * 100).toString().padEnd(6)} Console              ${(45000 + i * 8000)} K"))
                }
            }
            "ver" -> {
                logs.add(TerminalLog("WinDesk Cloud PC OS [Version 11.0.22631.3007]"))
            }
            "cls", "clear" -> {
                logs.clear()
            }
            "echo" -> {
                val msg = parts.drop(1).joinToString(" ")
                logs.add(TerminalLog(msg))
            }
            "" -> {}
            else -> {
                logs.add(TerminalLog("'$cmd' is not recognized as an internal or external command. Type 'help' for available commands.", Color(0xFFEF4444)))
            }
        }

        logs.add(TerminalLog(""))
        currentInput = ""
        coroutineScope.launch {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0C0C))
            .padding(10.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(logs) { log ->
                Text(
                    text = log.text,
                    color = log.color,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 16.sp
                )
            }
        }

        // Input Line
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PS C:\\Users\\Administrator> ",
                color = Color(0xFF38BDF8),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            BasicTextField(
                value = currentInput,
                onValueChange = { currentInput = it },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                ),
                cursorBrush = SolidColor(Color.White),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { executeCommand(currentInput) }),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

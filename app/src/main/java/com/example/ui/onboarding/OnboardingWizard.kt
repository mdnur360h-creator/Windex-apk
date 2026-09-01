package com.example.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DesktopThemeType
import com.example.model.TargetVmRam
import com.example.model.TargetVmStorage
import com.example.viewmodel.DesktopViewModel

@Composable
fun OnboardingWizard(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    var step by remember { mutableStateOf(0) }
    val uiState by viewModel.uiState.collectAsState()
    val config = uiState.cloudPcConfig

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .width(540.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
            color = Color(0xFF0F172A),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Step Indicator Pills
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (i in 0..2) {
                        Box(
                            modifier = Modifier
                                .size(width = if (i == step) 28.dp else 12.dp, height = 6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (i == step) theme.primaryAccent else Color(0xFF334155))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (step) {
                    0 -> {
                        // Step 1: Welcome to WinDesk Cloud PC
                        Icon(Icons.Default.DesktopWindows, contentDescription = null, tint = theme.primaryAccent, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Welcome to WinDesk Cloud PC", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "A full-featured landscape Windows 11 desktop experience for your Android tablet or phone. Experience multi-window management, snap layouts, Start Menu, taskbar, and cloud PC virtualization.",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 17.sp
                        )
                    }
                    1 -> {
                        // Step 2: Virtual Machine Resource Configuration
                        Icon(Icons.Default.Memory, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Configure Target VM Resources", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Allocate high-performance virtual memory and storage for your Cloud PC instance:", color = Color(0xFF94A3B8), fontSize = 11.sp, textAlign = TextAlign.Center)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TargetVmRam.values().forEach { r ->
                                FilterChip(
                                    selected = config.targetRam == r,
                                    onClick = { viewModel.updateCloudPcConfig(config.copy(targetRam = r)) },
                                    label = { Text("${r.gigaBytes} GB RAM", fontSize = 11.sp) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TargetVmStorage.values().forEach { s ->
                                FilterChip(
                                    selected = config.targetStorage == s,
                                    onClick = { viewModel.updateCloudPcConfig(config.copy(targetStorage = s)) },
                                    label = { Text("${s.gigaBytes} GB NVMe", fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                    2 -> {
                        // Step 3: Architecture & Security Transparency
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Hardware Transparency & Readiness", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "• WinDesk runs securely on Android with no root required.\n" +
                                   "• Local apps run in instant client desktop mode.\n" +
                                   "• Heavy Windows x86 apps execute in your dedicated 12GB Cloud PC session.\n" +
                                   "• Default Desktop PIN is 1234.",
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (step > 0) {
                        TextButton(onClick = { step-- }) {
                            Text("Back", color = Color(0xFF94A3B8))
                        }
                    } else {
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    Button(
                        onClick = {
                            if (step < 2) {
                                step++
                            } else {
                                viewModel.completeOnboarding()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = theme.primaryAccent),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (step == 2) "Start WinDesk Desktop" else "Next", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

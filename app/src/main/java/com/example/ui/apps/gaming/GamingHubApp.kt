package com.example.ui.apps.gaming

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DesktopThemeType
import com.example.model.HardwareTelemetry
import com.example.model.PcGameItem
import com.example.viewmodel.DesktopViewModel

@Composable
fun GamingHubApp(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    val uiState by viewModel.uiState.collectAsState()
    val telemetry = uiState.telemetry

    var showOnScreenGamepad by remember { mutableStateOf(false) }
    var selectedResolution by remember { mutableStateOf("1080p 60 FPS") }
    var activeGamePlaying by remember { mutableStateOf<PcGameItem?>(null) }

    val gameLibrary = remember {
        listOf(
            PcGameItem("g1", "Cyberpunk 2077: Phantom Liberty", "Action RPG", 0xFF00F0FF, 60, 8, 70),
            PcGameItem("g2", "Forza Horizon 5", "Racing Simulator", 0xFFFF0055, 60, 6, 110),
            PcGameItem("g3", "Halo Infinite", "FPS Multiplayer", 0xFF107C10, 120, 6, 50),
            PcGameItem("g4", "Counter-Strike 2", "Competitive Shooter", 0xFFF59E0B, 120, 4, 35),
            PcGameItem("g5", "Elden Ring: Shadow of Erdtree", "Action Souls", 0xFF8B5CF6, 60, 6, 60),
            PcGameItem("g6", "Microsoft Flight Simulator", "Simulation", 0xFF0284C7, 60, 12, 150)
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Gaming Mode Top Bar & HUD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF090D16))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(
                    color = Color(0xFF00F0FF).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00F0FF))
                ) {
                    Text(
                        "WIN-DESK GAMING RIG (CLOUD GPU)",
                        color = Color(0xFF00F0FF),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text("LATENCY: ${telemetry.streamLatencyMs}ms", color = Color(0xFF4ADE80), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("FPS: 60 (VSYNC ON)", color = Color(0xFF38BDF8), fontSize = 11.sp)
                Text("BITRATE: 24 Mbps HEVC", color = Color(0xFFA78BFA), fontSize = 11.sp)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Toggle Virtual Touch Gamepad Overlay
                Button(
                    onClick = { showOnScreenGamepad = !showOnScreenGamepad },
                    colors = ButtonDefaults.buttonColors(containerColor = if (showOnScreenGamepad) Color(0xFF00F0FF) else Color(0xFF1E293B)),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(Icons.Default.Gamepad, contentDescription = null, tint = if (showOnScreenGamepad) Color.Black else Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (showOnScreenGamepad) "Hide Gamepad" else "Touch Controller",
                        fontSize = 10.sp,
                        color = if (showOnScreenGamepad) Color.Black else Color.White
                    )
                }
            }
        }

        // Main Gaming Body
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF06090F))
                .padding(12.dp)
        ) {
            if (activeGamePlaying != null) {
                // Active Game Stream View
                val game = activeGamePlaying!!
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Now Playing: ${game.title}",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { activeGamePlaying = null },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("Exit Game Session", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Simulated 60 FPS Game Stream Viewport
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.radialGradient(listOf(Color(game.bannerColor).copy(alpha = 0.3f), Color(0xFF020408))))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.SportsEsports, contentDescription = null, tint = Color(game.bannerColor), modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Live Cloud Game Stream Active", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Rendered on Cloud RTX GPU • Streamed to Android Display @ 60 FPS", color = Color(0xFF94A3B8), fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Controller / Keyboard & Mouse Inputs Routed Directly to Windows VM", color = Color(0xFF4ADE80), fontSize = 11.sp)
                        }

                        // On-Screen Virtual Gamepad Touch Overlay
                        if (showOnScreenGamepad) {
                            VirtualGamepadOverlay(modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            } else {
                // Game Library Launcher
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Windows PC Game Launcher (Cloud / VM Passthrough)",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Real Windows games run in high-performance cloud GPU environment and stream to your screen.",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 200.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(gameLibrary) { game ->
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { activeGamePlaying = game },
                                color = Color(0xFF0F172A),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(70.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Brush.linearGradient(listOf(Color(game.bannerColor).copy(alpha = 0.5f), Color(0xFF0F172A)))),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.SportsEsports, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(game.title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                    Text(game.genre, color = Color(0xFF94A3B8), fontSize = 10.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("${game.targetFps} FPS Target", color = Color(0xFF4ADE80), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                        Text("${game.storageSizeGb} GB NVMe", color = Color(0xFF38BDF8), fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VirtualGamepadOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.4f))
            .padding(16.dp)
    ) {
        // D-Pad Left
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(110.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.TopCenter)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowDropUp, contentDescription = "Up", tint = Color.White)
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.BottomCenter)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Down", tint = Color.White)
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterStart)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowLeft, contentDescription = "Left", tint = Color.White)
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterEnd)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowRight, contentDescription = "Right", tint = Color.White)
            }
        }

        // ABXY Buttons Right
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(110.dp)
        ) {
            GamepadButton("Y", Color(0xFFFBBF24), Modifier.align(Alignment.TopCenter))
            GamepadButton("A", Color(0xFF22C55E), Modifier.align(Alignment.BottomCenter))
            GamepadButton("X", Color(0xFF38BDF8), Modifier.align(Alignment.CenterStart))
            GamepadButton("B", Color(0xFFEF4444), Modifier.align(Alignment.CenterEnd))
        }
    }
}

@Composable
private fun GamepadButton(label: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.6f))
            .border(1.dp, color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

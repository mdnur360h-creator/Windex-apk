package com.example.ui.apps.mediaplayer

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DesktopThemeType

data class MediaTrack(val title: String, val artist: String, val duration: String)

@Composable
fun MediaPlayerApp(
    theme: DesktopThemeType
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentTrackIndex by remember { mutableStateOf(0) }
    var progress by remember { mutableStateOf(0.35f) }

    val playlist = listOf(
        MediaTrack("Midnight Synthwave Dreams", "WinDesk Ambient Ensemble", "3:45"),
        MediaTrack("Cybernetic Horizon", "Cloud PC Audio Labs", "4:12"),
        MediaTrack("Aero Nostalgia (Windows 7 Tribute)", "Glass Orchestra", "3:18")
    )

    val currentTrack = playlist[currentTrackIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (theme.isDark) Color(0xFF141414) else Color(0xFFF1F5F9))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Album Artwork
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(
                        listOf(theme.primaryAccent, theme.secondaryAccent)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(64.dp))
        }

        // Track Info
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(currentTrack.title, color = theme.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(currentTrack.artist, color = theme.textSecondary, fontSize = 12.sp)
        }

        // Progress Bar & Scrubber
        Column(modifier = Modifier.fillMaxWidth()) {
            Slider(
                value = progress,
                onValueChange = { progress = it },
                colors = SliderDefaults.colors(thumbColor = theme.primaryAccent, activeTrackColor = theme.primaryAccent)
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("1:18", color = theme.textSecondary, fontSize = 10.sp)
                Text(currentTrack.duration, color = theme.textSecondary, fontSize = 10.sp)
            }
        }

        // Playback Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                currentTrackIndex = if (currentTrackIndex > 0) currentTrackIndex - 1 else playlist.size - 1
            }) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = theme.textPrimary, modifier = Modifier.size(28.dp))
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(theme.primaryAccent),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { isPlaying = !isPlaying }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            IconButton(onClick = {
                currentTrackIndex = (currentTrackIndex + 1) % playlist.size
            }) {
                Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = theme.textPrimary, modifier = Modifier.size(28.dp))
            }
        }
    }
}

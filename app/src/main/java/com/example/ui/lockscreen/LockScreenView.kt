package com.example.ui.lockscreen

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.DesktopThemeType
import com.example.viewmodel.DesktopViewModel

@Composable
fun LockScreenView(
    viewModel: DesktopViewModel,
    theme: DesktopThemeType
) {
    val uiState by viewModel.uiState.collectAsState()
    var pinText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        // High-res desktop wallpaper
        Image(
            painter = painterResource(id = R.drawable.bg_wallpaper_bloom),
            contentDescription = "Lock Screen Wallpaper",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Acrylic Frosted Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Big Windows Clock & Date
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = uiState.systemTime,
                    color = Color.White,
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Light,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = androidx.compose.ui.graphics.Shadow(color = Color.Black.copy(alpha = 0.8f), blurRadius = 8f)
                    )
                )
                Text(
                    text = uiState.systemDate,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = androidx.compose.ui.graphics.Shadow(color = Color.Black.copy(alpha = 0.8f), blurRadius = 6f)
                    )
                )
            }

            // User Avatar & PIN Input Box
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E293B).copy(alpha = 0.7f))
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 28.dp, vertical = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(theme.primaryAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = "User", tint = Color.White, modifier = Modifier.size(36.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("User-PC", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("WinDesk Cloud PC Session", color = Color(0xFF94A3B8), fontSize = 11.sp)

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = pinText,
                        onValueChange = {
                            pinText = it
                            errorMessage = null
                        },
                        placeholder = { Text("Enter PIN (1234)", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f)) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            if (viewModel.unlockDesktop(pinText)) {
                                errorMessage = null
                            } else {
                                errorMessage = "Incorrect PIN. (Default: 1234)"
                            }
                        }),
                        modifier = Modifier.width(180.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = theme.primaryAccent,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                        )
                    )

                    IconButton(
                        onClick = {
                            if (viewModel.unlockDesktop(pinText)) {
                                errorMessage = null
                            } else {
                                errorMessage = "Incorrect PIN. (Default: 1234)"
                            }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(theme.primaryAccent)
                    ) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Unlock", tint = Color.White)
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(errorMessage!!, color = Color(0xFFEF4444), fontSize = 11.sp)
                }
            }

            // Bottom Quick Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CloudDone, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("12 GB Target VM Standby • 256 GB NVMe", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Wifi, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Icon(Icons.Default.BatteryFull, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

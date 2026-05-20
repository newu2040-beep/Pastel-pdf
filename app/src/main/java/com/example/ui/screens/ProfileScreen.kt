package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.ProfileEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profile: ProfileEntity?,
    onUpdateProfile: (ProfileEntity) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf(profile?.username ?: "User") }
    var isDarkMode by remember { mutableStateOf(profile?.isDarkMode ?: false) }
    var notificationsEnabled by remember { mutableStateOf(profile?.notificationsEnabled ?: true) }
    var selectedColor by remember { mutableStateOf(profile?.primaryColorHex ?: "#FFB3BA") }

    val themeColors = listOf("#FFB3BA", "#BAE1FF", "#B5EAD7", "#xFFFFDFBA", "#E2B6FF")

    LaunchedEffect(profile) {
        if (profile != null) {
            username = profile.username
            isDarkMode = profile.isDarkMode
            notificationsEnabled = profile.notificationsEnabled
            selectedColor = profile.primaryColorHex
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Check out my Pastel PDF profile!\nTheme color: $selectedColor\nDark Mode: $isDarkMode")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { 
                    username = it
                    onUpdateProfile(ProfileEntity(username = it, isDarkMode = isDarkMode, notificationsEnabled = notificationsEnabled, primaryColorHex = selectedColor))
                },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text("Theme DetailColor", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                themeColors.forEach { colorHex ->
                    val actualColorHex = colorHex.replace("#FFFFDFBA", "#FFDFBA") // fix typo length
                    val isSelected = selectedColor == actualColorHex
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(android.graphics.Color.parseColor(actualColorHex)))
                            .border(
                                width = if (isSelected) 3.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                selectedColor = actualColorHex
                                onUpdateProfile(ProfileEntity(username = username, isDarkMode = isDarkMode, notificationsEnabled = notificationsEnabled, primaryColorHex = actualColorHex))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.Black)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Preferences", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dark Mode", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = {
                        isDarkMode = it
                        onUpdateProfile(ProfileEntity(username = username, isDarkMode = it, notificationsEnabled = notificationsEnabled, primaryColorHex = selectedColor))
                    }
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Notifications", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = {
                        notificationsEnabled = it
                        onUpdateProfile(ProfileEntity(username = username, isDarkMode = isDarkMode, notificationsEnabled = it, primaryColorHex = selectedColor))
                    }
                )
            }

            var cloudSyncEnabled by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Cloud Sync", style = MaterialTheme.typography.bodyLarge)
                    Text("Sync documents to cloud storage", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
                Switch(
                    checked = cloudSyncEnabled,
                    onCheckedChange = { cloudSyncEnabled = it }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                 Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                     Icon(imageVector = Icons.Default.Check, contentDescription = "Offline Mode", tint = MaterialTheme.colorScheme.onTertiaryContainer)
                     Spacer(modifier = Modifier.width(12.dp))
                     Text("Offline Support Enabled.\nTools can be used without an internet connection.", color = MaterialTheme.colorScheme.onTertiaryContainer)
                 }
            }
        }
    }
}

package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.AppDatabase
import com.example.data.Repository
import com.example.ui.MainViewModel
import com.example.ui.MainViewModelFactory
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ToolScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    private val repository by lazy { Repository(database) }
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val profile by viewModel.profile.collectAsStateWithLifecycle()
            
            MyApplicationTheme(
                darkTheme = profile?.isDarkMode ?: false,
                dynamicColor = false,
                primaryColorHex = profile?.primaryColorHex ?: "#FFB3BA"
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(viewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val activities by viewModel.activities.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(
                activities = activities,
                onNavigateToTool = { tool -> navController.navigate("tool/$tool") },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }
        composable("profile") {
            ProfileScreen(
                profile = profile,
                onUpdateProfile = { viewModel.updateProfile(it) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        // Tools endpoints
        composable("tool/convert") {
            ToolScreen(title = "Convert Image to PDF", toolType = "convert", onNavigateBack = { navController.popBackStack() }, onLogActivity = { file, action -> viewModel.addActivity(file, action) })
        }
        composable("tool/sign") {
            ToolScreen(title = "Sign PDF", toolType = "sign", onNavigateBack = { navController.popBackStack() }, onLogActivity = { file, action -> viewModel.addActivity(file, action) })
        }
        composable("tool/view") {
            ToolScreen(title = "View PDF", toolType = "view", onNavigateBack = { navController.popBackStack() }, onLogActivity = { file, action -> viewModel.addActivity(file, action) })
        }
        composable("tool/batch") {
            ToolScreen(title = "Batch Process", toolType = "batch", onNavigateBack = { navController.popBackStack() }, onLogActivity = { file, action -> viewModel.addActivity(file, action) })
        }
    }
}

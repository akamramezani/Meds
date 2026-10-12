package com.example.medprotocols.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medprotocols.MedProtocolsApp
import com.example.medprotocols.ui.screens.calculator.DoseCalculatorScreen
import com.example.medprotocols.ui.screens.category.CategoryScreen
import com.example.medprotocols.ui.screens.detail.ProtocolDetailScreen
import com.example.medprotocols.ui.screens.home.HomeScreen
import com.example.medprotocols.ui.screens.settings.SettingsScreen

sealed class Destination(val route: String) {
    data object Home : Destination("home")
    data object Category : Destination("category/{categoryId}") {
        fun createRoute(categoryId: Long) = "category/$categoryId"
    }
    data object ProtocolDetail : Destination("protocol/{protocolId}") {
        fun createRoute(protocolId: Long) = "protocol/$protocolId"
    }
    data object Calculator : Destination("calculator")
    data object Settings : Destination("settings")
}

@Composable
fun AppNavHost(app: MedProtocolsApp) {
    val navController = rememberNavController()
    val repository = app.container.repository

    // Content is Persian regardless of the device's system language, so the
    // reading direction is fixed here rather than following system locale.
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            bottomBar = { AppBottomBar(navController) }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Destination.Home.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(Destination.Home.route) {
                    HomeScreen(
                        repository = repository,
                        onCategoryClick = { navController.navigate(Destination.Category.createRoute(it)) },
                        onProtocolClick = { navController.navigate(Destination.ProtocolDetail.createRoute(it)) }
                    )
                }
                composable(
                    route = Destination.Category.route,
                    arguments = listOf(navArgument("categoryId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: 0L
                    CategoryScreen(
                        categoryId = categoryId,
                        repository = repository,
                        onProtocolClick = { navController.navigate(Destination.ProtocolDetail.createRoute(it)) },
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Destination.ProtocolDetail.route,
                    arguments = listOf(navArgument("protocolId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val protocolId = backStackEntry.arguments?.getLong("protocolId") ?: 0L
                    ProtocolDetailScreen(
                        protocolId = protocolId,
                        repository = repository,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(Destination.Calculator.route) {
                    DoseCalculatorScreen()
                }
                composable(Destination.Settings.route) {
                    SettingsScreen(app = app)
                }
            }
        }
    }
}

@Composable
private fun AppBottomBar(navController: NavHostController) {
    val items = listOf(
        Triple(Destination.Home.route, Icons.Filled.Home, "Home"),
        Triple(Destination.Calculator.route, Icons.Filled.Calculate, "Calculator"),
        Triple(Destination.Settings.route, Icons.Filled.Settings, "Settings")
    )
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        items.forEach { (route, icon, label) ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}

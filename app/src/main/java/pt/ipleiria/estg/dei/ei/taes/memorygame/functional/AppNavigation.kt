package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import BrainViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.DashboardScreen
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.GameScreen
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.HistoryScreen
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.LoginScreen
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.ProfileScreen
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.ScoreboardScreen

@Composable
fun AppNavigation(loggedIn: Boolean, notificationsViewModel: NotificationsViewModel) {
    val navController = rememberNavController()
    val brainViewModel: BrainViewModel = viewModel()
    UserData.brainViewModel = brainViewModel
    val startDestination = if (loggedIn) {
        "dashboard" // Navigate to the dashboard if token exists
    } else {
        "login" // Navigate to login if no token exists
    }
    NavHost(navController = navController, startDestination = startDestination ) {
        composable("dashboard")
        {
            DashboardScreen(navController, brainViewModel, notificationsViewModel)
        }
        composable(
            "game/{cardsRow}/{cardsColumn}",
            arguments = listOf(
                navArgument("cardsRow") { type = NavType.IntType },
                navArgument("cardsColumn") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // Get the arguments from the backStackEntry
            val cardsRow = backStackEntry.arguments?.getInt("cardsRow") ?: 3 // Default value if no argument
            val cardsColumn = backStackEntry.arguments?.getInt("cardsColumn") ?: 4 // Default value if no argument
            GameScreen(cardsRow, cardsColumn, brainViewModel, navController,notificationsViewModel)
        }
        composable("scoreboard"){
            ScoreboardScreen(navController, brainViewModel,notificationsViewModel)
        }
        composable("profile"){
            ProfileScreen(navController, brainViewModel,notificationsViewModel)
        }
        composable("history"){
            HistoryScreen(navController, brainViewModel,notificationsViewModel)
        }
        composable("login"){
            LoginScreen(  navController, Modifier.fillMaxSize(), true, brainViewModel,notificationsViewModel)
        }

    }

}

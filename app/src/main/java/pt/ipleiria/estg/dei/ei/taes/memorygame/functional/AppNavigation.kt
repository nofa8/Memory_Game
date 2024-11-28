package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import BrainViewModel
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.DashboardScreen
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.GameScreen
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.HistoryScreen
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.ProfileScreen
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.ScoreboardScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val brainViewModel: BrainViewModel = viewModel()
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard")
        {
            DashboardScreen(navController, brainViewModel)
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
            GameScreen(cardsRow, cardsColumn, brainViewModel, navController)
        }
        composable("scoreboard"){
            ScoreboardScreen(navController, brainViewModel)
        }
        composable("profile"){
            ProfileScreen(navController, brainViewModel)
        }
        composable("history"){
            HistoryScreen(navController, brainViewModel)
        }
    }

}

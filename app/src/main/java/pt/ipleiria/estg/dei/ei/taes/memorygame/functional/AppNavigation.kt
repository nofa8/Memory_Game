package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.DashboardScreen
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.GameScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") { DashboardScreen(navController) }
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
            GameScreen(cardsRow, cardsColumn)
        }
    }

}
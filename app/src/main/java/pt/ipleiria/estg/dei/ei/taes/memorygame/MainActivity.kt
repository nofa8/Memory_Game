package pt.ipleiria.estg.dei.ei.taes.memorygame
import BrainViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.AppInitializer
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.AppNavigation
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.DashboardScreen
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.MemoryGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        AppInitializer.initializeApp(this) { isAuthenticated ->
            setContent {
                MemoryGameTheme {
                    AppNavigation(isAuthenticated) // Pass authentication status to navigation
                }
            }
        }

    }
}

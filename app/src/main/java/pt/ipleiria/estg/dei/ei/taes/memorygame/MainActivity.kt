package pt.ipleiria.estg.dei.ei.taes.memorygame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.AppInitializer
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.AppNavigation
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.AppContextHolder
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.TokenRefresher
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.WebSocketManager
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.MemoryGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppContextHolder.initialize(this)
        AppInitializer.initializeApp(this) { isAuthenticated ->
            setContent {
                MemoryGameTheme {
                    println(isAuthenticated)
                    AppNavigation(isAuthenticated)
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        TokenRefresher.stop()
        WebSocketManager.disconnect()

    }
}

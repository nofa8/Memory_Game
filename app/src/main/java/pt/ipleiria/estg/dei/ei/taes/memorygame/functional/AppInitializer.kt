package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API

class AppInitializer {
    companion object {
        fun initializeApp(context: Context, onInitializationComplete: (isAuthenticated: Boolean) -> Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                // Initialize API instance
                val api = API.getInstance(context)

                // Fetch board data asynchronously
                val boardsFetched = BoardData.fetchBoards()

                // Validate the token by fetching user data
                val user = api.fetchUserData()
                val isAuthenticated = user != null

                // Update user data if token is valid
                if (isAuthenticated) {
                    UserData.user = user
                } else {
                    // Clear invalid token and user data
                    api.saveToken("")
                    UserData.user = null
                }

                // Notify when initialization is complete
                withContext(Dispatchers.Main) {
                    onInitializationComplete(isAuthenticated)
                }
            }
        }
    }
}

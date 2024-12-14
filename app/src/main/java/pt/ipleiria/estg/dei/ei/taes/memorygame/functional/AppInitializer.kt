package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.TokenRefresher
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.NotificationHelper
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.WebSocketManager


class AppInitializer {
    companion object {
        private const val REQUEST_CODE_NOTIFICATION_PERMISSION = 1001
        var notificationsViewModel = NotificationsViewModel()

        fun initializeApp(context: Context, onInitializationComplete: (isAuthenticated: Boolean) -> Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                // Initialize boards and API
                val boardsLoaded = BoardData.fetchBoards()
                val api = API.getInstance(context)
                api.loadToken()
                val isAuthenticated = if (API.token.isNotEmpty()) {
                    val user = api.fetchUserData()
                    user?.let {
                        UserData.updateUser(it)
                        TokenRefresher.start(context) { errorMessage ->
                            Log.e("TokenRefresher", "Error refreshing token: $errorMessage")
                            Toast.makeText(context, "Session expired. Please log in again.", Toast.LENGTH_LONG).show()
                            onInitializationComplete(false)
                        }
                        true
                    } == true
                } else {
                    api.clearToken()
                    false
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED) {
                        if (context is Activity) {
                            ActivityCompat.requestPermissions(
                                context,
                                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                REQUEST_CODE_NOTIFICATION_PERMISSION
                            )
                        } else {
                            Log.e("Permission", "Context is not an Activity. Cannot request permission.")
                        }
                    } else {
                        NotificationHelper.notificationView = notificationsViewModel

                        WebSocketManager.connect()
                    }
                } else {
                    NotificationHelper.notificationView = notificationsViewModel

                    WebSocketManager.connect()
                }

                withContext(Dispatchers.Main) {
                    onInitializationComplete(isAuthenticated)
                }
            }
        }

        fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>, grantResults: IntArray, context: Context
        ) {
            if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission", "Notification permission granted.")
                    NotificationHelper.notificationView = notificationsViewModel
                    WebSocketManager.connect()
                } else {
                    Log.d("Permission", "Notification permission denied.")
                    Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


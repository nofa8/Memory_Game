import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import org.json.JSONException
import org.json.JSONObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.TokenRefresher
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.BoardData
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.UserData
import kotlin.random.Random
import pt.ipleiria.estg.dei.ei.taes.memorygame.R

class AppInitializer {
    companion object {
        private const val REQUEST_CODE_NOTIFICATION_PERMISSION = 1001

        fun initializeApp(context: Context, onInitializationComplete: (isAuthenticated: Boolean) -> Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                val boardsLoaded = BoardData.fetchBoards()

                val api = API.getInstance(context)
                api.loadToken()

                val isAuthenticated = if (API.token.isNotEmpty()) {
                    val user = api.fetchUserData()
                    user?.let {
                        UserData.user = it
                        TokenRefresher.start(context) { errorMessage ->
                            println(errorMessage)
                            Toast.makeText(context, "Session expired. Please log in again.", Toast.LENGTH_LONG).show()
                            onInitializationComplete(false)
                        }
                        true
                    } == true
                } else {
                    api.clearToken()
                    false
                }

                // Request permission to post notifications (for Android 13+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED) {
                        // Request permission
                        ActivityCompat.requestPermissions(
                            context as Activity,  // Cast context to Activity
                            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                            REQUEST_CODE_NOTIFICATION_PERMISSION
                        )
                    } else {
                        // Permission already granted, setup WebSocket
                        setupWebSocket(context)
                    }
                } else {
                    // No need to request permission for Android < 13
                    setupWebSocket(context)
                }

                withContext(Dispatchers.Main) {
                    onInitializationComplete(isAuthenticated)
                }
            }
        }

        private fun setupWebSocket(context: Context) {
            // Pusher configuration
            val options = PusherOptions().apply {
                setCluster("mt1") // Match Laravel's PUSHER_APP_CLUSTER
                setHost("10.0.0.2") // Match Laravel's PUSHER_HOST
                setWsPort(8080) // Match Laravel's PUSHER_PORT
                setUseTLS(false) // Use HTTP for local development
                isEncrypted = false // Disable encryption for local setup
            }

            val pusher = Pusher("d3e370f24b73926cb383", options) // Match Laravel's PUSHER_APP_KEY

            // Establish connection and handle connection events
            pusher.connect(object : ConnectionEventListener {
                override fun onConnectionStateChange(change: ConnectionStateChange) {
                    Log.i("Pusher", "State changed from ${change.previousState} to ${change.currentState}")
                }

                override fun onError(message: String?, code: String?, e: Exception?) {
                    Log.e("Pusher", "Error: code($code), message($message), exception($e)")
                }
            }, ConnectionState.ALL)

            // Subscribe to the "top-scores" channel
            val topScoresChannel = pusher.subscribe("top-scores")

            topScoresChannel.bind("GlobalBroadcastTop") { event ->
                Log.i("Pusher", "Received global broadcast: ${event.data}")

                // Parse the event data as JSON safely
                try {
                    val eventData = JSONObject(event.data)
                    val playerName = eventData.getJSONObject("player").getString("name")
                    val board = eventData.getString("board")
                    val type = eventData.getString("type")
                    val custom = eventData.getInt("custom")

                    // Show a notification on the main thread
                    Handler(Looper.getMainLooper()).post {
                        showNotification(
                            context,
                            "New Top Score!",
                            "Player $playerName achieved a new top score on board $board ($type: $custom)"
                        )
                    }
                } catch (e: JSONException) {
                    Log.e("Pusher", "Error parsing event data: ${e.message}")
                }
            }
        }

        @SuppressLint("MissingPermission")
        private fun showNotification(context: Context, title: String, message: String) {
            val notificationBuilder = NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true) // Dismiss the notification when tapped

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(Random.nextInt(), notificationBuilder.build())
        }

        // Handle the result of permission request
        fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>, grantResults: IntArray, context: Context
        ) {
            if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with WebSocket setup
                    setupWebSocket(context)
                } else {
                    // Permission denied, show a message or take appropriate action
                    Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

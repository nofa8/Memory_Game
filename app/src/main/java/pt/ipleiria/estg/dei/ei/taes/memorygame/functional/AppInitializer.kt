import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONException
import org.json.JSONObject
import pt.ipleiria.estg.dei.ei.taes.memorygame.R
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.BoardData
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.Notification
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.NotificationsViewModel
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.UserData
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.TokenRefresher
import java.util.concurrent.TimeUnit
import kotlin.random.Random

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
                        setupWebSocket(context, notificationsViewModel)
                    }
                } else {
                    setupWebSocket(context, notificationsViewModel)
                }

                withContext(Dispatchers.Main) {
                    onInitializationComplete(isAuthenticated)
                }
            }
        }

        private fun setupWebSocket(context: Context, notificationsViewModel: NotificationsViewModel) {
            val websocketUrl = "ws://10.0.2.2:8080"
            val client = OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build()

            val request = Request.Builder().url(websocketUrl).build()

            val webSocketListener = object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    Log.i("WebSocket", "Connected to $websocketUrl")
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    handleWebSocketMessage(text, context)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Log.e("WebSocket", "Connection failed: ${t.message}")
                    Handler(Looper.getMainLooper()).postDelayed({
                        setupWebSocket(context, notificationsViewModel)
                    }, 5000)
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    Log.i("WebSocket", "Connection closed: $code $reason")
                }
            }

            val webSocket = client.newWebSocket(request, webSocketListener)
            client.dispatcher.executorService.shutdown()
        }

        private fun handleWebSocketMessage(text: String, context: Context) {
            try {
                val eventData = JSONObject(text)
                val event = eventData.optString("event", null)
                val data = eventData.optJSONObject("data")
                if (event == null || data == null) {
                    Log.e("WebSocket", "Malformed JSON data received: $text")
                    return
                }

                if (event == "GlobalBroadcastTop") {
                    val playerName = data.getJSONObject("player").getString("name")
                    val board = data.getString("board")
                    val type = data.getString("type")
                    val custom = data.getInt("custom")

                    Handler(Looper.getMainLooper()).post {
                        val newNotification = Notification(
                            title = "New Top Score!",
                            message = "Player $playerName achieved a new top score on board $board ($type: $custom)",
                            timestamp = System.currentTimeMillis().toString(),
                            isRead = false
                        )

                        notificationsViewModel.notifications.add(0, newNotification)
                        showNotification(
                            context,
                            "New Top Score!",
                            "Player $playerName achieved a new top score on board $board ($type: $custom)"
                        )
                    }
                }
            } catch (e: JSONException) {
                Log.e("WebSocket", "Error parsing message: ${e.message}")
            }
        }

        @SuppressLint("MissingPermission")
        private fun showNotification(context: Context, title: String, message: String) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "default",
                    "Default Channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager?.createNotificationChannel(channel)
            }

            val notificationBuilder = NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(Random.nextInt(), notificationBuilder.build())
        }

        fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>, grantResults: IntArray, context: Context
        ) {
            if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission", "Notification permission granted.")
                    setupWebSocket(context, notificationsViewModel)
                } else {
                    Log.d("Permission", "Notification permission denied.")
                    Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

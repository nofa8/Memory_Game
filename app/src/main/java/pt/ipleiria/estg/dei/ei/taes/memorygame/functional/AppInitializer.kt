package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

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
import org.json.JSONException
import org.json.JSONObject
import pt.ipleiria.estg.dei.ei.taes.memorygame.R
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
                        WebSocketConnectionManager.connect(context, notificationsViewModel)
                    }
                } else {
                    WebSocketConnectionManager.connect(context, notificationsViewModel)
                }

                withContext(Dispatchers.Main) {
                    onInitializationComplete(isAuthenticated)
                }
            }
        }

        @SuppressLint("MissingPermission")
        fun showNotification(context: Context, title: String, message: String) {
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
                    WebSocketConnectionManager.connect(context, notificationsViewModel)
                } else {
                    Log.d("Permission", "Notification permission denied.")
                    Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

object WebSocketConnectionManager {

    private const val TAG = "WebSocketManager"
//    private const val WEB_SOCKET_URL = "ws://10.0.2.2:8080"
    private const val WEB_SOCKET_URL = "ws://ws-dad-group-9-172.22.21.101.sslip.io"
    private val client: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()
    public var webSocket: okhttp3.WebSocket? = null

    fun connect(context: Context, notificationsViewModel: NotificationsViewModel) {
        val request = Request.Builder().url(WEB_SOCKET_URL).build()

        webSocket = client.newWebSocket(request, object : okhttp3.WebSocketListener() {
            override fun onOpen(webSocket: okhttp3.WebSocket, response: okhttp3.Response) {
                Log.i(TAG, "WebSocket connected to $WEB_SOCKET_URL")
            }

            override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
                handleWebSocketMessage(text, context, notificationsViewModel)
            }

            override fun onFailure(webSocket: okhttp3.WebSocket, t: Throwable, response: okhttp3.Response?) {
                Log.e(TAG, "WebSocket connection failed: ${t.message}")
                Handler(Looper.getMainLooper()).postDelayed({
                    connect(context, notificationsViewModel)
                }, 5000)
            }

            override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
                Log.i(TAG, "WebSocket closed: $code $reason")
            }
        })
    }

    private fun handleWebSocketMessage(text: String, context: Context, notificationsViewModel: NotificationsViewModel) {
        try {
            val eventData = JSONObject(text)
            val event = eventData.optString("event", null)
            val data = eventData.optJSONObject("data")

            if (event == null || data == null) {
                Log.e(TAG, "Malformed JSON data received: $text")
                return
            }

            when (event) {
                "loginTAES" -> {
                    val user = data.optJSONObject("user")
                    if (user != null) {
                        val userId = user.optString("id", null)
                        Log.i(TAG, "User logged in with ID: $userId")
                    }
                }

                "logoutTAES" -> {
                    val user = data.optJSONObject("user")
                    if (user != null) {
                        val userId = user.optString("id", null)
                        Log.i(TAG, "User logged out with ID: $userId")
                    }
                }

                "transactionTAES" -> {
                    val user = data.optJSONObject("user")
                    val message = data.optString("message")
                    if (user != null && message != null) {
                        Log.i(TAG, "Transaction received for user: ${user.optString("id")}, message: $message")
                        AppInitializer.showNotification(context, "Transaction", message)
                    }
                }

                "globalRecord" -> {
                    val message = data.optString("message")
                    if (message != null) {
                        Log.i(TAG, "Global broadcast message: $message")
                        Handler(Looper.getMainLooper()).post {
                            AppInitializer.showNotification(context, "Global Broadcast", message)
                        }
                    }
                }

                "personalRecord" -> {
                    val message = data.optString("message")
                    if (message != null) {
                        Log.i(TAG, "Personal message: $message")
                        Handler(Looper.getMainLooper()).post {
                            AppInitializer.showNotification(context, "Personal Message", message)
                        }
                    }
                }

                else -> {
                    Log.w(TAG, "Unhandled WebSocket event: $event")
                }
            }
        } catch (e: JSONException) {
            Log.e(TAG, "Error parsing WebSocket message: ${e.message}")
        }
    }


    fun disconnect() {
        webSocket?.close(1000, "Connection closed by client")
        client.dispatcher.executorService.shutdown()
    }
}

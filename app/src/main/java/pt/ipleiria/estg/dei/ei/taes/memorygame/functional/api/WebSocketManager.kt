package pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api

import android.util.Log
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.User
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.UserData
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.Notification
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.NotificationsViewModel
import java.net.URISyntaxException


data class ResponseMessage(
    val message: String
)

data class ResponseTransaction(
    val user : User,
    val message: String
)

object WebSocketManager {
//    private const val SOCKET_URL = "http://10.0.2.2:8080"
    private const val SOCKET_URL = "ws://ws-dad-group-9-172.22.21.101.sslip.io"
    private const val TAG = "WebSocketManager"
    private var socket: Socket? = null
    private var login = false
    private var disconnectedUser: User? = null // Store the user on disconnect
    private val  gson = Gson()
    // Initialize the socket connection
    fun initializeSocket() {
        if (socket == null) {
            try {
                socket = IO.socket(SOCKET_URL)
                socket?.on(Socket.EVENT_CONNECT) {
                    Log.i(TAG, "Connected to WebSocket server.")
                    if (UserData.user.value != null) {

                        emitLogin(UserData.user.value!!)
                    }
                }
                socket?.on(Socket.EVENT_DISCONNECT) {
                    onSocketDisconnected()
                }
                socket?.on("transaction") { args ->
                    try {
                        val jsonObject = args[0] as JSONObject
                        // Parse the JSON into ResponseTransaction
                        val responseTransaction = gson.fromJson(jsonObject.toString(), ResponseTransaction::class.java)

                        // Access the user and message from the ResponseTransaction object
                        val user = responseTransaction.user
                        val message = responseTransaction.message

                        // Log or use the values as needed
                        Log.d("TransactionTAES", "User: ${user.name}, Message: $message")

                        // Handle the event (e.g., updating the UI or database)
                        handleTransactionEvent(responseTransaction)
                    } catch (e: Exception) {
                        Log.e("TransactionTAES", "Error parsing data: ${e.message}")
                    }
                }

// Listen to the "globalRecord" event
                socket?.on("globalRecord") { args ->
                    try {
                        val jsonObject = args[0] as JSONObject
                        // Parse the JSON into ResponseMessage
                        val responseMessage = gson.fromJson(jsonObject.toString(), ResponseMessage::class.java)

                        // Access the message from the ResponseMessage object
                        val message = responseMessage.message

                        // Log or use the message as needed
                        Log.d("GlobalRecord", "Message: $message")

                        // Handle the event (e.g., updating the UI or database)
                        handleGlobalRecordEvent(responseMessage)
                    } catch (e: Exception) {
                        Log.e("GlobalRecord", "Error parsing data: ${e.message}")
                    }
                }


                connect()
            } catch (e: URISyntaxException) {
                Log.e(TAG, "Invalid WebSocket URL: ${e.message}")
            }
        }
    }

    // Get the socket instance
    fun getSocket(): Socket? {
        if (socket == null) initializeSocket()
        return socket
    }

    // Connect to the WebSocket server
    fun connect() {
        getSocket()
        socket?.let {
            if (!it.connected()) {
                it.connect()
                Log.i(TAG, "WebSocket connection established.")
            }
        } ?: Log.e(TAG, "Socket not initialized.")
    }

    // Disconnect from the WebSocket server
    fun disconnect() {
        socket?.let {
            if (it.connected()) {
                emitLogout(UserData.user.value!!)
                it.disconnect()
                Log.i(TAG, "WebSocket connection closed.")
            } else {
                Log.i(TAG, "WebSocket already disconnected.")
            }
        } ?: Log.e(TAG, "Socket not initialized.")
    }

    // Emit login event
    fun emitLogin(user: User) {
        socket?.let {
            if (it.connected()) {
                val userJson = Gson().toJson(user)  // Convert User object to JSON string
                it.emit("loginTAES", userJson)
                login = true
                Log.i(TAG, "Emitted loginTAES for user: ${user.name}")
            } else {
                Log.e(TAG, "Socket not connected. Cannot emit loginTAES.")
            }
        }
    }

    // Emit logout event
    fun emitLogout(user: User) {
        socket?.let {
            if (it.connected()) {
                it.emit("logoutTAES", user)
                login = false
                Log.i(TAG, "Emitted logoutTAES for user: ${user.name}")
            } else {
                Log.e(TAG, "Socket not connected. Cannot emit logoutTAES.")
            }
        }
    }

    // Emit broadcast event
    fun emitBroadcast(message: String) {
        socket?.let {
            if (it.connected()) {
                it.emit("broadcastTAES", message)
                Log.i(TAG, "Emitted broadcastTAES with message: $message")
            } else {
                Log.e(TAG, "Socket not connected. Cannot emit broadcastTAES.")
            }
        }
    }

    // Handle disconnection event
    private fun onSocketDisconnected() {
        Log.i(TAG, "WebSocket disconnected.")
        if (login && UserData.user.value != null) {
            disconnectedUser = UserData.user.value
        }
        login = false
    }

    // Handle transaction event (received from server)
    private fun handleTransactionEvent(args: ResponseTransaction) {
        // This handles the transaction event, which is received from the server.
        Log.i(TAG, "Transaction event received with args: ${args.message}, ${args.user}")

        // Extract relevant information from the args (assuming args[0] is user and args[1] is message)
        val user = args.user
        val message = args.message
        UserData.updateUser(user)
        // Show a notification with transaction details
        user.let {
            NotificationHelper.showNotification(
                context = AppContextHolder.appContext,  // Pass context of your activity or application
                title = "Transaction for ${it.name}",
                message = message
            )
        }

        // Implement further logic if needed (e.g., update UI, process transaction, etc.)
    }

    // Handle global broadcast event (received from server)
    private fun handleGlobalRecordEvent(args: ResponseMessage) {
        // This handles the global broadcast event, which is received from the server.
        Log.i(TAG, "Global broadcast event received with args: ${args.message}")

        // Extract message from the args (assuming args[0] contains the message)
        val message = args.message

        // Show a notification for the global broadcast
        NotificationHelper.showNotification(
            context = AppContextHolder.appContext,  // Pass context of your activity or application
            title = "Global Broadcast",
            message = message
        )

    }

}

package pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.User
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.UserData
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.Notification
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.NotificationsViewModel
import java.net.URISyntaxException

object WebSocketManager {
    private const val SOCKET_URL = "ws://10.0.2.2:8080"
    private const val TAG = "WebSocketManager"
    private var socket: Socket? = null
    private var login = false
    private var disconnectedUser: User? = null // Store the user on disconnect

    // Initialize the socket connection
    fun initializeSocket() {
        if (socket == null) {
            try {
                socket = IO.socket(SOCKET_URL)
                socket?.on(Socket.EVENT_CONNECT) {
                    Log.i(TAG, "Connected to WebSocket server.")
                    if (UserData.user != null) {
                        emitLogin(UserData.user!!)
                    }
                }
                socket?.on(Socket.EVENT_DISCONNECT) {
                    onSocketDisconnected()
                }
                socket?.on("transactionTAES") { args ->
                    handleTransactionEvent(args)
                }
                socket?.on("globalRecord") { args ->
                    handleGlobalRecordEvent(args)
                }
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
                emitLogout(UserData.user!!)
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
                it.emit("loginTAES", user)
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
        if (login && UserData.user != null) {
            disconnectedUser = UserData.user
        }
        login = false
    }

    // Handle transaction event (received from server)
    private fun handleTransactionEvent(args: Array<Any>) {
        // This handles the transaction event, which is received from the server.
        Log.i(TAG, "Transaction event received with args: ${args.joinToString()}")

        // Extract relevant information from the args (assuming args[0] is user and args[1] is message)
        val user = args.getOrNull(0) as? User
        val message = args.getOrNull(1) as? String ?: "No message"

        // Show a notification with transaction details
        user?.let {
            NotificationHelper.showNotification(
                context = AppContextHolder!!.appContext,  // Pass context of your activity or application
                title = "Transaction for ${it.name}",
                message = message
            )
        }

        // Implement further logic if needed (e.g., update UI, process transaction, etc.)
    }

    // Handle global broadcast event (received from server)
    private fun handleGlobalRecordEvent(args: Array<Any>) {
        // This handles the global broadcast event, which is received from the server.
        Log.i(TAG, "Global broadcast event received with args: ${args.joinToString()}")

        // Extract message from the args (assuming args[0] contains the message)
        val message = args.getOrNull(0) as? String ?: "No global broadcast message"

        // Show a notification for the global broadcast
        NotificationHelper.showNotification(
            context = AppContextHolder.appContext,  // Pass context of your activity or application
            title = "Global Broadcast",
            message = message
        )

    }

}

package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.TokenRefresher

class AppInitializer {
    companion object {
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
                    false
                }

                if (isAuthenticated){
                    val options = PusherOptions()
                    options.setCluster("eu");

                    val pusher = Pusher("d3e370f24b73926cb383", options)

                    pusher.connect(object : ConnectionEventListener {
                        override fun onConnectionStateChange(change: ConnectionStateChange) {
                            Log.i("Pusher", "State changed from ${change.previousState} to ${change.currentState}")
                        }

                        override fun onError(
                            message: String,
                            code: String,
                            e: Exception
                        ) {
                            Log.i("Pusher", "There was a problem connecting! code ($code), message ($message), exception($e)")
                        }
                    }, ConnectionState.ALL)

                    val channel = pusher.subscribe("my-channel")
                    channel.bind("my-event") { event ->
                        Log.i("Pusher","Received event with data: $event")
                    }
                }

                withContext(Dispatchers.Main) {
                    onInitializationComplete(isAuthenticated)
                }
            }
        }
    }
}

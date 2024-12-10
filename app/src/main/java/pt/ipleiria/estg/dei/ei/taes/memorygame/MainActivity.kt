package pt.ipleiria.estg.dei.ei.taes.memorygame
import BrainViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.AppInitializer
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.AppNavigation
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.TokenRefresher
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.DashboardScreen
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.MemoryGameTheme
import android.util.Log
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        AppInitializer.initializeApp(this) { isAuthenticated ->
            setContent {
                MemoryGameTheme {
                    AppNavigation(isAuthenticated)
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        TokenRefresher.stop()
    }
}

//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val options = PusherOptions()
//        options.setCluster("eu");
//
//        val pusher = Pusher("d3e370f24b73926cb383", options)
//
//        pusher.connect(object : ConnectionEventListener {
//            override fun onConnectionStateChange(change: ConnectionStateChange) {
//                Log.i("Pusher", "State changed from ${change.previousState} to ${change.currentState}")
//            }
//
//            override fun onError(
//                message: String,
//                code: String,
//                e: Exception
//            ) {
//                Log.i("Pusher", "There was a problem connecting! code ($code), message ($message), exception($e)")
//            }
//        }, ConnectionState.ALL)
//
//        val channel = pusher.subscribe("my-channel")
//        channel.bind("my-event") { event ->
//            Log.i("Pusher","Received event with data: $event")
//        }
//    }
//}
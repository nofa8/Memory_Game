package pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*

object TokenRefresher {
    private var refreshJob: Job? = null
    private const val REFRESH_INTERVAL = 1000L * 60 * 110 // 110 minutes

    fun start(context: Context, onError: (String) -> Unit) {
        if (refreshJob?.isActive == true) {
            refreshJob?.cancel() // Cancel any existing job
        }

        refreshJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                try {
                    val api = API.getInstance(context)
                    val refreshResponse = API.callApi("${API.url}/auth/refreshtoken", "POST")

                    // Assuming the response contains a JSON object with the new token
                    val jsonResponse = Gson().fromJson(refreshResponse, JsonObject::class.java)
                    val newToken = jsonResponse["token"]?.asString

                    if (!newToken.isNullOrEmpty()) {
                        api.saveToken(newToken) // Save the new token
                    } else {
                        throw Exception("Failed to refresh token: Invalid response")
                    }

                } catch (e: Exception) {
                    onError(e.message ?: "Unknown error during token refresh")
                    stop() // Stop the token refresher if an error occurs
                }

                delay(REFRESH_INTERVAL) // Wait for the interval before the next refresh
            }
        }
    }

    fun stop() {
        refreshJob?.cancel()
        refreshJob = null
    }
}


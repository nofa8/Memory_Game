package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.ScoreController.fetchScores
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.ScoreController.scores
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API

data class Board(
    val id: Int,
    val cols: Int,
    val rows: Int
)


object BoardData {
    private val apiUrl = "${API.url}/boards"
    var boards: List<Board> = emptyList()
        private set



    suspend fun fetchBoards(): Boolean {
        return try {
            // Fetch the data using the API
            val jsonResponse = withContext(Dispatchers.IO) {
                API.callApi(apiUrl = apiUrl, httpMethod = "GET")
            }

            // Parse the JSON response
            val jsonObject = Gson().fromJson(jsonResponse, JsonObject::class.java)
            val dataArray = jsonObject.getAsJsonArray("data")

            // Convert JSON array into a list of Board objects
            val type = object : TypeToken<List<Board>>() {}.type
            boards = Gson().fromJson(dataArray, type)

            true // Return true on success
        } catch (e: Exception) {
            e.printStackTrace()
            boards = emptyList() // Reset to an empty list on failure
            false // Return false on failure
        }
    }
}

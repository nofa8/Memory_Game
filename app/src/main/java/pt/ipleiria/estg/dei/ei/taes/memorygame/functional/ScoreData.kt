package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import java.util.Date

//data class ScoreEntry(
//    val id: Int,
//    val name: String,
//    val time: String,
//    val moves: Int,
//    val boardSize: String,
//    val date: Date
//)
data class ScoreEntry(
    val id: Int,
    val type: String,
    val status: String,
    val time: String,
    val creator: Int, // Represents the creator's ID
    val name: String, // Represents the creator's name
    val start_time: String,
    val end_time: String,
    val board: String, // Updated to match the "3x4" or "6x6" string format
    val turns: Int // Nullable, as `turns`
)


object ScoreDataRepository {
    // Declare scores as a mutable list that will be populated asynchronously
    var scores: List<ScoreEntry> = emptyList()

    init {
        // Initialize the scores list asynchronously
        CoroutineScope(Dispatchers.IO).launch {
            scores = fetchScores()  // Call suspend function to fetch data
        }
    }

    // Fetch scores from the API (suspend function)
    suspend fun fetchScores(): List<ScoreEntry> {
        return try {
            // Call the API on a background thread using Dispatchers.IO
            val jsonResponse = withContext(Dispatchers.IO) {
                API.callApi(apiUrl = API.url + "/games", httpMethod = "GET")
            }

            // Parse the entire JSON object
            val jsonObject = Gson().fromJson(jsonResponse, JsonObject::class.java)

            // Extract the "data" array
            val dataArray = jsonObject.getAsJsonArray("data")

            // Convert the array to a list of ScoreEntry
            val type = object : TypeToken<List<ScoreEntry>>() {}.type
            Gson().fromJson(dataArray, type)

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Return an empty list if there is an error
        }
    }
}


fun calculateScore(timeSec: Int, moves: Int): Int {
    val baseScore = 1000.0 // Pontuação inicial
    val tempoBase = 60.0 // Tempo base (1 minuto)
    val jogadasBase = 20.0 // Jogadas base (20 jogadas)
    val ponderacaoTempo = 1.5 // Peso do tempo
    val ponderacaoJogadas = 2.0 // Peso das jogadas

    // Fórmula de cálculo
    val divisor = 1 + (timeSec / tempoBase * ponderacaoTempo) + (moves / jogadasBase * ponderacaoJogadas)
    val score = baseScore / divisor

    // Retorna o score arredondado para inteiro
    return score.toInt()
}
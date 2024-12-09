package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update



data class ScoreEntry(
    val id: Int,
    val type: String,
    val status: String,
    val total_time: Float,
    val creator: Int, // Represents the creator's ID
    val name: String, // Represents the creator's name
    val start_time: String,
    val end_time: String,
    val board: Int, // Updated to match the "3x4" or "6x6" string format or not : /
    val turns: Int // Nullable, as `turns`
)




object ScoreController {
    // Use StateFlow for reactive updates
    private val _scores = MutableStateFlow<List<ScoreEntry>>(emptyList())
    val scores: StateFlow<List<ScoreEntry>> = _scores

    private val _history = MutableStateFlow<List<ScoreEntry>>(emptyList())
    val history: StateFlow<List<ScoreEntry>> = _history

    // Fetch scores from the API (suspend function)
    suspend fun fetchScores(): List<ScoreEntry> {
        return try {
            val jsonResponse = withContext(Dispatchers.IO) {
                API.callApi(apiUrl = API.url + "/games", httpMethod = "GET")
            }
            val jsonObject = Gson().fromJson(jsonResponse, JsonObject::class.java)
            val dataArray = jsonObject.getAsJsonArray("data")
            val type = object : TypeToken<List<ScoreEntry>>() {}.type
            Gson().fromJson(dataArray, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun fetchHistory(): List<ScoreEntry> {
        return try {
            val jsonResponse = withContext(Dispatchers.IO) {
                API.callApi(apiUrl = API.url + "/indexHistoryTAES", httpMethod = "GET")
            }
            val jsonObject = Gson().fromJson(jsonResponse, JsonObject::class.java)
            val dataArray = jsonObject.getAsJsonArray("data")
            val type = object : TypeToken<List<ScoreEntry>>() {}.type
            Gson().fromJson(dataArray, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Update scores and history
    suspend fun refreshScores() {
        val newScores = fetchScores()
        _scores.update { newScores }
    }

    suspend fun refreshHistory() {
        val newHistory = fetchHistory()
        _history.update { newHistory }
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
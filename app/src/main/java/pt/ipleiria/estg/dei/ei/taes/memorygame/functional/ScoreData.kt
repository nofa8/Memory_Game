package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import java.util.Date

data class ScoreEntry(
    val id: Int,
    val name: String,
    val time: String,
    val moves: Int,
    val boardSize: String,
    val date: Date
)

object ScoreDataRepository {
    val scores: List<ScoreEntry> = try {
        val jsonResponse = API.callApi(apiUrl = API.url+"/games", httpMethod = "GET")

        // Parse the entire JSON object
        val jsonObject = Gson().fromJson(jsonResponse, JsonObject::class.java)

        // Extract the "data" array
        val dataArray = jsonObject.getAsJsonArray("data")

        // Convert the array to a list of ScoreEntry
        val type = object : TypeToken<List<ScoreEntry>>() {}.type
        Gson().fromJson(dataArray, type)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList<ScoreEntry>()
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
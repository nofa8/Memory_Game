package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import java.util.Calendar
import java.util.Date


data class ScoreEntry(
    val name: String,
    val time: String,
    val moves: Int,
    val scores: Int,
    val boardSize: String,
    val date: Date
)

object ScoreDataRepository {
    val sampleScores = listOf(
        ScoreEntry("Player 1", "02:30", 45, 1200, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 1", "02:40", 65, 1200, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 1", "02:30", 45, 1200, "4x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 2", "03:15", 55, 900, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 3", "01:45", 35, 1500, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 4", "02:00", 40, 1400, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 5", "01:30", 38, 1600, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 6", "02:45", 50, 1100, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 7", "01:20", 32, 1700, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 7", "01:20", 33, 1700, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 8", "01:55", 37, 1450, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 9", "01:40", 36, 1550, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 10", "01:35", 39, 1500, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 11", "01:25", 33, 1650, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28)),
        ScoreEntry("Player 12", "01:50", 41, 1400, "3x4", generateRandomDate(2023, 11, 1, 2024, 2, 28))
    )

    // Função para gerar uma data aleatória entre dois intervalos
    private fun generateRandomDate(startYear: Int, startMonth: Int, startDay: Int,
                                   endYear: Int, endMonth: Int, endDay: Int): Date {
        val calendar = Calendar.getInstance()

        // Configurar os limites de data
        val startCalendar = Calendar.getInstance().apply {
            set(startYear, startMonth - 1, startDay)
        }

        val endCalendar = Calendar.getInstance().apply {
            set(endYear, endMonth - 1, endDay)
        }

        // Calcular a diferença de milissegundos entre as datas
        val diff = endCalendar.timeInMillis - startCalendar.timeInMillis

        // Gerar um valor aleatório dentro desse intervalo
        val randomOffset = (Math.random() * diff).toLong()

        // Adicionar o offset à data inicial
        calendar.timeInMillis = startCalendar.timeInMillis + randomOffset

        return calendar.time
    }
}

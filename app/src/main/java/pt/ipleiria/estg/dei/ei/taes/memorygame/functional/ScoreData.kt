package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

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
        ScoreEntry("Player 1", "02:30", 45, 1200, "3x4", Date(2024 - 1900, 1 - 1, 30, 14, 30)),
        ScoreEntry("Player 1", "02:40", 65, 1200, "3x4", Date(2024 - 1900, 1 - 1, 2, 15, 15)),
        ScoreEntry("Player 1", "02:30", 45, 1200, "4x4", Date(2024 - 1900, 1 - 1, 3, 16, 0)),
        ScoreEntry("Player 1", "03:15", 55, 900, "3x4", Date(2024 - 1900, 1 - 1, 4, 17, 45)),
        ScoreEntry("Player 1", "01:45", 35, 1500, "4x4", Date(2024 - 1900, 1 - 1, 5, 18, 20)),
        ScoreEntry("Player 1", "02:00", 40, 1400, "3x4", Date(2024 - 1900, 1 - 1, 6, 19, 10)),
        ScoreEntry("Player 1", "01:30", 38, 1600, "3x4", Date(2024 - 1900, 1 - 1, 7, 20, 5)),
        ScoreEntry("Player 1", "02:45", 50, 1100, "4x4", Date(2024 - 1900, 1 - 1, 8, 21, 50)),
        ScoreEntry("Player 1", "01:20", 32, 1700, "3x4", Date(2024 - 1900, 1 - 1, 9, 22, 25)),
        ScoreEntry("Player 1", "01:20", 33, 1700, "3x4", Date(2024 - 1900, 1 - 1, 10, 23, 0)),
        ScoreEntry("Player 1", "01:55", 37, 1450, "6x6", Date(2024 - 1900, 1 - 1, 11, 13, 35)),
        ScoreEntry("Player 1", "01:40", 36, 1550, "3x4", Date(2024 - 1900, 1 - 1, 12, 12, 10)),
        ScoreEntry("Player 1", "01:35", 39, 1500, "3x4", Date(2024 - 1900, 1 - 1, 13, 14, 15)),
        ScoreEntry("Player 2", "01:25", 33, 1650, "3x4", Date(2024 - 1900, 1 - 1, 14, 15, 5)),
        ScoreEntry("Player 3", "01:50", 41, 1400, "6x6", Date(2024 - 1900, 1 - 1, 15, 16, 0)),
        ScoreEntry("Player 4", "01:50", 41, 1400, "6x6", Date(2024 - 1900, 1 - 1, 16, 17, 30)),
        ScoreEntry("Player 5", "01:50", 41, 1400, "6x6", Date(2024 - 1900, 1 - 1, 17, 18, 15)),
        ScoreEntry("Player 6", "01:50", 41, 1400, "6x6", Date(2024 - 1900, 1 - 1, 18, 19, 45)),
        ScoreEntry("Player 7", "01:35", 39, 1500, "6x6", Date(2024 - 1900, 1 - 1, 19, 20, 25)),
        ScoreEntry("Player 8", "01:25", 33, 1650, "6x6", Date(2024 - 1900, 1 - 1, 20, 21, 10)),
        ScoreEntry("Player 9", "01:50", 41, 1400, "3x4", Date(2024 - 1900, 1 - 1, 21, 22, 55)),
        ScoreEntry("Player 10", "01:50", 41, 1400, "4x4", Date(2024 - 1900, 1 - 1, 22, 23, 45)),
        ScoreEntry("Player 11", "01:50", 41, 1400, "4x4", Date(2024 - 1900, 1 - 1, 23, 13, 20)),
        ScoreEntry("Player 12", "01:50", 41, 1400, "4x4", Date(2024 - 1900, 1 - 1, 24, 14, 40))
    )
}

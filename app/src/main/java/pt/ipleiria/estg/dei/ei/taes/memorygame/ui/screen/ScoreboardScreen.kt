package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen

import BrainViewModel
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BoardDropdown
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BottomActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BrainCoinsButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.FilterBoardDropdown
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.FilterTypeDropdown
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.NotificationButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.ScoreEntry
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.ScoreTab
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.TopActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBackground


@Composable
fun ScoreboardScreen(navController: NavController, brainViewModel: BrainViewModel) {
    val sampleScores = listOf(
        ScoreEntry("Player 1", "02:30", 45, 1200, "3x4"),
        ScoreEntry("Player 1", "02:40", 65, 1200, "3x4"),
        ScoreEntry("Player 1", "02:30", 45, 1200, "4x4"),
        ScoreEntry("Player 2", "03:15", 55, 900, "3x4"),
        ScoreEntry("Player 3", "01:45", 35, 1500, "3x4"),
        ScoreEntry("Player 4", "02:00", 40, 1400, "3x4"),
        ScoreEntry("Player 5", "01:30", 38, 1600, "3x4"),
        ScoreEntry("Player 6", "02:45", 50, 1100, "3x4"),
        ScoreEntry("Player 7", "01:20", 32, 1700, "3x4"),
        ScoreEntry("Player 7", "01:20", 33, 1700, "3x4"),
        ScoreEntry("Player 8", "01:55", 37, 1450, "3x4"),
        ScoreEntry("Player 9", "01:40", 36, 1550, "3x4"),
        ScoreEntry("Player 10", "01:35", 39, 1500, "3x4"),
        ScoreEntry("Player 11", "01:25", 33, 1650, "3x4"),
        ScoreEntry("Player 12", "01:50", 41, 1400, "3x4")
    )
    var selectedBoard by remember { mutableStateOf("3x4") }
    var selectedType by remember { mutableStateOf("Personal") }

    //  convert time string to seconds
    fun timeToSeconds(time: String): Int {
        val (minutes, seconds) = time.split(":").map { it.toInt() }
        return minutes * 60 + seconds
    }

    // Filter, sort, and select top 10 performances
    val topPerformances = remember(selectedBoard, selectedType) {
        sampleScores
            .filter { it.boardSize == selectedBoard }
            .filter {
                if (selectedType == "Personal") it.name == "Player 1" // Only Player 1's scores
                else true
            }
            .sortedWith(
                compareBy<ScoreEntry> { timeToSeconds(it.time) }
                    .thenBy { it.moves }
            )
            .take(10)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ColorBackground
    ) { paddings ->
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            TopActionBar(
                modifier = Modifier
                    .padding(
                        horizontal = 24.dp,
                        vertical = 10.dp
                    )
                    .padding(paddings),
                leftFunction = { BrainCoinsButton(brainViewModel = brainViewModel) },
                rightFunction = { NotificationButton() }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterTypeDropdown(
                    selectedValue = selectedType,
                    onOptionSelected = { selectedType = it },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.weight(2f))

                FilterBoardDropdown(
                    selectedValue = selectedBoard,
                    onOptionSelected = { selectedBoard = it },
                    modifier = Modifier.weight(1f)
                )
            }

            ScoreTab(
                scores = topPerformances,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))
            BottomActionBar(
                onScoresClick = { navController.navigate("scoreboard") },
                onPlayClick = { navController.navigate("dashboard") },
                onProfileClick = { navController.navigate("profile") }
            )
        }
    }
}
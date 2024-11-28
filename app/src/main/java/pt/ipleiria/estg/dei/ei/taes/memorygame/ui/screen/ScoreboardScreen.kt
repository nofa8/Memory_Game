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
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.ScoreDataRepository.sampleScores
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.ScoreEntry
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BoardDropdown
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BottomActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BrainCoinsButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.FilterBoardDropdown
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.FilterTypeDropdown
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.NotificationButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.ScoreTab
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.TopActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBackground


@Composable
fun ScoreboardScreen(navController: NavController, brainViewModel: BrainViewModel) {
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
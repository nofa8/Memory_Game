package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen

import BrainViewModel
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BottomActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BrainCoinsButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.NotificationButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.ScoreEntry
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.ScoreTab
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.TopActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBackground


@Composable
fun ScoreboardScreen(navController: NavController, brainViewModel: BrainViewModel) {
    val sampleScores = listOf(
        ScoreEntry("Player 1", "02:30", 45, 1200),
        ScoreEntry("Player 2", "03:15", 55, 900),
        ScoreEntry("Player 3", "01:45", 35, 1500),
        ScoreEntry("Player 4", "02:00", 40, 1400),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ColorBackground
    ){ paddings ->
        Column (
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
        ){

            TopActionBar(modifier = Modifier
                .padding(
                    horizontal = 24.dp,
                    vertical = 10.dp
                )
                .padding(paddings),
                leftFunction = {BrainCoinsButton(brainViewModel = brainViewModel)},
                rightFunction = {NotificationButton()}
            )
            ScoreTab(scores = sampleScores,
                Modifier.padding(vertical = 4.dp).fillMaxWidth())

            Spacer(modifier = Modifier.weight(1f))
            BottomActionBar(
                onScoresClick = { navController.navigate("scoreboard") },
                onPlayClick = { navController.navigate("dashboard") },
                onProfileClick = { navController.navigate("profile") }
            )
        }

    }
}
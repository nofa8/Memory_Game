package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen

import BrainViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.NotificationsViewModel
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.ScoreController
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BottomActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BrainCoinsButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.FilterBoardDropdown
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.FilterTypeDropdown
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.NotificationButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.ScoreTab
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.TopActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBackground


@Composable
fun ScoreboardScreen(
    navController: NavController,
    brainViewModel: BrainViewModel,
    notificationsViewModel: NotificationsViewModel
) {
    var selectedBoard by remember { mutableStateOf("3x4") }
    var selectedType by remember { mutableStateOf("Global") }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            ScoreController.refreshScores()
            ScoreController.refreshPersonal()
        }
    }

    // Observe the scores reactively
    val scores by ScoreController.scores.collectAsState(initial = emptyList())
    val scoresPersonal by ScoreController.scoresPersonal.collectAsState(initial = emptyList())




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
                rightFunction = { NotificationButton(notificationsViewModel = notificationsViewModel) }
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
                    modifier = Modifier.weight(2f)
                )

                Spacer(modifier = Modifier.weight(1f))


            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Main content - ScoreTab
                ScoreTab(
                    scores = if (selectedType == "Global") scores else scoresPersonal,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                        .weight(1f) // Take up all available space, pushing BottomActionBar down
                )

                // Bottom Action Bar
                BottomActionBar(
                    onScoresClick = { navController.navigate("scoreboard") },
                    onPlayClick = { navController.navigate("dashboard") },
                    onProfileClick = { navController.navigate("profile") },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

        }
    }
}
package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen


import BrainViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.NotificationsViewModel
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BottomActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BrainCoinsButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.GameTab

import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.NotificationButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.TopActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBackground





@Composable
fun DashboardScreen(
    navController: NavController,
    brainViewModel: BrainViewModel,
    notificationsViewModel: NotificationsViewModel
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ColorBackground
    ){ paddings ->
        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
        ){
            TopActionBar(modifier = Modifier.padding(
                horizontal =  24.dp,
                vertical = 10.dp
            ).padding(paddings),
                leftFunction = {BrainCoinsButton(brainViewModel = brainViewModel)},
                rightFunction = {NotificationButton(
                    notificationsViewModel = notificationsViewModel
                )}
            )
            GameTab(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
                text = "Play",
                navController = navController,
                brainViewModel = brainViewModel
            )
            BottomActionBar(
                onScoresClick = { navController.navigate("scoreboard") },
                onPlayClick = { navController.navigate("dashboard") },
                onProfileClick = { navController.navigate("profile") }
            )

        }

    }
}



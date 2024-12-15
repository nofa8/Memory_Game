package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen

import BrainViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.UserData
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BottomActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.ProfileStuff
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BrainCoinsButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.NotificationButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.TopActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBackground

@Composable
fun ProfileScreen(navController: NavController, brainViewModel: BrainViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ColorBackground
    ) { paddings ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Top Action Bar
            TopActionBar(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 10.dp)
                    .padding(paddings)
                    .weight(1f),
                leftFunction = { BrainCoinsButton(brainViewModel = brainViewModel) },
                rightFunction = { NotificationButton() }
            )

            // Content based on token availability
            if (API.token.isNotEmpty()) {
                // Show history and logout button if the user is logged in
                ProfileStuff("History", navController, "history")

                Spacer(modifier = Modifier.height(20.dp))

                ProfileStuff("Logout", navController, "profile")

            } else {
                // Show login screen if the user is not logged in
                LoginScreen(navController, Modifier.fillMaxWidth(), brainViewModel = brainViewModel)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Action Bar
            BottomActionBar(
                onScoresClick = { navController.navigate("scoreboard") },
                onPlayClick = { navController.navigate("dashboard") },
                onProfileClick = { navController.navigate("profile") }
            )
        }
    }
}

package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen

import BrainViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.ScoreController
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BottomActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BrainCoinsButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.HistoryTab
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.NotificationButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.TopActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBackground
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.NotificationsViewModel
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.FilterTypeTurnsNTime
import java.util.Calendar

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.FilterBoardDropdown
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.FilterTypeTurnsNTimeNDate
import java.util.Locale

@Composable
fun HistoryScreen(
    navController: NavController,
    brainViewModel: BrainViewModel,
    notificationsViewModel: NotificationsViewModel
) {

    val playerHistory by ScoreController.history.collectAsState(initial = emptyList())

    var selectedOrder by remember { mutableStateOf("Date") }
    var selectedBoard by remember { mutableStateOf("3x4") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    // Side effect to refresh data
    LaunchedEffect(Unit) {
        ScoreController.refreshHistory()

    }

    val onStartDateSelected: (String) -> Unit = { start ->
        startDate = start
    }

    val onEndDateSelected: (String) -> Unit = { end ->
        endDate = end
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ColorBackground
    ) { paddings ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopActionBar(modifier = Modifier.padding(
                start = 24.dp, end = 24.dp,
                top = 10.dp
            ).padding(paddings),
                leftFunction = { BrainCoinsButton(brainViewModel = brainViewModel) },
                rightFunction = { NotificationButton(
                    notificationsViewModel = notificationsViewModel
                ) }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Date Range Selector
                    TwoDateSelectors(
                        onStartDateSelected = onStartDateSelected,
                        onEndDateSelected = onEndDateSelected
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        FilterTypeTurnsNTimeNDate(
                            selectedValue = selectedOrder,
                            onOptionSelected = { selectedOrder = it },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.weight(1f))

                        FilterBoardDropdown(selectedValue = selectedBoard,
                            onOptionSelected = { selectedBoard = it },
                            modifier = Modifier.weight(1f)
                        )


                    }
                    FilterButton {
                        CoroutineScope(Dispatchers.Main).launch {
                            ScoreController.refreshFilterHistory(selectedOrder, startDate, endDate)
                    } }

                    // History Tab showing the player history
                    HistoryTab(
                        scores = playerHistory,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 4.dp)
                    )
                }
            }

            BottomActionBar(
                onScoresClick = { navController.navigate("scoreboard") },
                onPlayClick = { navController.navigate("dashboard") },
                onProfileClick = { navController.navigate("profile") }
            )
        }
    }
}

@Composable
fun FilterButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors()
    ) {
        Text(
            text = "Filter",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}


@Composable
fun TwoDateSelectors(
    onStartDateSelected: (String) -> Unit = {},
    onEndDateSelected: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // States for selected dates
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    // DatePickerDialog launcher for Start Date
    val openStartDatePicker = {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                startDate = formatDate(year, month, dayOfMonth)
                onStartDateSelected(startDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // DatePickerDialog launcher for End Date
    val openEndDatePicker = {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                endDate = formatDate(year, month, dayOfMonth)
                onEndDateSelected(endDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Layou
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {        // Start Date Picker
        OutlinedButton(
            onClick = openStartDatePicker,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), // Set a height for buttons
            border = BorderStroke(1.dp, Color.Gray),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
        ) {
            Text(
                text = if (startDate.isEmpty()) "Select Start Date" else "Start Date: $startDate",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                maxLines = 1
            )
        }

        // End Date Picker
        OutlinedButton(
            onClick = openEndDatePicker,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), // Set a height for buttons
            border = BorderStroke(1.dp, Color.Gray),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
        ) {
            Text(
                text = if (endDate.isEmpty()) "Select End Date" else "End Date: $endDate",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                maxLines = 1
            )
        }
    }
}


fun formatDate(year: Int, month: Int, day: Int): String {
    return String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year)
}

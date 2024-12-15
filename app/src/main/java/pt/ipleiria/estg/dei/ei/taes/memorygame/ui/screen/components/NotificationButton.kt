package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.ContentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import pt.ipleiria.estg.dei.ei.taes.memorygame.R
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.NotificationsViewModel
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorImageBlueShadow
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorSurface

@Composable
fun NotificationButton(
    modifier: Modifier = Modifier,
    notificationsViewModel: NotificationsViewModel
    ) {
    // Count unread notifications
    val unreadCount = notificationsViewModel.notifications.count { !it.isRead }

    // State to track popup visibility
    var isPopupVisible by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.TopEnd // Align badge to the top-end of the button
    ) {
        // Notification Button
        Box(
            modifier = Modifier
                .size(60.dp)
                .border(
                    width = 1.5.dp,
                    color = ColorSurface,
                    shape = CircleShape
                )
                .customShadow(
                    color = ColorImageBlueShadow,
                    alpha = 0.15f,
                    shadowRadius = 8.dp,
                    borderRadius = 48.dp,
                    offsetY = 3.dp
                )
                .clickable(onClick = { isPopupVisible = true }), // Show popup on click
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.bell_64),
                contentDescription = "Notifications",
                modifier = modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
        }

        // Badge for unread notifications
        if (unreadCount > 0) {
            UnreadCountBadge(unreadCount = unreadCount)
        }
    }

    // Show the popup if `isPopupVisible` is true
    if (isPopupVisible) {
        PopUpNotification(
            notificationsViewModel = notificationsViewModel,
            quitFunction = { isPopupVisible = false } // Close popup
        )
    }
}

@Composable
fun UnreadCountBadge(unreadCount: Int) {
    Box(
        modifier = Modifier
            .size(20.dp) // Badge size
            .background(Color.Red, shape = CircleShape)
            .border(1.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = unreadCount.toString(),
            color = Color.White,
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(2.dp), // Padding inside the badge
        )
    }
}



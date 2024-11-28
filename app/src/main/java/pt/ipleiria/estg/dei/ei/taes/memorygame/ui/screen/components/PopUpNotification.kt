package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.semantics.SemanticsProperties.ContentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.intellij.lang.annotations.Identifier
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.NotificationsViewModel
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBackground




@Composable
fun PopUpNotification(
    notificationsViewModel: NotificationsViewModel,
    quitFunction: () -> Unit
) {
    val notifications = notificationsViewModel.notifications

    // Mark a single notification as read
    fun markNotificationAsRead(index: Int) {
        notifications[index] = notifications[index].copy(isRead = true)
    }

    // Mark all unread notifications as read
    fun markAllNotificationsAsRead() {
        notifications.forEachIndexed { index, notification ->
            if (!notification.isRead) {
                notifications[index] = notification.copy(isRead = true)
            }
        }
    }

    Dialog(
        onDismissRequest = {
            quitFunction() // Close the popup
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false // Custom width
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Make the dialog cover the full screen
                .background(Color.Black.copy(alpha = 0.3f)) // Dimmed background
                .clickable { quitFunction() }, // Close the dialog when clicking outside
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f) // 90% of the screen width
                    .fillMaxHeight(0.9f) // 90% of the screen height
                    .background(Color.White.copy(alpha = 0.85f), shape = RoundedCornerShape(20.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(text = "Notifications", style = MaterialTheme.typography.h4)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Non-read notifications: ${notifications.count { !it.isRead }}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Total notifications: ${notifications.size}",
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to mark all notifications as read
                    Button(
                        onClick = { markAllNotificationsAsRead() },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = orange,
                            contentColor = white
                        )
                    ) {
                        Text(
                            text = "Mark All as Read",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color(0xFFFFFFFF)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // Add some space after the button

                    // Notifications list
                    LazyColumn(
                        modifier = Modifier.weight(1f), // Take available vertical space
                        verticalArrangement = Arrangement.spacedBy(8.dp) // Spacing between items
                    ) {
                        itemsIndexed(notifications.sortedByDescending { it.timestamp }) { index, notification ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                                    .clickable {
                                        // Mark as read when clicked
                                        markNotificationAsRead(index)
                                    }
                            ) {
                                // Title with bold style for unread notifications
                                Text(
                                    text = notification.title,
                                    style = if (!notification.isRead) {
                                        MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                                    } else {
                                        MaterialTheme.typography.subtitle1
                                    },
                                    color = Color.Black,
                                )

                                // Message content
                                Text(
                                    text = notification.message,
                                    style = MaterialTheme.typography.body2,
                                    color = Color.Black
                                )

                                // Timestamp with additional "Unread" sign for unread notifications
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = notification.timestamp,
                                        style = MaterialTheme.typography.caption,
                                        color = Color.Black
                                    )
                                    if (!notification.isRead) {
                                        Text(
                                            text = "Não Lida", // "Unread" marker
                                            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
                                            color = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // Add space before closing the popup

                    // Close button
                    Button(
                        onClick = quitFunction,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = orange,
                            contentColor = white
                        )
                    ) {
                        Text(
                            text = "Close",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color(0xFFFFFFFF)
                        )
                    }
                }
            }
        }
    }
}


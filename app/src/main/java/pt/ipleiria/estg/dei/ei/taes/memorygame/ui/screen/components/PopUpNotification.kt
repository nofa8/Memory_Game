package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBackground

data class Notification(
    val title: String,
    val message: String,
    val timestamp: String
)


@Composable
fun PopUpNotification(
    quitFunction: () -> Unit
) {
    val notifications: List<Notification> = listOf(
        Notification("Nova Mensagem", "Você recebeu uma nova mensagem de João.", "10:45"),
        Notification("Atualização", "O sistema foi atualizado para a versão 1.2.", "09:30"),
        Notification("Promoção", "Descontos de até 50% apenas hoje!", "08:15"),
        Notification("Alerta", "Sua conta será bloqueada em 24 horas.", "07:00"),Notification("Nova Mensagem", "Você recebeu uma nova mensagem de João.", "10:45"),
        Notification("Atualização", "O sistema foi atualizado para a versão 1.2.", "09:30"),
        Notification("Promoção", "Descontos de até 50% apenas hoje!", "08:15"),
        Notification("Alerta", "Sua conta será bloqueada em 24 horas.", "07:00"),Notification("Nova Mensagem", "Você recebeu uma nova mensagem de João.", "10:45"),
        Notification("Atualização", "O sistema foi atualizado para a versão 1.2.", "09:30"),
        Notification("Promoção", "Descontos de até 50% apenas hoje!", "08:15"),
        Notification("Alerta", "Sua conta será bloqueada em 24 horas.", "07:00"),Notification("Nova Mensagem", "Você recebeu uma nova mensagem de João.", "10:45"),
        Notification("Atualização", "O sistema foi atualizado para a versão 1.2.", "09:30"),
        Notification("Promoção", "Descontos de até 50% apenas hoje!", "08:15"),
        Notification("Alerta", "Sua conta será bloqueada em 24 horas.", "07:00")
    )



    Dialog(
        onDismissRequest = {
            quitFunction()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Make the dialog cover the full screen
                .background(Color.Black.copy(alpha = 0.3f))
                // Dimmed background
                .clickable { quitFunction() }, // Close the dialog when clicking outside
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f) // 90% of the screen width
                    .fillMaxHeight(0.9f)// Ensure the inner container also fills the screen
                    .background(Color.White.copy(alpha = 0.85f), shape = RoundedCornerShape(20.dp))// Optional: No rounded corners for fullscreen
                    .padding(16.dp), // Inner padding
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center // Center the content vertically

                ) {
                    Text(text = "Notifications", style = MaterialTheme.typography.h4)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Non read notifications: ${notifications.size}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Notifications total: ${notifications.size}",
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    LazyColumn(
                        modifier = Modifier.weight(1f), // Take available vertical space
                        verticalArrangement = Arrangement.spacedBy(8.dp) // Spacing between items
                    ) {
                        items(notifications) { notification ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = notification.title,
                                    style = MaterialTheme.typography.subtitle1,
                                    color = Color.Black
                                )
                                Text(
                                    text = notification.message,
                                    style = MaterialTheme.typography.body2,
                                    color = Color.Black
                                )
                                Text(
                                    text = notification.timestamp,
                                    style = MaterialTheme.typography.caption,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                    Button(
                        onClick = quitFunction,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Close")
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

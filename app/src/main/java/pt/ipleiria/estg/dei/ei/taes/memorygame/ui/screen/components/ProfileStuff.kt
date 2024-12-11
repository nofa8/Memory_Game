package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API

@Composable
fun ProfileStuff(
    text: String,
    navController: NavController,
    naviagateTo: String
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.padding(horizontal = 90.dp, vertical = 16.dp),
    ) {

        Button(
            colors = ButtonColors(
                containerColor = Color(0xFFF7F8E3),
                contentColor = Color(0xFFFFFFFF),
                disabledContainerColor = Color(0xAAFFFFFF),
                disabledContentColor = Color(0xAAFFFFFF)
            ),
            onClick = {
                if (naviagateTo == "login"){
                    API.getInstance(context).clearToken()
                }
                navController.navigate(naviagateTo)
            },
            modifier = Modifier.fillMaxWidth().height(70.dp),

            ) {
            Text(text = text,
                style = MaterialTheme.typography.displaySmall,
                color = Color(0xFF4A4A4A),
                fontWeight = FontWeight.Bold,)
        }
    }
}
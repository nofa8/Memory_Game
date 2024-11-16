package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.ipleiria.estg.dei.ei.taes.memorygame.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController


//Todo: Tab do Jogo DropDown de Tabelas, Escolha de cartas?, Jogar

@Composable
fun GameTab(
    modifier: Modifier,
    text: String,
    navController: NavController
) {
    var selectedBoard by remember { mutableStateOf("3x4") }

    Surface(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 9.dp),
        color = Color.Transparent
        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Column(modifier = Modifier.customShadow(
                color = Color(0x401FBAf6),
                alpha = 0.15f,
                shadowRadius = 8.dp,
                borderRadius = 48.dp,
                offsetY = (-3).dp),
                verticalArrangement = Arrangement.SpaceEvenly)
            {
                Row(

                    horizontalArrangement = Arrangement.SpaceEvenly,
                ){

                    Image(
                        painter = painterResource(id = R.drawable.card_blue),
                        contentDescription = text,
                        modifier = Modifier.weight(1f).padding(all = 5.dp)
                    )


                    Image(
                        painter = painterResource(id = R.drawable.card_blue),
                        contentDescription = text,
                        modifier = Modifier.weight(1f).padding(all = 5.dp)
                    )


                }
            }

            Spacer(modifier = Modifier.height(80.dp))
            BoardDropdown(
                selectedValue = selectedBoard,
                onOptionSelected = { selectedBoard = it }
            )
            Row(
                modifier = Modifier.padding(horizontal = 90.dp, vertical = 16.dp),
            ) {

                Button(
                    onClick = {
                        val (cardsRow, cardsColumn) = selectedBoard.split("x").map { it.toInt() }
                        navController.navigate("game/$cardsRow/$cardsColumn")},
                    modifier = Modifier.fillMaxWidth().height(70.dp),

//                shape =
                ) {
                    Text(text = text,
                        style = MaterialTheme.typography.displaySmall,
                        color = Color(0xFF4A4A4A),
                        fontWeight = FontWeight.Bold,)
                }
            }
        }
    }
}
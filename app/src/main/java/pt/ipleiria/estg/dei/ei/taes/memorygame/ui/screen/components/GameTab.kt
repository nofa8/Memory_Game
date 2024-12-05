package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import BrainViewModel
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController


//Todo: Tab do Jogo DropDown de Tabelas, Escolha de cartas?, Jogar

@Composable
fun GameTab(
    modifier: Modifier,
    text: String,
    brainViewModel: BrainViewModel,
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
            val context = LocalContext.current
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
                        contentDescription = "Card Left",
                        modifier = Modifier.weight(1f).padding(all = 5.dp)
                    )


                    Image(
                        painter = painterResource(id = R.drawable.card_blue),
                        contentDescription = "Card Right",
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
                val uiState by brainViewModel.uiState.collectAsState()

                val brainValue = uiState.brainValue
                Button(
                    colors = ButtonColors(
                        containerColor = Color(0xFFF7F8E3),
                        contentColor = Color(0xFFFFFFFF),
                        disabledContainerColor = Color(0xAAFFFFFF),
                        disabledContentColor = Color(0xAAFFFFFF)
                    ),
                    onClick = {


                        if(brainValue<=0 && selectedBoard != "3x4"){
                            Toast.makeText(context, "Brains coins insufficient!!", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            if(selectedBoard != "3x4"){
                                brainViewModel.updateBrains(-1)

                            }
                            val (cardsRow, cardsColumn) = selectedBoard.split("x").map { it.toInt() }
                            navController.navigate("game/$cardsRow/$cardsColumn")
                        }
                        },
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
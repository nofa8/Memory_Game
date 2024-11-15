package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import pt.ipleiria.estg.dei.ei.taes.memorygame.R
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBackground
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBottomBackground
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorGameSection
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorImageBlueShadow


//Todo: Tab do Jogo DropDown de Tabelas, Escolha de cartas?, Jogar

@Composable
fun GameTab(
    modifier: Modifier,
    text: String,
) {
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
            BoardDropdown()
            Row(
                modifier = Modifier.padding(horizontal = 90.dp, vertical = 16.dp),
            ) {

                Button(
                    onClick = {},
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
package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import pt.ipleiria.estg.dei.ei.taes.memorygame.R
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBrainButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBrainCoinsNumber
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorSurface
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorTextPrimary
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorWhiteBorder

@Composable
fun ControlButton(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,) {
        Surface(
            color = ColorBrainButton,
            shape = CircleShape,
            modifier = modifier
                .border(
                    width = 1.5.dp,
                    color = ColorSurface,
                    shape = CircleShape
                )
                .size(54.dp)
                .customShadow(
                    color = ColorWhiteBorder,
                    alpha = 0.2f,
                    shadowRadius = 16.dp,
                    borderRadius = 48.dp,
                    offsetY = 4.dp
                ),
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.brain),
                    contentDescription = "Options",
                    modifier = Modifier.size(30.dp)
                )
            }

        }
        BrainCoins(modifier = Modifier.absoluteOffset(y = (-9.5f).dp).zIndex(1f), valueBrain = 2)
    }

}

//Todo: BrainCoins icon com quantidade de brain coins e botão "+" para ir para a página de compra futuramente
@Composable
private fun BrainCoins(
    modifier: Modifier = Modifier,
    valueBrain: Int
) {
    Box(
        modifier = modifier
            .background(color = ColorBrainCoinsNumber,

                shape = RoundedCornerShape(12.dp)

            )
            .width(50.dp)
            .padding(
                vertical = 2.dp,
                horizontal = 10.dp
            )
//            .border(
//                width = 2.dp,
//                brush = Brush.linearGradient(
//                    0f to ColorGradient1,
//                    0.25f to ColorGradient2,
//                    1f to ColorGradient3
//                ),
//                shape = CircleShape
//            )
        ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = valueBrain.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = ColorTextPrimary.copy(alpha = 0.7f),
            fontWeight = FontWeight.SemiBold

        )
    }
}
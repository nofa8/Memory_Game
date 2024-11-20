package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import pt.ipleiria.estg.dei.ei.taes.memorygame.R
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBrainButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBrainCoinsNumber
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorImageBlueShadow
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorImageYellowShadow
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorSurface
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorTextPrimary
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorWhiteBorder

@Composable
fun HintButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,) {
        Surface(
            color = Color.Transparent,
            shape = CircleShape,
            modifier = modifier
                .border(
                    width = 1.5.dp,
                    color = Color.Transparent,
                    shape = CircleShape
                )
                .size(54.dp)

        ) {

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .border(
                        width = 1.5.dp,
                        color = ColorSurface,
                        shape = CircleShape
                    )
                    .customShadow(
                        color = ColorImageYellowShadow,
                        alpha = 0.15f,
                        shadowRadius = 8.dp,
                        borderRadius = 48.dp,
                        offsetY = 3.dp
                    )
                    .clickable(onClick = onClick) // Adiciona o comportamento de clique
                    .then(modifier), // Adiciona modificadores externos passados por argumento
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tips_bulb),
                    contentDescription = "Notifications",
                    modifier = modifier
                        .size(46.dp)
                        .clip(CircleShape)

                )
            }
        }

        BrainCoinInformation(modifier = Modifier.absoluteOffset(y = (-9.5f).dp).zIndex(1f))

    }
}


@Composable
private fun BrainCoinInformation(
    modifier: Modifier = Modifier,
) {
    val hintCust = 1
    Box(
        modifier = modifier
            .background(color = ColorBrainCoinsNumber,

                shape = RoundedCornerShape(12.dp)

            )
            .width(80.dp)
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
            text = "-"+hintCust+" Brain",
            style = MaterialTheme.typography.bodyMedium,
            color = ColorTextPrimary.copy(alpha = 0.7f),
            fontWeight = FontWeight.SemiBold

        )
    }
}
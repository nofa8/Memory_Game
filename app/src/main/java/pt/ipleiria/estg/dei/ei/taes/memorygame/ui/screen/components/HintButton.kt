package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBackground
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBrainCoinsNumber
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorImageYellowShadow
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorSurface
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorTextPrimary

@Composable
fun HintButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    brainViewModel: BrainViewModel
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
                .size(60.dp)

        ) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .border(
                        width = 1.5.dp,
                        color = ColorSurface,
                        shape = CircleShape
                    )
                    .customShadow(
                        color = ColorImageYellowShadow,
                        alpha = 0.55f,
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
                        .size(50.dp)
                        .clip(CircleShape)

                )
            }
        }

        BrainCoinInformation(brainViewModel = brainViewModel, modifier = Modifier.absoluteOffset(y = (-6.5f).dp).zIndex(1f))

    }
}


@Composable
private fun BrainCoinInformation(
    modifier: Modifier = Modifier,
    brainViewModel: BrainViewModel
) {

    Row(
        modifier = modifier
            .background(
                color = ColorBrainCoinsNumber,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 6.dp, vertical = 2.5f.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = brainViewModel.getBrainValue().toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )
            )
        }
        Box(
            modifier = Modifier
                .background(
                    color = ColorBrainCoinsNumber,
                    shape = CircleShape
                )
//                .padding(right)
        ) {
            Image(
                painter = painterResource(id = R.drawable.brain),
                contentDescription = "BrainCoins",
                modifier = Modifier.size(24.dp)
            )
        }
    }


}
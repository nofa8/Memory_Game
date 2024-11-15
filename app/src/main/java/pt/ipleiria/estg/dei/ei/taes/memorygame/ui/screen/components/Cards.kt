package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.ipleiria.estg.dei.ei.taes.memorygame.R

@Composable
fun Cards(
    modifier: Modifier = Modifier,
    widthCard: Dp
) {
    Image(
        painter = painterResource(id = R.drawable.card_blue),
        contentDescription = "Card",
        modifier = modifier.width(widthCard).padding(all = 5.dp)
    )
}
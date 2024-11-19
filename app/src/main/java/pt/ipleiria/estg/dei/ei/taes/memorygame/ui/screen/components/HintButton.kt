package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pt.ipleiria.estg.dei.ei.taes.memorygame.R
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorImageBlueShadow
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorImageYellowShadow
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorSurface

@Composable
fun HintButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
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
package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import pt.ipleiria.estg.dei.ei.taes.memorygame.R
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBottomBackground

//Todo: ActionBar de baixo, Jogo, Perfil, Classificações
@Composable
fun BottomActionBar(
    modifier: Modifier = Modifier,
    selected: Int = 2,
    onScoresClick: () -> Unit,
    onPlayClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
//            .padding(horizontal = 16.dp, vertical = 9.dp),
        color = ColorBottomBackground,
//        shape = RoundedCornerShape(36.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 17.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomIcon(text = "Scores",
                image = R.drawable.table,
                backgroundColor = Color(0xFFE0F7EC),
                modifier = Modifier.weight(1f),
                onClick = onScoresClick)
            BottomIcon(text = "Play",
                image = R.drawable.cards,
                backgroundColor = Color(0xFFFFD7DA),
                modifier = Modifier.weight(1f),
                onClick = onPlayClick)
            BottomIcon(text = "Profile",
                image = R.drawable.account_icon,
                backgroundColor = Color(0xFFE6E6FA),
                modifier = Modifier.weight(1f),
                onClick = onProfileClick)
        }
    }
}


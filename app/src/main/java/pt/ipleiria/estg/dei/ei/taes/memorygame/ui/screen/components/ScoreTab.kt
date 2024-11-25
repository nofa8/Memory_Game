package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class ScoreEntry(
    val name: String,
    val time: String,
    val moves: Int,
    val scores: Int
)


@Composable
fun ScoreTab(
    scores: List<ScoreEntry>,
    modifier: Modifier = Modifier,

    ) {
    Surface(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 9.dp),
        color = Color.Transparent
    ){


        Column(modifier = modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,


                ) {
                Text(text = "Name", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text(text = "Time", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text(text = "Moves", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text(text = "Scores", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Score Entries
            LazyColumn {
                itemsIndexed(scores) { _, entry ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text(text = entry.name, modifier = Modifier.weight(1f))
                        Text(text = entry.time, modifier = Modifier.weight(1f))
                        Text(text = entry.moves.toString(), modifier = Modifier.weight(1f))
                        Text(text = entry.scores.toString(), modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
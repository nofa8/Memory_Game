package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.BoardData
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.ScoreEntry
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.calculateScore


@Composable
fun ScoreTab(
    scores: List<ScoreEntry>,
    modifier: Modifier = Modifier
) {
    val lightGrayBackground = Color(0xFFF8F9FA)
    val softBlueHeader = Color(0xFFE9ECF3)
    val darkGrayText = Color(0xFF333333)
    val softGrayText = Color(0xFF666666)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp)),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(softBlueHeader)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScoreHeaderCell(
                    text = "Name",
                    modifier = Modifier.weight(1.2f),
                    color = darkGrayText
                )
                ScoreHeaderCell(
                    text = "Time",
                    modifier = Modifier.weight(1f),
                    color = darkGrayText
                )
                ScoreHeaderCell(
                    text = "Moves",
                    modifier = Modifier.weight(0.8f),
                    color = darkGrayText
                )
                ScoreHeaderCell(
                    text = "Score",
                    modifier = Modifier.weight(0.8f),
                    color = darkGrayText
                )
                ScoreHeaderCell(
                    text = "Board",
                    modifier = Modifier.weight(1f),
                    color = darkGrayText
                )
            }

            // Score Entries
            LazyColumn {
                itemsIndexed(scores) { index, entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (index % 2 == 0) Color.White
                                else lightGrayBackground
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ScoreCell(
                            text = entry.name,
                            modifier = Modifier.weight(1.2f),
                            color = darkGrayText,
                            fontWeight = FontWeight.SemiBold
                        )
                        ScoreCell(
                            text = entry.total_time.toString(),
                            modifier = Modifier.weight(1f),
                            color = softGrayText
                        )
                        ScoreCell(
                            text = entry.turns.toString(),
                            modifier = Modifier.weight(0.8f),
                            color = softGrayText
                        )
                        ScoreCell(
                            text =calculateScore(moves = entry.turns, timeSec =  entry.total_time.toInt() ).toString(),
                            modifier = Modifier.weight(0.8f),
                            color = softGrayText
                        )
                        ScoreCell(
                            text = BoardData.boards[entry.board-1].cols.toString()+"x"+BoardData.boards[entry.board-1].rows.toString(),
                            modifier = Modifier.weight(1f),
                            color = softGrayText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreHeaderCell(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = color,
        textAlign = TextAlign.Start,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun ScoreCell(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = fontWeight,
        color = color,
        textAlign = TextAlign.Start,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}
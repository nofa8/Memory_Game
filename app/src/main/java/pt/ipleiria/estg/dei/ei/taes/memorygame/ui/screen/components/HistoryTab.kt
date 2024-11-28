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
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.ScoreEntry
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HistoryTab(
    scores: List<ScoreEntry>,
    modifier: Modifier = Modifier
) {
    val lightGrayBackground = Color(0xFFF8F9FA)
    val softBlueHeader = Color(0xFFE9ECF3)
    val darkGrayText = Color(0xFF333333)
    val softGrayText = Color(0xFF666666)

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val sortedScores = scores.sortedByDescending { it.date }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp)),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(softBlueHeader)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HistoryHeaderCell(
                    text = "Date",
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = 8.dp),
                    color = darkGrayText
                )
                HistoryHeaderCell(
                    text = "Time",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp, end = 8.dp),
                    color = darkGrayText
                )
                HistoryHeaderCell(
                    text = "Moves",
                    modifier = Modifier
                        .weight(1.2f)
                        .padding(start = 8.dp),
                    color = darkGrayText
                )
                HistoryHeaderCell(
                    text = "Score",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    color = darkGrayText
                )
                HistoryHeaderCell(
                    text = "Board",
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(start = 8.dp),
                    color = darkGrayText
                )
            }

            LazyColumn {
                itemsIndexed(sortedScores) { index, entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (index % 2 == 0) Color.White
                                else lightGrayBackground
                            )
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HistoryCell(
                            text = dateFormat.format(entry.date),
                            modifier = Modifier
                                .weight(2f)
                                .padding(end = 8.dp),
                            color = softGrayText
                        )
                        HistoryCell(
                            text = entry.time,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp, end = 8.dp),
                            color = softGrayText
                        )
                        HistoryCell(
                            text = entry.moves.toString(),
                            modifier = Modifier
                                .weight(1.2f)
                                .padding(start = 8.dp),
                            color = softGrayText
                        )
                        HistoryCell(
                            text = entry.scores.toString(),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            color = softGrayText
                        )
                        HistoryCell(
                            text = entry.boardSize,
                            modifier = Modifier
                                .weight(0.8f)
                                .padding(start = 8.dp),
                            color = softGrayText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryHeaderCell(
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
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun HistoryCell(
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
        textAlign = TextAlign.Center,
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Clip
    )
}
package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.ipleiria.estg.dei.ei.taes.memorygame.R
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.AppData
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.CardFile
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.ControlButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.HintButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.TopActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBackground
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.floor


@Composable
fun GameScreen(cardsRow: Int, cardsColumn: Int) {

    val cardCount = cardsColumn * cardsRow
    val cards = remember { AppData.getPairsOfCards(cardCount / 2) }
    val flippedCards = remember { mutableStateListOf<Int>() }
    val matchedCards = remember { mutableStateListOf<Int>() }
    val moves = remember { mutableIntStateOf(0) }
    var isClickable by remember { mutableStateOf(true) } // Track whether the game is clickable
    var elapsedTime by remember { mutableStateOf(0) } // Track elapsed time
    var isGameOver by remember { mutableStateOf(false) } // Track if the game is over

    // Timer for elapsed time
    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (true) {
                delay(1000) // Update every second
                elapsedTime++
            }
        }
    }

    fun onCardClick(index: Int) {
        if (!isClickable) return // If not clickable, ignore the click

        if (flippedCards.size < 2 && index !in flippedCards && index !in matchedCards) {
            flippedCards.add(index)
        }

        if (flippedCards.size == 2) {
            moves.intValue++
            val firstCard = cards[flippedCards[0]]
            val secondCard = cards[flippedCards[1]]

            if (firstCard == secondCard) {
                matchedCards.addAll(flippedCards)
            }

            // Set isClickable to false to prevent further clicks
            isClickable = false

            // Delay to allow the user to see the flipped cards before resetting
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000) // 1 second delay
                flippedCards.clear()

                // After the delay, make cards clickable again
                isClickable = true
            }

            // Check if all cards are matched
            if (matchedCards.size == cardCount) {
                isGameOver = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ColorBackground
    ) { paddings ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            TopActionBar(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 10.dp)
                    .padding(paddings),
                leftFunction = { ControlButton() },
                rightFunction = { HintButton() }
            )

            // Display Elapsed Time
            Text(
                text = "Time: ${if (elapsedTime > 60) elapsedTime/60 else "00"}:${if (elapsedTime%60 < 10) "0"+elapsedTime%60 else elapsedTime%60}",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(16.dp)
            )

            // Card Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(cardsRow),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(cards.size) { index ->
                    val cardValue = cards[index]
                    val isFlipped = index in flippedCards || index in matchedCards

                    val rotation by animateFloatAsState(
                        targetValue = if (isFlipped) 180f else 0f,
                        animationSpec = tween(durationMillis = 400)
                    )

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth()
                            .clickable(enabled = isClickable) { onCardClick(index) } // Only clickable when isClickable is true
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 8 * density
                                scaleX =
                                    if (rotation < 90f) 1f else -1f // Flip the image horizontally when card is flipped
                            }
                    ) {
                        if (rotation < 90f) {
                            // Back of the card
                            Image(
                                painter = painterResource(id = R.drawable.card_blue),
                                contentDescription = "Card Back",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            // Front of the card
                            Image(
                                painter = painterResource(id = getCardImageResource(cardValue)),
                                contentDescription = "Card Front",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
            // Display Nº of Moves
            Text(
                text = "Moves: ${moves.intValue}",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(paddings)
            )
        }
    }
}






//// Helper function to return the correct resource ID based on card value
fun getCardImageResource(annotation: String): Int {
    val file = CardFile.fromValue(annotation)
    if (file == null){
        return R.drawable.card_blue
    }
    return file.drawableRes
}


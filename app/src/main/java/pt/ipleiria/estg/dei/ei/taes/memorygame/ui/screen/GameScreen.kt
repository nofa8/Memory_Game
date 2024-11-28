package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen

import BrainViewModel
import android.widget.Toast
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
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.CardControl
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.CardFile
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.HintButton
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.TopActionBar
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorBackground
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.PopUpEndGame
import androidx.compose.ui.platform.LocalContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BackButton

//

@Composable
fun GameScreen(cardsRow: Int, cardsColumn: Int, brainViewModel: BrainViewModel, navController: NavController) {

    val cardCount = cardsColumn * cardsRow
    val cards = remember { CardControl.getPairsOfCards(cardCount / 2) }
    val flippedCards = remember { mutableStateListOf<Int>() }
    val matchedCards = remember { mutableStateListOf<Int>() }
    val moves = remember { mutableIntStateOf(0) }
    var isClickable by remember { mutableStateOf(true) } // Track whether the game is clickable
    var elapsedTime by remember { mutableStateOf(0) } // Track elapsed time
    var isGameOver by remember { mutableStateOf(false) } // Track if the game is over
    var popUpAlreadyOpened by remember { mutableStateOf(false) } // Track if the game is over

    var hintIndices: List<Int>? by remember { mutableStateOf(null) } // Estado de indices das cartas reveladas

    // Timer for elapsed time
    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (true) {
                delay(1000) // Update every second
                elapsedTime++
            }
        }
        else{
            popUpAlreadyOpened = true;
        }

    }
    val context = LocalContext.current

    if (popUpAlreadyOpened) {
        // Quando o jogo terminar, o estado isGameOver é verdadeiro e mostramos o popup
        PopUpEndGame(
            time = "${if (elapsedTime > 60) elapsedTime/60 else "00"}:${if (elapsedTime%60 < 10) "0"+elapsedTime%60 else elapsedTime%60}",
            moves = moves.intValue,
            score = calculateScore(elapsedTime, moves.intValue),
            onTryAgain = {
                // Navegar novamente para "game/{cardsRow}/{cardsColumn}"
                if(brainViewModel.getBrainValue() > 0 || cardCount == 12){
                    if(cardCount != 12){
                        brainViewModel.updateBrains(-1)
                    }
                    navController.navigate("game/$cardsRow/$cardsColumn") {
                        popUpTo("game/$cardsRow/$cardsColumn") { inclusive = true }
                        // Remove a instância anterior de "game" da pilha para garantir reinício
                    }
                }
                else{
                    Toast.makeText(context, "Brains coins insufficient!!", Toast.LENGTH_SHORT).show()
                    navController.navigate("dashboard")
                }

            },
            onConfirm = {
                // Navegar para o dashboard
                navController.navigate("dashboard")
            },
            quitFunction = {
                popUpAlreadyOpened = false;
            }
        )
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

                if(matchedCards.size == cardCount){
                    isGameOver = true

                }
                flippedCards.clear()

            }else{
                // Set isClickable to false to prevent further clicks
                isClickable = false
                // Delay to allow the user to see the flipped cards before resetting
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1000) // 1 second delay
                    flippedCards.clear()

                    // After the delay, make cards clickable again
                    isClickable = true
                }
            }

        }
    }

    fun revealHint() {
        // Verificar se já existe uma carta revelada
        if(brainViewModel.getBrainValue()<=0){
                Toast.makeText(context, "Insufficient Brains coins!!", Toast.LENGTH_SHORT).show()
                return
        }
        if( isGameOver == true || isClickable == false){
            return
        }
        brainViewModel.updateBrains(-1)
        if (flippedCards.size == 1) {
            // Há uma carta revelada, então procurar pelo par correspondente
            val firstRevealedIndex = flippedCards.first() // O índice da carta revelada
            val firstRevealedCardValue = cards[firstRevealedIndex] // Valor da carta revelada

            // Procurar a carta correspondente no restante das cartas
            val matchingPair = cards.withIndex()
                .filter { it.value == firstRevealedCardValue && it.index !in flippedCards } // Filtra o par correspondente
                .map { it.index }

            // Se encontrar o par correspondente, revela-se as cartas
            matchingPair.firstOrNull()?.let { matchingIndex ->
//                flippedCards.add(matchingIndex)
//                matchedCards.addAll(listOf(firstRevealedIndex, matchingIndex)) // Marca as duas como combinadas
                onCardClick(matchingIndex)
            }
        } else {
            // Se nenhuma carta foi revelada (flippedCards vazio), revela um par de cartas que ainda não foram combinadas
            val unmatchedPairs = cards.withIndex()
                .groupBy { it.value }
                .values
                .filter { pair ->
                    pair.all { it.index !in matchedCards && it.index !in flippedCards } // Apenas cartas não combinadas e não reveladas
                }
                .firstOrNull() // Escolhe o primeiro par válido (se houver)

            unmatchedPairs?.let {
                val indicesToReveal = it.map { it.index }
                for( index in indicesToReveal){
                    onCardClick(index)
                }
            }
        }
        hintIndices?.let {
            hintIndices = null // Reseta o estado das cartas reveladas
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
                leftFunction = { BackButton(onConfirmExit =  {
                    navController.navigate("dashboard")
                }
                ) },
                rightFunction = {
                    HintButton(
                        brainViewModel = brainViewModel,
                        onClick = { revealHint() }
                    )
                }

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
                                contentDescription = "Card "+index,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            // Front of the card
                            Image(
                                painter = painterResource(id = getCardImageResource(cardValue)),
                                contentDescription = "Card "+ index + " " +cardValue,
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

fun calculateScore(timeSec: Int, moves: Int): Int {
    val baseScore = 1000.0 // Pontuação inicial
    val tempoBase = 60.0 // Tempo base (1 minuto)
    val jogadasBase = 20.0 // Jogadas base (20 jogadas)
    val ponderacaoTempo = 1.5 // Peso do tempo
    val ponderacaoJogadas = 2.0 // Peso das jogadas

    // Fórmula de cálculo
    val divisor = 1 + (timeSec / tempoBase * ponderacaoTempo) + (moves / jogadasBase * ponderacaoJogadas)
    val score = baseScore / divisor

    // Retorna o score arredondado para inteiro
    return score.toInt()
}


//// Helper function to return the correct resource ID based on card value
fun getCardImageResource(annotation: String): Int {
    val file = CardFile.fromValue(annotation)
    if (file == null){
        return R.drawable.card_blue
    }
    return file.drawableRes
}


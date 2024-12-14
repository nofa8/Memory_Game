package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen

import BrainViewModel
import android.content.Context
import android.util.Log
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
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.withContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.Board
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.BoardData
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.BoardData.boards
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.UserData
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.NotificationHelper
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.WebSocketManager
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.calculateScore
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components.BackButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//

fun getCurrentFormattedTime(time: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return time.format(formatter)
}
fun addSecondsToDateTime(dateTime: LocalDateTime, secondsToAdd: Long): String {
    val updatedDateTime = dateTime.plusSeconds(secondsToAdd) // Add seconds
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return updatedDateTime.format(formatter)
}



data class GameResult(
    val type: String,
    val status: String,
    val total_time: Int?,
    val created_user_id: Int,
    val winner_user_id: Int?,
    val began_at: String,
    val ended_at: String?,
    val board_id: Int,
    val total_turns_winner: Int?
)


suspend fun postGameResult(gameResult: GameResult?, current: Context): Boolean {
    if (gameResult == null) {
        Log.e("API Error", "Game result is null.")
        return false
    }

    val apiUrl = "${API.url}/gamesTAES"
    return try {
        // Perform network call on IO thread
        val response = withContext(Dispatchers.IO) {
            API.callApi(
                apiUrl = apiUrl,
                httpMethod = "POST",
                requestModel = gameResult
            )
        }

        // Parse the response safely
        val jsonResponse = Gson().fromJson(response, JsonObject::class.java)

        // Debugging output for the response
        Log.d("API Response", "Response JSON: $jsonResponse")

        // Safely access specific fields with null checks
        val isPersonalTopTime = jsonResponse.get("is_personal_top_time")?.asBoolean == true
        val isPersonalTopTurn = jsonResponse.get("is_personal_top_turns")?.asBoolean == true
        val isGlobalTopTurn = jsonResponse.get("is_top_turns")?.asBoolean == true
        val isGlobalTopTime = jsonResponse.get("is_top_time")?.asBoolean == true

        // WebSocket connection handling
        val webSocket = WebSocketManager

        if (isPersonalTopTime){
            NotificationHelper.showNotification(
                context = current,  // Pass context of your activity or application
                title = "Personal Top Time",
                message = "You got a new Personal Top Turns in ${BoardData.boards[gameResult.board_id-1].cols}x${BoardData.boards[gameResult.board_id-1].rows}"
            )
        }
        if(isPersonalTopTurn){
            NotificationHelper.showNotification(
                context = current,  // Pass context of your activity or application
                title = "Personal Top Turns",
                message = "You got a new Personal Top Turns in ${BoardData.boards[gameResult.board_id-1].cols}x${BoardData.boards[gameResult.board_id-1].rows}"
            )
        }

        // Handle global top turn and time announcements
        if (isGlobalTopTurn) {
            val user = UserData.user
            webSocket.emitBroadcast("${user!!.nickname} has gotten into the Global Top Turns in ${BoardData.boards[gameResult.board_id-1].cols}x${BoardData.boards[gameResult.board_id-1].rows}")
        }

        if (isGlobalTopTime) {
            val user = UserData.user
            webSocket.emitBroadcast("${user!!.nickname} has gotten into the Global Top Time in ${BoardData.boards[gameResult.board_id-1].cols}x${BoardData.boards[gameResult.board_id-1].rows}")
        }

        // Log for further debugging
        Log.d("API Response", "Personal Top Time: $isPersonalTopTime, Personal Top Turns: $isPersonalTopTurn")
        Log.d("API Response", "Global Top Time: $isGlobalTopTime, Global Top Turns: $isGlobalTopTurn")

        // Return whether the "is_personal_top_time" key exists
        jsonResponse.has("is_personal_top_time")
    } catch (e: Exception) {
        // Enhanced error logging
        Log.e("API Error", "Failed to post game result: ${e.message}", e)
        false
    }
}


@Composable
fun GameScreen(cardsRow: Int, cardsColumn: Int, brainViewModel: BrainViewModel, navController: NavController) {
    val board: Board? = boards.find { it.cols == cardsColumn && it.rows == cardsRow }
    val cardCount = cardsColumn * cardsRow
    val cards = remember { CardControl.getPairsOfCards(cardCount / 2) }
    val flippedCards = remember { mutableStateListOf<Int>() }
    val matchedCards = remember { mutableStateListOf<Int>() }
    val moves = remember { mutableIntStateOf(0) }
    var isClickable by remember { mutableStateOf(true) } // Track whether the game is clickable
    var elapsedTime by remember { mutableStateOf(0) } // Track elapsed time
    var isGameOver by remember { mutableStateOf(false) } // Track if the game is over
    var popUpAlreadyOpened by remember { mutableStateOf(false) } // Track if the game is over
    val current_time = LocalDateTime.now()
    var hintIndices: List<Int>? by remember { mutableStateOf(null) } // Estado de indices das cartas reveladas

    var gameResult by remember { mutableStateOf<GameResult?>(null) }


    val current = LocalContext.current

    // Timer for elapsed time
    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (true) {
                delay(1000) // Update every second
                elapsedTime++
            }
        }
        else{
            gameResult = GameResult(
                type = "S",
                status = "E", // "E" for Ended -> Only store the ones that end
                total_time = elapsedTime, // in seconds
                created_user_id = 1,
                winner_user_id = null,
                began_at = getCurrentFormattedTime(current_time),
                ended_at = addSecondsToDateTime(current_time,  elapsedTime.toLong()),
                board_id = board?.id ?: 1 , // Not supposed to have a null board, if so default board
                total_turns_winner = moves.intValue
            )

            popUpAlreadyOpened = true;

            postGameResult(gameResult, current)
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
                    if (API.token.isNotBlank()){
                        HintButton(
                            brainViewModel = brainViewModel,
                            onClick = { revealHint() }
                        )
                    }else{
                        HintButton(
                            brainViewModel = brainViewModel,
                            onClick = {  }
                        )
                    }
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


//// Helper function to return the correct resource ID based on card value
fun getCardImageResource(annotation: String): Int {
    val file = CardFile.fromValue(annotation)
    if (file == null){
        return R.drawable.card_blue
    }
    return file.drawableRes
}




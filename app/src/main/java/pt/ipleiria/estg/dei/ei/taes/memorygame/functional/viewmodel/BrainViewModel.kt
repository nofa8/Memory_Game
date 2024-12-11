
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.UserData

data class BrainUiState(
    var brainValue: Int = if (UserData.user  != null) {
        UserData.user!!.brain_coins_balance
    }else{
        0
    }
)

class BrainViewModel : ViewModel() {

    // Gerencia o estado da interface
    private val _uiState = MutableStateFlow(BrainUiState())
    val uiState: StateFlow<BrainUiState> = _uiState.asStateFlow()

    // Função para obter o valor atual
    fun getBrainValue(): Int = _uiState.value.brainValue

    // Função para atualizar o valor do brain
    fun updateBrains(amount: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                brainValue = currentState.brainValue + amount
            )
        }
    }
}


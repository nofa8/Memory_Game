
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BrainUiState(
    val brainValue: Int = 0 // Valor inicial
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


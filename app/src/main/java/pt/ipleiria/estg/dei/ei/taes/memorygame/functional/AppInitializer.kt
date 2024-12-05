package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppInitializer {
    companion object {
        fun initializeApp() {
            CoroutineScope(Dispatchers.IO).launch {
                val success = BoardData.fetchBoards()

            }
        }
    }
}

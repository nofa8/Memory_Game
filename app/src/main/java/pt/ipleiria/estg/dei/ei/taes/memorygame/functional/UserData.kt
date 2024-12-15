package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.rememberTextMeasurer
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val type: String,
    val nickname: String,
    val photoFileName: String?,
    val brain_coins_balance: Int
)

object UserData {
    private val apiUrl = "${API.url}/users/me"
    var brainViewModel: BrainViewModel? = null
    // Mutable user property. Public setter allows you to change it from outside the object.
    var user: MutableState<User?> = mutableStateOf(null)
        private set

    /**
     * Fetch the user data from the API.
     * @return Boolean - Returns true if successful, false if failed.
     */
    suspend fun fetchUser(): Boolean {
        return try {
            // Fetch the data using the API
            val jsonResponse = withContext(Dispatchers.IO) {
                API.callApi(apiUrl = apiUrl, httpMethod = "GET")
            }

            // Parse the JSON response to extract the user data
            val jsonObject = Gson().fromJson(jsonResponse, JsonObject::class.java)
            val dataObject = jsonObject.getAsJsonObject("data")

            // Convert the JSON object into a User object
            user.value = Gson().fromJson(dataObject, User::class.java)

            true // Return true on success
        } catch (e: Exception) {
            e.printStackTrace()
            user.value = null // Reset to null on failure
            false // Return false on failure
        }
    }

    /**
     * Optionally, you can create a method to allow modifying the user from outside, if needed.
     */
    fun updateUser(newUser: User?) {
        val oldUser = user.value // Current user state

        user.value = newUser // Update user state
        if (oldUser == null){
            return
        }
        newUser?.brain_coins_balance?.let { newBalance ->
            val oldBalance = oldUser.brain_coins_balance
            if (newBalance != oldBalance) {
                brainViewModel?.updateBrains(newBalance - oldBalance)
            }
        }
    }

}

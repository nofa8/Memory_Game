package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val type: String,
    val nickname: String,
    val photoFileName: String,
    val brain_coins_balance: Int,
)

object UserData {
    private val apiUrl = "${API.url}/users/me"
    var user: User? = null
        private set



    suspend fun fetchUser(): Boolean {
        return try {
            // Fetch the data using the API
            val jsonResponse = withContext(Dispatchers.IO) {
                API.callApi(apiUrl = apiUrl, httpMethod = "GET")
            }

            // Parse the JSON response
            val jsonObject = Gson().fromJson(jsonResponse, JsonObject::class.java)
            val dataObject = jsonObject.getAsJsonObject("data")

            // Convert JSON array into a list of Board objects
            user = Gson().fromJson(dataObject, User::class.java)


            true // Return true on success
        } catch (e: Exception) {
            e.printStackTrace()
            user = null // Reset to an empty list on failure
            false // Return false on failure
        }
    }
}
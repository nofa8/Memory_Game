package pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.JsonObject
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.User
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.UserData
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

class API private constructor(context: Context) {
    companion object {
        private var instance: API? = null
        private const val PREFS_NAME = "auth_preferences"
        private const val TOKEN_KEY = "auth_token"

        // The token is stored in memory (as a singleton)
        var token: String = ""
            private set
//        var url: String = "http://10.0.2.2:8085/api"
        var url:String = "http://api-dad-group-9-172.22.21.101.sslip.io/api"
        var TOKEN_VALIDATION_ENDPOINT = "$url/users/me"


        // Access the singleton API instance and load the token
        fun getInstance(context: Context): API {
            if (instance == null) {
                instance = API(context.applicationContext)
                instance?.loadToken()  // Load token only once when the instance is created
            }
            return instance!!
        }

        fun callApi(apiUrl: String, httpMethod: String, requestModel: Any? = null): String {
            val response = StringBuilder()

            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = httpMethod
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Accept", "application/json")
                if (token.isNotBlank()) {
                    connection.setRequestProperty("Authorization", "Bearer $token")
                }

                if (httpMethod == "POST" || httpMethod == "PUT") {
                    connection.doOutput = true
                    requestModel?.let {
                        val jsonInput = Gson().toJson(it)
                        OutputStreamWriter(connection.outputStream).use { os ->
                            os.write(jsonInput)
                            os.flush()
                        }
                    }
                }

                val responseCode = connection.responseCode
                val charset = connection.contentType?.substringAfter("charset=", "utf-8") ?: "utf-8"


                if (responseCode in 200..299) {
                    connection.inputStream.use { inputStream ->
                        val responseBytes = inputStream.readBytes()  // Read the entire response as bytes
                        response.append(String(responseBytes, Charset.defaultCharset()))  // Convert bytes to string using the charset
                    }
                }
                else {
                    BufferedReader(InputStreamReader(connection.errorStream ?: connection.inputStream, charset)).use { br ->
                        var responseLine: String?
                        while (br.readLine().also { responseLine = it } != null) {
                            response.append(responseLine?.trim())
                        }
                    }
                    println("Error Response Code: $responseCode, Message: ${connection.responseMessage}")
                }
            } catch (e: MalformedURLException) {
                println("Invalid URL: $apiUrl")
                e.printStackTrace()
                return "Invalid URL"
            } catch (e: IOException) {
                println("Network Error: ${e.message}")
                e.printStackTrace()
                return "Network Error"
            } catch (e: Exception) {
                println("Unexpected Error: ${e.message}")
                e.printStackTrace()
                return "Unexpected Error"
            }
            var cena = response
            var imbalance = areBracesBalanced(cena.toString())
            if (imbalance != 0 && imbalance != -1){
                while (imbalance != 0){
                    imbalance--
                    cena.append("}")
                }
            }
            println("Response: $cena")
            return  cena.toString()
        }


        // Helper function to check if the braces are balanced
        private fun areBracesBalanced(json: String): Int {
            var braceCount = 0
            for (char in json) {
                when (char) {
                    '{' -> braceCount++
                    '}' -> braceCount--
                }

                // If at any point braces are imbalanced, return false
                if (braceCount < 0) return -1
            }
            // Ensure all opening braces have a corresponding closing brace
            return braceCount
        }
    }



    private val encryptedPreferences by lazy {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            PREFS_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // Function to load token from EncryptedSharedPreferences once
    fun loadToken() {
        if (token.isBlank()) {
            token = encryptedPreferences.getString(TOKEN_KEY, "").toString()
        }
    }
    // Function to clear token from EncryptedSharedPreferences
    fun clearToken() {
        encryptedPreferences.edit().remove(TOKEN_KEY).apply()  // Remove the token from preferences
        token = ""  // Clear the token in memory
        UserData.updateUser(null)
    }

    fun fetchUserData(): User? {
        return try {
            val jsonResponse = callApi(apiUrl = TOKEN_VALIDATION_ENDPOINT, httpMethod = "GET")

            val jsonObject = Gson().fromJson(jsonResponse, JsonObject::class.java)
            val data = jsonObject.getAsJsonObject("data")
            Gson().fromJson(data, User::class.java) // Parse response into User object
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null if the token is invalid or the request fails
        }
    }

    // Function to save token
    fun saveToken(tokener: String) {
        encryptedPreferences.edit().putString(TOKEN_KEY, tokener).apply()
        token = tokener
    }


}

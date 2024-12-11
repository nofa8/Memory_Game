package pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.JsonObject
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.User
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class API private constructor(context: Context) {
    companion object {
        private var instance: API? = null
        private const val PREFS_NAME = "auth_preferences"
        private const val TOKEN_KEY = "auth_token"

        // The token is stored in memory (as a singleton)
        var token: String = ""
            private set
        public var url: String = "http://10.0.2.2:8085/api"
        //"http://api-dad-group-9-172.22.21.101.sslip.io/api"
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

                // Set request headers for JSON format and authorization
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Accept", "application/json")
                if (token.isNotBlank()) {
                    connection.setRequestProperty("Authorization", "Bearer $token")
                }

                // Send request body for POST/PUT methods
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

                // Handle the response
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    BufferedReader(InputStreamReader(connection.inputStream, "utf-8")).use { br ->
                        var responseLine: String?
                        while (br.readLine().also { responseLine = it } != null) {
                            response.append(responseLine?.trim())
                        }
                    }
                } else {
                    // Handle error response
                    BufferedReader(InputStreamReader(connection.errorStream, "utf-8")).use { br ->
                        var responseLine: String?
                        while (br.readLine().also { responseLine = it } != null) {
                            response.append(responseLine?.trim())
                        }
                    }
                    println("Error Response Code: $responseCode, Message: ${connection.responseMessage}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return e.message.toString()
            }
            println("Response: ${response.toString()}")

            return response.toString()
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

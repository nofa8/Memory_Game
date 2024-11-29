package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API


@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier, start: Boolean = false) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Username TextField
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password TextField
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Text(
                    text = if (isPasswordVisible) "Hide" else "Show",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .clickable { isPasswordVisible = !isPasswordVisible }
                        .padding(8.dp)
                )
            }
        )


        Spacer(modifier = Modifier.height(16.dp))

        // Error Message
        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Login Button
        Button(
            onClick = {
                performLogin(username, password) { success, error ->
                    if (success) {
                        // Navigate to the next screen or show success message
                        navController.navigate("dashboard")
                    } else {
                        errorMessage = error // Display error message
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))
        if (start){
            Button(
                onClick = {
                    API.token = "" // Clear token for anonymous user
                    navController.navigate("dashboard") // Proceed to app without login
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue as Guest")
            }
        }


    }
}

fun performLogin(
    username: String,
    password: String,
    onResult: (success: Boolean, error: String?) -> Unit
) {
    val apiUrl = "${API.url}/login"
    val loginRequest = mapOf("username" to username, "password" to password)

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = API.callApi(
                apiUrl = apiUrl,
                httpMethod = "POST",
                requestModel = loginRequest
            )
            val jsonResponse = Gson().fromJson(response, JsonObject::class.java)
            val token = jsonResponse["token"]?.asString

            if (!token.isNullOrEmpty()) {
                API.token = token // Save token in API class
                withContext(Dispatchers.Main) {
                    onResult(true, null) // Notify success
                }
            } else {
                withContext(Dispatchers.Main) {
                    onResult(false, "Failed to retrieve token")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onResult(false, "Login failed: ${e.message}")
            }
        }
    }
}

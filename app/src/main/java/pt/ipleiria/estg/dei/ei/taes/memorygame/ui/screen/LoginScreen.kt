package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen

import BrainViewModel
import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.NotificationsViewModel
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.UserData
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.WebSocketManager

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    start: Boolean = false,
    brainViewModel: BrainViewModel,
    notificationsViewModel: NotificationsViewModel
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var loginMessage by remember { mutableStateOf<String?>(null) }
    var loginSuccess by remember { mutableStateOf(false) }  // Track login success status
    var context = LocalContext.current


    LaunchedEffect(API.token != "" && UserData.user.value != null) {
        if (UserData.user.value != null && !loginSuccess){
            navController.navigate("dashboard")
        }
    }

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
            label = { Text("Username/Email") },
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
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                Text(
                    text = if (isPasswordVisible) {
                        "Hide"
                    } else {
                        "Show"
                    },
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

        // Login Button with navigation
        Button(
            onClick = {
                errorMessage = ""
                if (username.isEmpty() && password.isEmpty()){
                    errorMessage = "Username and Password must be inputed"
                }else if (username.isEmpty()){
                    errorMessage = "Username must be inputed"
                }else if (!"^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$".toRegex().matches(username)){
                    errorMessage = "Username invalid!"
                }else if (password.isEmpty()){
                    errorMessage = "Password must be inputed"
                }else{
                    performLogin(username, password, context) { success, error ->
                        if (success) {
                            // Show success message and trigger navigation after delay
                            loginMessage = "Login Successful"
                            if (UserData.user.value != null) {
                                if (WebSocketManager.getSocket() == null){
                                    WebSocketManager.initializeSocket()

                                }
                                Log.i("WOW", "Connected to WebSocket server.")

                                WebSocketManager.emitLogin(UserData.user.value!!)
                                Log.i("WOW", "Login emited")

                            }
                            loginSuccess = true
                        } else {
                            errorMessage = error // Display error message
                        }
                    }

                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (start) {
            Button(
                onClick = {

                    navController.navigate("dashboard") // Proceed to app without login
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue as Guest")
            }
        }

        // Show login success message if applicable
        loginMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        // Use LaunchedEffect to navigate after a delay when login is successful
        LaunchedEffect(loginSuccess) {
            if (loginSuccess) {
                delay(1000) // Wait for 1 second

                val userOk = UserData.fetchUser()
                if (userOk){
                    if (UserData.user.value!= null ){
                        brainViewModel.updateBrains(UserData.user.value!!.brain_coins_balance)
                    }
                }
                if (start == false) {
                    navController.navigate("profile")
                } else {
                    navController.navigate("dashboard")
                }
            }
        }
    }
}

fun performLogin(
    username: String,
    password: String,
    context: Context,
    onResult: (success: Boolean, error: String?) -> Unit,

) {
    val apiUrl = "${API.url}/auth/login"
    val loginRequest = mapOf("email" to username, "password" to password)

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
                val api = API.getInstance(context) // Get the API singleton instance
                api.saveToken(token)
                UserData.fetchUser()
                // Save token in API class (for future api related requests that need token for identification)
                withContext(Dispatchers.Main) {
                    onResult(true, null) // Notify success
                }
            } else {
                withContext(Dispatchers.Main) {
                    onResult(false, "Credentials invalid!")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onResult(false, "Failed to connect to the server")
            }
        }
    }
}

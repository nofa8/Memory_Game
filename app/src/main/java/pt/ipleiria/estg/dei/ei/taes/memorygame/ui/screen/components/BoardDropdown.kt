package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components


import android.widget.Toast
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api.API
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorTextPrimary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardDropdown(
    selectedValue: String,
    onOptionSelected: (String) -> Unit
) {
    val options: List<String> = listOf("3x4", "4x4", "6x6")
    var expanded by remember { mutableStateOf(false) }
    val cur = LocalContext.current
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            value = selectedValue,
            onValueChange = {}, // Read-only, so no internal state update here
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            readOnly = true,
            singleLine = true,
            label = { Text("Board") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedIndicatorColor = ColorTextPrimary,
                focusedIndicatorColor = Color.White,
                focusedTextColor = ColorTextPrimary,
                unfocusedTextColor = ColorTextPrimary,
                unfocusedContainerColor = Color(0xFFf5f5dc),
                focusedContainerColor = Color(0xFFf5f5dc),

            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color(0xFFF6ECDC)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF4A4A4A),
                        )
                    },
                    onClick = {

                        if (API.token.isNotEmpty() || option == "3x4"){
                            onOptionSelected(option) // Notify the parent about the selection
                            expanded = false
                        }else{
                            Toast.makeText(
                                cur,
                                "Login needed to access non-3x4 boards",
                                Toast.LENGTH_SHORT
                            ).show()
                            expanded = false
                        }

                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
//                    enabled = API.token.isNotEmpty() || option == "3x4",

                )
            }
        }
    }
}

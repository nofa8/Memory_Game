package pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.theme.ColorTextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBoardExtraDropdown(
    selectedValue: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier
) {
    val options: List<String> = listOf("All","3x4", "4x4", "6x6")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.width(120.dp) // Reduced width
    ) {
        TextField(
            value = selectedValue,
            onValueChange = {}, // Read-only, so no internal state update here
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .width(120.dp), // Reduced width
            readOnly = true,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium, // Smaller text style
            label = {
                Text(
                    "Board",
                    style = MaterialTheme.typography.bodySmall // Smaller label
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                    modifier = Modifier.size(16.dp) // Smaller icon
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedIndicatorColor = ColorTextPrimary,
                focusedIndicatorColor = Color.White,
                focusedTextColor = ColorTextPrimary,
                unfocusedTextColor = ColorTextPrimary,
                unfocusedContainerColor = Color(0xFFf5f5dc),
                focusedContainerColor = Color(0xFFf5f5dc)
            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color(0xFFF6ECDC),
            modifier = Modifier.width(120.dp) // Reduced width
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            style = MaterialTheme.typography.bodyMedium, // Slightly smaller text
                            color = Color(0xFF4A4A4A),
                        )
                    },
                    onClick = {
                        onOptionSelected(option) // Notify the parent about the selection
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    modifier = Modifier.height(40.dp) // Reduced height
                )
            }
        }
    }
}
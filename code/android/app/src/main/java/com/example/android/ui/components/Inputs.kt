import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AppTextField(modifier: Modifier = Modifier, label : String) {
    var value by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier.padding(bottom = 50.dp),
        value = value,
        onValueChange = { newValue ->
            value = newValue
        },
        label = {Text(label) }
    )
}

@Composable
fun PasswordTextField(modifier: Modifier = Modifier, label : String) {
    var value by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    TextField(
        value = value,
        onValueChange = {
            value = it
        },
        label = {Text(label)},
        visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(
                onClick = {
                    showPassword = !showPassword
                }
            ) {
                Icon(
                    imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "Ícone de Visibilidade"
                )
            }
        }
    )
}
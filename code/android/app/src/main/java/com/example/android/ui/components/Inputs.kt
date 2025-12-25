import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
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
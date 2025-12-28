import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

const val GRAY = 0xFFD1D1D1;

@Composable
fun AppBottomNavigationBar(modifier: Modifier = Modifier) {
    val navItemMap = mapOf(
        "Home" to Icons.Default.Home,
        "Explorar" to Icons.Default.Explore,
        "Chat" to Icons.AutoMirrored.Filled.Chat,
        "Adicionar mídia" to Icons.Default.AddCircle
        )

    var selectedKey by remember {
        mutableStateOf("Home")
    }

    NavigationBar(containerColor = Color(GRAY)) {
        for (item in navItemMap) {
            NavigationBarItem(
                selected = selectedKey == item.key,
                onClick = {
                    selectedKey = item.key
                },
                icon = {
                    Icon(imageVector = item.value, contentDescription = item.key)
                },

            )
        }
    }
}








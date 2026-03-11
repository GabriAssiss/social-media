import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

const val GRAY = 0xFFD1D1D1;

@Composable
fun AppBottomNavigationBar(
    modifier: Modifier = Modifier,
    currentRoute: String,
    onBottomNavigateClick: (String) -> Unit = {}
) {
    val navItemMap = mapOf(
        "profile" to Icons.Default.Home,
        "explorar" to Icons.Default.Explore,
        "messages" to Icons.AutoMirrored.Filled.Chat,
        "adicionar mídia" to Icons.Default.AddCircle
    )

    NavigationBar(containerColor = Color(GRAY)) {
        for (item in navItemMap) {
            NavigationBarItem(
                selected = currentRoute == item.key,
                onClick = {
                    onBottomNavigateClick(item.key)
                },
                icon = {
                    Icon(imageVector = item.value, contentDescription = item.key)
                },
            )
        }
    }
}








package com.example.android.ui.view

import AppBottomNavigationBar
import ProfileImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.VideoCameraBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.R
import com.example.android.ui.components.AppTopNavigation


const val GRAY = 0xFFD1D1D1;

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(modifier: Modifier = Modifier) {

    val textFieldState = rememberTextFieldState()
    val nickname = "Nickname"
    val searchResults = remember {
        listOf("Lista", "de", "Resultados", "de", "Pesquisa")
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopNavigation(
                textFieldState = textFieldState,
                onSearch = { query ->
                    println("Buscando por: $query")
                },
                searchResults = searchResults
            )
        },
        bottomBar = { AppBottomNavigationBar() }) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize() ,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(24.dp))
                ProfileImage(image = painterResource(id = R.drawable.default_avatar_icon), modifier = Modifier
                    .size(100.dp)
                   )
                Spacer(modifier = Modifier.height(12.dp))
                Text(nickname)
                Spacer(modifier = Modifier.height(12.dp))
                Bios()
                Row(
                    modifier = Modifier
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Stats(modifier)
                }
                ContentBar()
            }
        }
    }
}

@Composable
fun Bios(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp)
        ) {
        Text("Bios: texto para se colocar na bios, servindo como placeholder")
    }
}

@Composable
fun Stats(modifier: Modifier = Modifier) {
    val stats = listOf("seguindo", "seguidores", "posts")

    for (stat in stats) {
        Stat(modifier, stat, "0")
    }
}

@Composable
fun Stat(modifier: Modifier = Modifier, status : String, value : String) {
    Column(
        modifier = Modifier.padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(status)
        Text(value)
    }
}

@Composable
fun ContentBar(modifier: Modifier = Modifier) {
    val tabs = listOf("Fotos", "Vídeos")
    val icons = listOf(Icons.Default.Photo, Icons.Default.VideoCameraBack)

    var selectedIndex by remember { mutableIntStateOf(0) }

    SecondaryTabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color(GRAY),
        divider = {}
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                text = { Text(text = title) },
                icon = {
                    Icon(imageVector = icons[index], contentDescription = null)
                }
            )
        }
    }
}

@Preview
@Composable
fun ProfileViewPreview() {
    ProfileView()
}
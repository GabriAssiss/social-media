package com.example.android.ui.view

import AppBottomNavigationBar
import ProfileImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.VideoCameraBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.R
import com.example.android.ui.components.AppTopNavigation
import com.example.android.ui.viewmodel.ProfileUiState
import com.example.android.ui.viewmodel.UserSearchUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState,
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val textFieldState = rememberTextFieldState()
    val nickname = uiState.name ?: stringResource(R.string.profile_name_placeholder)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppTopNavigation(
                textFieldState = textFieldState,
                onQueryChange = {},
                searchState = UserSearchUiState.Idle,
                showSearch = false,
                onUserSelected = {},
                onLogout = onLogout
            )
        },
        bottomBar = {
            AppBottomNavigationBar(
                currentRoute = "profile",
                onBottomNavigateClick = { route -> onNavigate(route) }
            )
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                ProfileImage(
                    image = painterResource(id = R.drawable.default_avatar_icon),
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(nickname)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Stats(modifier, uiState)
                }
                Box(
                    modifier = Modifier.padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Bios(modifier, uiState)
                }
                Spacer(modifier = Modifier.height(12.dp))
                ContentBar(modifier, uiState)
            }
        }
    }
}

@Composable
fun Bios(modifier: Modifier = Modifier, uiState: ProfileUiState) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(uiState.bio ?: stringResource(R.string.bio_placeholder))
    }
}

@Composable
fun Stats(modifier: Modifier = Modifier, uiState: ProfileUiState) {

    Row(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.following))
            Text(uiState.followedCount.toString())
        }

        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.followers))
            Text(uiState.followersCount.toString())
        }

        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.posts))
            Text(uiState.postsCount.toString())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentBar(modifier: Modifier = Modifier, uiState: ProfileUiState) {
    val tabs = listOf(stringResource(R.string.photos), stringResource(R.string.videos))
    val icons = listOf(Icons.Default.Photo, Icons.Default.VideoCameraBack)
    var selectedIndex by remember { mutableIntStateOf(0) }

    Column(modifier = modifier) {
        SecondaryTabRow(
            selectedTabIndex = selectedIndex,
            containerColor = Color(0xFFD1D1D1),
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    text = { Text(text = title) },
                    icon = { Icon(imageVector = icons[index], contentDescription = null) }
                )
            }
        }
        
        when (selectedIndex) {
            0 -> Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) { 
                Text("Grid de Fotos") 
            }
            1 -> Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) { 
                Text("Grid de Vídeos") 
            }
        }
    }
}

@Preview
@Composable
fun ProfileViewPreview() {
    ProfileView(
        uiState = ProfileUiState()
    )
}
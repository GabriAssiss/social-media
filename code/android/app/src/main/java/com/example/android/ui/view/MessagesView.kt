package com.example.android.ui.view

import AppBottomNavigationBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.ui.components.AppTopNavigation
import com.example.android.ui.components.UserCard


@Composable
fun MessageView(modifier: Modifier = Modifier) {

    val textFieldState = rememberTextFieldState()
    val searchResults = remember {
        listOf("Lista", "de", "Resultados", "de", "Pesquisa")
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
            Column {
                for (i in 1..6) {
                    UserCard()
                }
            }
        }
    }
}

@Preview
@Composable
fun MessageViewPreview() {
    MessageView()
}
package com.example.android.ui.view

import AppTextField
import PasswordTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.ui.components.AppOutlinedButton
import com.example.android.ui.viewmodel.AuthUiState
import com.example.android.ui.viewmodel.AuthViewModel


@Composable
fun AuthView(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState.token) {
        uiState.token?.let { onLoginSuccess() }
    }

    AuthViewContent(
        uiState = uiState,
        email = email,
        password = password,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onLoginClick = { viewModel.login(email, password) },
        onRegisterClick = onRegisterClick,
        modifier = modifier
    )
}


@Composable
fun AuthViewContent(
    uiState: AuthUiState,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                uiState.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
                AppTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = "Email ou Celular"
                )
                PasswordTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = "Senha"
                )
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(bottom = 50.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppOutlinedButton(
                    onClick = onLoginClick,
                    label = if (uiState.isLoading) "Carregando..." else "Entrar",
                    enabled = !uiState.isLoading
                )
                AppOutlinedButton(onClick = onRegisterClick, label = "Cadastrar")
                Text("Esqueci a senha...", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}


@Preview
@Composable
fun LoginViewPreview() {
    AuthViewContent(
        uiState = AuthUiState(),
        email = "teste@email.com",
        password = "123456",
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        onRegisterClick = {}
    )
}
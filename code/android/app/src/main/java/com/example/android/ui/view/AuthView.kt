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
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.TextButton
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.example.android.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.ui.components.AppOutlinedButton
import com.example.android.ui.viewmodel.AuthUiState

@Composable
fun AuthView(
    modifier: Modifier = Modifier,
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    uiState.error?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                    AppTextField(
                        value = uiState.email, // Assume we add this to State
                        onValueChange = onEmailChange,
                        label = stringResource(R.string.email_or_phone),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    PasswordTextField(
                        value = uiState.password, // Assume we add to State
                        onValueChange = onPasswordChange,
                        label = stringResource(R.string.password)
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppOutlinedButton(
                        onClick = onLoginClick,
                        label = if (uiState.isLoading) stringResource(R.string.login_loading) else stringResource(R.string.login),
                        enabled = !uiState.isLoading
                    )
                    AppOutlinedButton(onClick = onRegisterClick, label = stringResource(R.string.register))
                    TextButton(onClick = onForgotPasswordClick) {
                        Text(stringResource(R.string.forgot_password), color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AuthViewPreview() {
    AuthView(
        uiState = AuthUiState(email = "teste@email.com", password = "123", isLoading = false),
    )
}
package com.example.android.ui.view

import AppTextField
import PasswordTextField
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.ui.components.AppOutlinedButton
import com.example.android.ui.components.AppReturnButton
import com.example.android.ui.viewmodel.RegisterUiState

@Composable
fun RegisterView(
    modifier: Modifier = Modifier,
    uiState: RegisterUiState,
    onPreviousStepClick: () -> Unit = {},
    onNextStepClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onPhoneChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {}
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {

            AppReturnButton(onClick = onPreviousStepClick)

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Insira as informações da conta",
                    fontSize = 20.sp,
                    modifier = Modifier.width(320.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(top = 60.dp))

                AppTextField(
                    label = "Nome",
                    value = uiState.name,
                    onValueChange = onNameChange
                )
                AppTextField(
                    label = "Email",
                    value = uiState.email,
                    onValueChange = onEmailChange
                )
                AppTextField(
                    label = "Celular",
                    value = uiState.phone,
                    onValueChange = onPhoneChange
                )
                PasswordTextField(
                    label = "Senha",
                    value = uiState.password,
                    onValueChange = onPasswordChange
                )

                Spacer(modifier = Modifier.padding(bottom = 50.dp))
                AppOutlinedButton(
                    onClick = onNextStepClick,
                    label = "Registrar conta"
                )
                Text(
                    "Já tenho uma conta",
                    modifier = Modifier.clickable { onLoginClick() },
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}


@Preview
@Composable
fun RegisterViewPreview() {
    RegisterView(
        uiState = RegisterUiState(),
        onPreviousStepClick = {},
        onNextStepClick = {},
        onLoginClick = {},
        onNameChange = {},
        onEmailChange = {},
        onPhoneChange = {},
        onPasswordChange = {}
    )
}
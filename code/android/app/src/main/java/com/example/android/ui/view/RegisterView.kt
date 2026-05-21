package com.example.android.ui.view

import AppTextField
import PasswordTextField
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextButton
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.example.android.R
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
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppReturnButton(
                    modifier = Modifier.align(Alignment.Start), 
                    onClick = onPreviousStepClick
                )

                Text(
                    text = stringResource(R.string.insert_account_info),
                    fontSize = 20.sp,
                    modifier = Modifier.width(320.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(60.dp))

                AppTextField(
                    label = stringResource(R.string.name),
                    value = uiState.name,
                    onValueChange = onNameChange,
                    isError = uiState.nameError != null,
                    supportingText = uiState.nameError?.let { { Text(it) } }
                )
                AppTextField(
                    label = stringResource(R.string.email),
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = uiState.emailError != null,
                    supportingText = uiState.emailError?.let { { Text(it) } }
                )
                AppTextField(
                    label = stringResource(R.string.phone),
                    value = uiState.phone,
                    onValueChange = onPhoneChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = uiState.phoneError != null,
                    supportingText = uiState.phoneError?.let { { Text(it) } }
                )
                PasswordTextField(
                    label = stringResource(R.string.password),
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    isError = uiState.passwordError != null,
                    supportingText = uiState.passwordError?.let { { Text(it) } }
                )

                Spacer(modifier = Modifier.height(50.dp))
                AppOutlinedButton(
                    onClick = onNextStepClick,
                    label = stringResource(R.string.register_account)
                )
                
                TextButton(onClick = onLoginClick) {
                    Text(
                        stringResource(R.string.already_have_account),
                        textDecoration = TextDecoration.Underline
                    )
                }
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
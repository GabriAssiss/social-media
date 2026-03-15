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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.ui.components.AppOutlinedButton
import com.example.android.ui.components.AppReturnButton

@Composable
fun RegisterView(
    modifier: Modifier = Modifier,
    title: String,
    fieldLabel: String,
    buttonLabel: String,
    onPreviousStepClick: () -> Unit = {},
    onNextStepClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            AppReturnButton(
                onClick = onPreviousStepClick
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    modifier = Modifier.width(320.dp)
                )
                Spacer(modifier = Modifier.padding(top = 60.dp))
                DinamicTextField(fieldLabel)
                Spacer(modifier = Modifier.padding(bottom = 50.dp))
                AppOutlinedButton(
                    onClick = onNextStepClick,
                    label = buttonLabel
                )
                Text(
                    "Já tenho uma conta",
                    modifier = Modifier.clickable {
                        onLoginClick()
                    },
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Composable
fun DinamicTextField(label: String) {
    if (label.equals("Senha")) {
        PasswordTextField(
            label = label,
            value = "",
            onValueChange = {}
        )
    } else {
        AppTextField(
            label = label,
            value = "",
            onValueChange = {}
        )
    }
}

@Preview
@Composable
fun RegisterViewPreview() {
    val title = "Insira seu nome de usuário."
    val fieldLabel = "Username"
    val buttonLabel = "avançar"
    RegisterView(
        modifier = Modifier,
        title,
        fieldLabel,
        buttonLabel,
        onPreviousStepClick = {},
        onNextStepClick = {},
        onLoginClick = {}
    )
}
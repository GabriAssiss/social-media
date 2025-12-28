package com.example.android.ui.view

import AppTextField
import PasswordTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.ui.components.AppOutlinedButton


@Composable
fun AuthView(modifier: Modifier = Modifier) {
    val labels = listOf("Email ou Apelido", "Senha")
    var buttonDescription = "Entrar"
    val forgotPass = "Esqueci a senha..."
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppTextField(
                    modifier, labels[0],
                )
                PasswordTextField(modifier, labels[1])
            }
            Column(
                modifier = Modifier.fillMaxSize().padding(bottom = 50.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                AppOutlinedButton(
                    onClick = {},
                    label = buttonDescription
                )


                buttonDescription = "Cadastrar"

                AppOutlinedButton(
                    onClick = {},
                    label = buttonDescription
                )
                Text(forgotPass, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Preview
@Composable
fun LoginViewPreview() {
    AuthView()
}
package com.example.android.ui.view

import AppTextField
import PasswordTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.ui.components.AppOutlinedButton
import com.example.android.ui.components.AppReturnButton

@Composable
fun RegisterEmailView(modifier: Modifier = Modifier, labels : List<String>) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            AppReturnButton(
                onClick = {}
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Text(
                    text = labels[0],
                    fontSize = 20.sp,
                    modifier = Modifier.width(320.dp)
                )
                Spacer(modifier = Modifier.padding(top = 60.dp))
                DinamicTextField(labels[1])
                Spacer(modifier = Modifier.padding(bottom = 50.dp))
                AppOutlinedButton(
                    onClick = {},
                    label = labels[2]
                )
                Text("Já tenho uma conta")
            }
        }
    }
}

@Composable
fun DinamicTextField(label : String) {
    if(label.equals("Senha")) {
        PasswordTextField(
            label = label
        )
    }
    else {
        AppTextField(
            label = label
        )
    }
}

@Preview
@Composable
fun RegisterEmailViewPreview() {
    val labels = listOf(
        "Insira o nome pelo qual os usuários o chamarão ",
        "Username",
        "Avançar"
    )
    RegisterEmailView(modifier = Modifier, labels)
}
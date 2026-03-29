package com.example.android.ui.nav

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android.ui.view.AuthView
import com.example.android.ui.view.ChatView
import com.example.android.ui.view.MessageView
import com.example.android.ui.view.ProfileView
import com.example.android.ui.view.RegisterView
import com.example.android.ui.viewmodel.RegisterViewModel

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object RegisterUsername : Screen("registerUsername")
    object RegisterEmail : Screen("registerEmail")
    object RegisterCell : Screen("registerCell")
    object RegisterPassword : Screen("registerPassword")
    object Profile : Screen("profile")
    object Messages : Screen("messages")
    object Chat : Screen("chat")
}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Auth.route) {
        composable(Screen.Auth.route) {
            AuthView(
                onLoginSuccess = { navController.navigate(Screen.Profile.route) },
                onRegisterClick = { navController.navigate(Screen.RegisterUsername.route) }
            )
        }

        composable(Screen.RegisterUsername.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            RegisterView(
                modifier = Modifier,
                title = "Insira seu nome de usuário.",
                fieldLabel = "Username",
                buttonLabel = "Avançar",
                onPreviousStepClick = { navController.navigate(Screen.Auth.route) },
                onNextStepClick = { navController.navigate(Screen.RegisterEmail.route) },
                onLoginClick = { navController.navigate(Screen.Auth.route) },
                fieldValue = uiState.name,
                onValueChange = { viewModel.updateName(it) }
            )
        }

        composable(Screen.RegisterEmail.route) {
            val backStackEntry = remember {
                navController.getBackStackEntry(Screen.RegisterUsername.route)
            }
            val viewModel: RegisterViewModel = hiltViewModel(backStackEntry)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            RegisterView(
                modifier = Modifier,
                title = "Insira seu email.",
                fieldLabel = "Email",
                buttonLabel = "Avançar",
                onPreviousStepClick = { navController.navigate(Screen.RegisterUsername.route) },
                onNextStepClick = { navController.navigate(Screen.RegisterCell.route) },
                onLoginClick = { navController.navigate(Screen.Auth.route) },
                fieldValue = uiState.email,
                onValueChange = { viewModel.updateEmail(it) }
            )
        }

        composable(Screen.RegisterCell.route) {
            val backStackEntry = remember {
                navController.getBackStackEntry(Screen.RegisterUsername.route)
            }
            val viewModel: RegisterViewModel = hiltViewModel(backStackEntry)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            RegisterView(
                modifier = Modifier,
                title = "Insira seu número de celular.",
                fieldLabel = "Celular",
                buttonLabel = "Avançar",
                onPreviousStepClick = { navController.navigate(Screen.RegisterEmail.route) },
                onNextStepClick = { navController.navigate(Screen.RegisterPassword.route) },
                onLoginClick = { navController.navigate(Screen.Auth.route) },
                fieldValue = uiState.phone,
                onValueChange = { viewModel.updatePhone(it) }
            )
        }

        composable(Screen.RegisterPassword.route) {
            val backStackEntry = remember {
                navController.getBackStackEntry(Screen.RegisterUsername.route)
            }
            val viewModel: RegisterViewModel = hiltViewModel(backStackEntry)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.success) {
                if (uiState.success) navController.navigate(Screen.Auth.route)
            }

            RegisterView(
                modifier = Modifier,
                title = "Insira sua senha.",
                fieldLabel = "Senha",
                buttonLabel = "Finalizar",
                onPreviousStepClick = { navController.navigate(Screen.RegisterCell.route) },
                onNextStepClick = { viewModel.register() },
                onLoginClick = { navController.navigate(Screen.Auth.route) },
                fieldValue = uiState.password,
                onValueChange = { viewModel.updatePassword(it) }
            )
        }

        composable(Screen.Profile.route) {
            ProfileView(
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(Screen.Messages.route) {
            MessageView(
                onNavigate = { route -> navController.navigate(route) },
                onChatClick = { navController.navigate(Screen.Chat.route) }
            )
        }

        composable(Screen.Chat.route) {
            ChatView(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
package com.example.android.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.android.ui.viewmodel.AuthViewModel
import com.example.android.ui.viewmodel.ProfileViewModel
import com.example.android.ui.viewmodel.RegisterViewModel

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Register : Screen("register")
    object Profile : Screen("profile")
    object Messages : Screen("messages")
    object Chat : Screen("chat")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(authUiState.token) {
        if (authUiState.token == null) {
            navController.navigate(Screen.Auth.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = Screen.Auth.route) {
        composable(Screen.Auth.route) {
            val viewModel: AuthViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            LaunchedEffect(uiState.token) {
                if (uiState.token != null) {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            }

            AuthView(
                uiState = uiState,
                email = email,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onLoginClick = { viewModel.login(email, password) },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.success) {
                if (uiState.success) navController.navigate(Screen.Auth.route)
            }

            RegisterView(
                uiState = uiState,
                onPreviousStepClick = { navController.popBackStack() },
                onNextStepClick = { viewModel.register() },
                onLoginClick = { navController.navigate(Screen.Auth.route) },
                onNameChange = { viewModel.updateName(it) },
                onEmailChange = { viewModel.updateEmail(it) },
                onPhoneChange = { viewModel.updatePhone(it) },
                onPasswordChange = { viewModel.updatePassword(it) }
            )
        }

        composable(Screen.Profile.route) {

            val viewModel: ProfileViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ProfileView(
                uiState = uiState,
                onNavigate = { navController.navigate(it) },
                onLogout = { authViewModel.logout() }
            )
        }

        composable(Screen.Messages.route) {
            MessageView(
                onNavigate = { navController.navigate(it) },
                onChatClick = { navController.navigate(Screen.Chat.route) },
                onLogout = { authViewModel.logout() }
            )
        }

        composable(Screen.Chat.route) {
            ChatView(onBackClick = { navController.popBackStack() })
        }
    }
}
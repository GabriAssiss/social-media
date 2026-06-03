package com.example.android.ui.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android.ui.view.AuthView
import com.example.android.ui.view.ChatView
import com.example.android.ui.view.ConversationsView
import com.example.android.ui.view.ProfileView
import com.example.android.ui.view.RegisterView
import com.example.android.ui.viewmodel.AuthViewModel
import com.example.android.ui.viewmodel.ChatViewModel
import com.example.android.ui.viewmodel.ProfileViewModel
import com.example.android.ui.viewmodel.RegisterViewModel
import com.example.android.ui.viewmodel.AuthUiState
import com.example.android.ui.viewmodel.ChatUiState
import com.example.android.ui.viewmodel.ProfileUiState
import com.example.android.ui.viewmodel.RegisterUiState

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Register : Screen("register")
    object Profile : Screen("profile")
    object Messages : Screen("messages")
    object Chat : Screen("chat/{userId}") {
        fun createRoute(userId: Int) = "chat/$userId"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(deepLinkReceiverId: String? = null) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authUiState by authViewModel.uiState.collectAsStateWithLifecycle(initialValue = AuthUiState())

    LaunchedEffect(authUiState.token) {
        if (authUiState.token == null) {
            navController.navigate(Screen.Auth.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(deepLinkReceiverId) {
        if (deepLinkReceiverId != null && authUiState.token != null) {
            val userId = deepLinkReceiverId.toIntOrNull()
            if (userId != null) {
                navController.navigate(Screen.Chat.createRoute(userId)) {
                    popUpTo(Screen.Messages.route)
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route,
        enterTransition = { slideInHorizontally(tween(280)) { it } },
        exitTransition = { fadeOut(tween(150)) },
        popEnterTransition = { fadeIn(tween(150)) },
        popExitTransition = { slideOutHorizontally(tween(280)) { it } }
    ) {

        composable(Screen.Auth.route) {
            val viewModel: AuthViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle(initialValue = AuthUiState())

            LaunchedEffect(uiState.token) {
                if (uiState.token != null) {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            }

            AuthView(
                uiState = uiState,
                onEmailChange = { viewModel.updateEmail(it) },
                onPasswordChange = { viewModel.updatePassword(it) },
                onLoginClick = { viewModel.login(uiState.email, uiState.password) },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onForgotPasswordClick = { /* missing logic but implemented */ }
            )
        }

        composable(Screen.Register.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle(initialValue = RegisterUiState())

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
            val uiState by viewModel.uiState.collectAsStateWithLifecycle(initialValue = ProfileUiState())

            LaunchedEffect(uiState.isSessionExpired) {
                if (uiState.isSessionExpired) {
                    authViewModel.logout()
                }
            }

            ProfileView(
                uiState = uiState,
                onNavigate = { navController.navigate(it) },
                onLogout = { authViewModel.logout() }
            )
        }

        composable(Screen.Messages.route) {
            ConversationsView(
                onNavigate = { navController.navigate(it) },
                onChatClick = { userId ->
                    navController.navigate(Screen.Chat.createRoute(userId))
                },
                onLogout = { authViewModel.logout() }
            )
        }

        composable(
            route = Screen.Chat.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            val viewModel: ChatViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle(initialValue = ChatUiState())
            val inputText by viewModel.inputText.collectAsStateWithLifecycle()

            ChatView(
                withUserId = userId,
                uiState = uiState,
                inputText = inputText,
                onTextChange = { viewModel.updateInputText(it) },
                onSendMessage = { receiverId, text -> 
                     viewModel.sendMessage(receiverId, text)
                     viewModel.updateInputText("")
                },
                onInit = { viewModel.init(it) },
                onLoadMore = { viewModel.loadMore() },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
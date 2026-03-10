package com.example.android.ui.nav
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android.ui.view.AuthView
import com.example.android.ui.view.ProfileView
import com.example.android.ui.view.RegisterView

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object RegisterUsername : Screen("registerUsername")
    object RegisterEmail : Screen("registerEmail")
    object RegisterCell : Screen("registerCell")
    object RegisterPassword : Screen("registerPassword")
    object Profile : Screen("profile")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Auth.route) {
        composable(Screen.Auth.route) {
            AuthView(
                onLoginClick = { navController.navigate(Screen.Profile.route) },
                onRegisterClick = { navController.navigate(Screen.RegisterUsername.route) }
            )
        }
        composable(Screen.RegisterUsername.route) {
            val title = "Insira seu nome de usuário."
            val fieldLabel = "Username"
            val buttonLabel = "avançar"
            RegisterView(modifier = Modifier, title, fieldLabel, buttonLabel, onPreviousStepClick = {navController.navigate(Screen.Auth.route)}, onNextStepClick = {navController.navigate(Screen.RegisterEmail.route)} )
        }
        composable(Screen.RegisterEmail.route) {
            val title = "Insira seu email de usuário."
            val fieldLabel = "Email"
            val buttonLabel = "avançar"
            RegisterView(modifier = Modifier, title, fieldLabel, buttonLabel, onPreviousStepClick = {navController.navigate(Screen.RegisterUsername.route)}, onNextStepClick = {navController.navigate(Screen.RegisterCell.route)} )
        }
        composable(Screen.RegisterCell.route) {
            val title = "Insira seu número de celular."
            val fieldLabel = "Celular"
            val buttonLabel = "avançar"
            RegisterView(modifier = Modifier, title, fieldLabel, buttonLabel, onPreviousStepClick = {navController.navigate(Screen.RegisterEmail.route)}, onNextStepClick = {navController.navigate(Screen.RegisterPassword.route)} )
        }
        composable(Screen.RegisterPassword.route) {
            val title = "Insira sua senha."
            val fieldLabel = "Senha"
            val buttonLabel = "Finalizar"
            RegisterView(modifier = Modifier, title, fieldLabel, buttonLabel, onPreviousStepClick = {navController.navigate(Screen.RegisterCell.route)}, onNextStepClick = {navController.navigate(Screen.Auth.route)} )
        }
        composable(Screen.Profile.route) {
            ProfileView()
        }
    }
}
package com.fahad.list_food.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.fahad.auth_fierbase_bottomnavigation.ui.screen.navigation.auth.AuthNavigation
import com.fahad.auth_fierbase_bottomnavigation.ui.screen.navigation.auth.AuthScreen
import com.fahad.list_food.ui.navigation.bottom.BottomBarRoot
import com.fahad.list_food.model.FavoriteViewModel
import com.fahad.list_food.model.FoodViewModel
import com.fahad.list_food.ui.screen.UserDataViewModel
import com.fahad.list_food.ui.screen.auth.login.LoginViewModel
import com.fahad.list_food.ui.screen.auth.register.RegisterViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay

@Composable
fun RootNavigation(navController: NavHostController) {
  val loginViewModel: LoginViewModel = hiltViewModel()
  val registerViewModel: RegisterViewModel = hiltViewModel()
  val userDataViewModel: UserDataViewModel = hiltViewModel()



  // Check authentication state when the RootNavigationGraph is recomposed
  LaunchedEffect(Unit) {
    // Simulate a splash screen delay if needed
    delay(1000) // 2 seconds delay, adjust as needed

    // Check authentication state
    checkAuthenticationState(navController, userDataViewModel)
  }

  NavHost(
    navController = navController,
    route = Graph.ROOT,
    startDestination = Graph.SPLASH
  ) {

      composable(route = Graph.SPLASH) {
        SplashScreen()
      }
    AuthNavigation(
      navController = navController,
      loginViewModel = loginViewModel,
      registerViewModel = registerViewModel,
    )
    composable(route = Graph.HOME) {
      BottomBarRoot(



      )
    }
  }
}

fun checkAuthenticationState(navController: NavController, userDataViewModel: UserDataViewModel) {
  val auth = Firebase.auth
  val currentUser = auth.currentUser

  if (currentUser != null) {
    // Get user data from Firebase
    userDataViewModel.getUserData()

    // Navigate to Home Screen
    navController.navigate(Graph.HOME) {
      popUpTo(Graph.AUTHENTICATION) {
        inclusive = true
      }
    }
  } else {
    // Navigate to Login Screen
    navController.navigate(AuthScreen.LOGIN.route) {
      popUpTo(Graph.AUTHENTICATION) {
        inclusive = true
      }
    }
  }
}

object Graph {
  const val ROOT = "root_graph"
  const val AUTHENTICATION = "auth_graph"
  const val HOME = "home_graph"
  const val Search = "Search_graph"
    const val SPLASH = "splash"
}
@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
        .fillMaxSize()
        .background(Color.White), // Set a background color for the splash screen
        contentAlignment = Alignment.Center
    ) {
        // Add your splash screen content
        Column(
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            text = "Auth Firebase",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }

}


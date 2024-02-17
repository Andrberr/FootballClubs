package com.example.footballclubs

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.footballclubs.models.Destinations
import com.example.footballclubs.models.User
import com.example.footballclubs.ui.screen.DetailScreen
import com.example.footballclubs.ui.screen.FavouriteScreen
import com.example.footballclubs.ui.screen.HomeScreen
import com.example.footballclubs.ui.screen.ProfileScreen
import com.example.footballclubs.ui.screen.SignInScreen
import com.example.footballclubs.ui.screen.SignUpScreen
import com.example.footballclubs.ui.screen.SplashScreen
import com.example.footballclubs.ui.view_model.MainViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: MainViewModel,
    showBottomNavBar: () -> Unit,
    signInUser: (String, String) -> Unit,
    signUpUser: (User) -> Unit,
    logOut: () -> Unit,
    deleteUser: () -> Unit
) {
    NavHost(navController, startDestination = Destinations.SplashDestination.route) {
        composable(Destinations.SplashDestination.route) {
            SplashScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Destinations.HomeDestination.route) {
            HomeScreen(
                navController = navController,
                viewModel = viewModel,
                showBottomNavBar = showBottomNavBar
            )
        }
        composable(Destinations.FavouriteDestination.route) {
            FavouriteScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Destinations.ProfileDestination.route) {
            ProfileScreen(logOut = logOut, deleteUser = deleteUser)
        }
        composable(Destinations.DetailsDestination.route) {
            DetailScreen(viewModel = viewModel)
        }
        composable(Destinations.SignInDestination.route) {
            SignInScreen(navController = navController, signInUser = signInUser)
        }
        composable(Destinations.SignUpDestination.route) {
            SignUpScreen(signUpUser = signUpUser)
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    if (state.value) {
        val screens = listOf(
            Destinations.FavouriteDestination,
            Destinations.HomeDestination,
            Destinations.ProfileDestination
        )

        NavigationBar(
            modifier = modifier,
            containerColor = Color.LightGray,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            screens.forEach { screen ->

                NavigationBarItem(
                    label = {
                        Text(text = screen.title!!)
                    },
                    icon = {
                        Icon(imageVector = screen.icon!!, contentDescription = "")
                    },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.popBackStack()
                        navController.navigate(screen.route)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedTextColor = Color.Gray, selectedTextColor = Color.White
                    )
                )
            }
        }
    }
}

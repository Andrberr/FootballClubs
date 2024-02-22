package com.example.footballclubs.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.footballclubs.R
import com.example.footballclubs.ui.theme.DarkBlue
import com.example.footballclubs.ui.view_model.MainViewModel
import com.example.footballclubs.utils.Clubs

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    showBottomNavBar: () -> Unit
) {
    val clubs = viewModel.clubs.collectAsState()

    LaunchedEffect(key1 = Unit) {
        showBottomNavBar()
        viewModel.fetchClubs()
    }

    Clubs(
        title = stringResource(id = R.string.home_title),
        clubs = clubs,
        navController = navController,
        viewModel = viewModel,
        color = DarkBlue,
        showSearchIcon = true
    )
}

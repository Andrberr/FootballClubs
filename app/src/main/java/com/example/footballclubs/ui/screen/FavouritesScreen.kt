package com.example.footballclubs.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.footballclubs.R
import com.example.footballclubs.ui.theme.DarkPurple
import com.example.footballclubs.ui.view_model.MainViewModel
import com.example.footballclubs.utils.Clubs
import com.example.footballclubs.utils.CurrentUser

@Composable
fun FavouriteScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {

    val clubs = viewModel.likedClubs.collectAsState()

    LaunchedEffect(key1 = Unit) {
        val likedIds = CurrentUser.user?.likedClubsIds ?: emptyList()
        viewModel.fetchLikedClubs(likedIds)
    }

    Clubs(
        title = stringResource(id = R.string.favourite_title),
        clubs = clubs,
        navController = navController,
        viewModel = viewModel,
        color = DarkPurple,
        showSearchIcon = false
    )
}
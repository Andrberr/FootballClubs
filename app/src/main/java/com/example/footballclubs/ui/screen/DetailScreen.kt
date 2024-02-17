package com.example.footballclubs.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.footballclubs.R
import com.example.footballclubs.ui.theme.DarkBlue
import com.example.footballclubs.ui.view_model.MainViewModel
import com.example.footballclubs.utils.CurrentUser
import com.example.footballclubs.utils.DataHolder

@Composable
fun DetailScreen(viewModel: MainViewModel) {
    val club = viewModel.currentClub
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = stringResource(id = R.string.best_players_title),
            style = TextStyle(color = Color.White, fontSize = 26.sp),
            fontWeight = FontWeight.Bold,
            fontFamily = DataHolder.fontFamily
        )

        ImagesSlider(playersImages = club.playersImages)

        Box(modifier = Modifier.padding(vertical = 10.dp)) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val iconState = remember {
                    mutableStateOf(isClubLiked(club.id))
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = club.name,
                        style = TextStyle(color = Color.White, fontSize = 24.sp),
                        fontWeight = FontWeight.Bold,
                        fontFamily = DataHolder.fontFamily
                    )
                    IconButton(onClick = {
                        if (iconState.value) removeClub(club.id, viewModel)
                        else addClub(club.id, viewModel)
                        iconState.value = !iconState.value
                    }) {
                        Icon(
                            modifier = Modifier
                                .size(32.dp),
                            imageVector = if (iconState.value) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = club.description + club.description,
                    style = TextStyle(color = Color.LightGray, fontSize = 20.sp),
                    fontFamily = DataHolder.fontFamily
                )
            }
        }
    }
}

private fun isClubLiked(id: Int): Boolean {
    return CurrentUser.user?.likedClubsIds?.contains(id) ?: false
}

private fun addClub(id: Int, viewModel: MainViewModel) {
    CurrentUser.user?.run {
        likedClubsIds.add(id)
        viewModel.addClubToFavourites(email, id)
    }
}

private fun removeClub(id: Int, viewModel: MainViewModel) {
    CurrentUser.user?.run {
        likedClubsIds.remove(id)
        viewModel.removeClubFromFavourites(email, id)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImagesSlider(modifier: Modifier = Modifier, playersImages: List<String>) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        playersImages.size
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize(),
            state = pagerState,
            pageSize = PageSize.Fill
        ) { index ->
            Image(
                painter = rememberAsyncImagePainter(playersImages[index]),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
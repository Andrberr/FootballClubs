package com.example.footballclubs.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.footballclubs.R
import com.example.footballclubs.extensions.noRippleClickable
import com.example.footballclubs.models.Club
import com.example.footballclubs.models.Destinations
import com.example.footballclubs.ui.view_model.MainViewModel

@Composable
fun TitleText(modifier: Modifier = Modifier, text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        fontFamily = DataHolder.fontFamily,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    )
}

@Composable
fun InputField(hint: String, textFieldState: MutableState<String>, modifier: Modifier = Modifier) {
    TextField(
        modifier = modifier.fillMaxWidth(0.7f),
        value = textFieldState.value,
        label = {
            Text(hint)
        },
        onValueChange = {
            textFieldState.value = it
        },
        singleLine = true
    )
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun SearchField(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val inputState = remember { mutableStateOf("") }
    OutlinedTextField(
        value = inputState.value,
        onValueChange = {
            if (it.isEmpty()) viewModel.fetchSearchClubs(it)
            inputState.value = it
        },
        label = { Text(LocalContext.current.getString(R.string.search_hint)) },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            viewModel.fetchSearchClubs(inputState.value)
        }),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 10.dp, end = 10.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null
            )
        },
        shape = RoundedCornerShape(6.dp),
    )
    Spacer(modifier = Modifier.height(12.dp))
}


@Composable
fun Clubs(
    modifier: Modifier = Modifier,
    title: String,
    clubs: State<List<Club>>,
    navController: NavHostController,
    viewModel: MainViewModel,
    color: Color,
    showSearchIcon: Boolean
) {

    Column(modifier = modifier.fillMaxSize()) {
        if (showSearchIcon) {
            SearchField(
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel
            )
        }

        TitleText(
            text = title,
            color = Color.Black,
        )

        ClubsColumn(
            clubs = clubs.value,
            navController = navController,
            viewModel = viewModel,
            color = color
        )
    }
}

@Composable
fun ClubsColumn(
    clubs: List<Club>?,
    navController: NavHostController,
    viewModel: MainViewModel,
    color: Color,
    modifier: Modifier = Modifier,
) {
    if (!clubs.isNullOrEmpty()) {
        val onItemClick: (Club) -> Unit = {
            viewModel.currentClub = it
            navController.navigate(Destinations.DetailsDestination.route)
        }
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
        ) {
            items(clubs) {
                ClubItem(club = it, onItemClick = onItemClick, color = color)
            }
        }
    }
}

@Composable
fun ClubItem(
    club: Club,
    onItemClick: (Club) -> Unit,
    color: Color,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .padding(10.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color)
        ) {
            Image(
                painter = rememberAsyncImagePainter(club.url),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .size(200.dp)
                    .clickable(onClick = { onItemClick(club) }),
                alignment = Alignment.TopCenter,
            )

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp, top = 5.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    club.name,
                    style = TextStyle(color = Color.White, fontSize = 20.sp),
                    fontFamily = DataHolder.fontFamily
                )
            }
        }
    }
}
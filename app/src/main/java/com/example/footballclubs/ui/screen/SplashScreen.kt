package com.example.footballclubs.ui.screen

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.footballclubs.R
import com.example.footballclubs.models.Destinations
import com.example.footballclubs.ui.view_model.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController, viewModel: MainViewModel) {
    val scale = remember {
        Animatable(0f)
    }
    val isUserSignedIn by viewModel.isUserSignedIn.collectAsState()

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 2f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(1000)
        navigate(navController, isUserSignedIn)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ball),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .scale(scale.value)
        )
    }
}

fun navigate(navController: NavHostController, isUserSignedIn: Boolean) {
    navController.popBackStack()
    val destination =
        if (isUserSignedIn) Destinations.HomeDestination.route
        else Destinations.SignInDestination.route
    navController.navigate(destination)
}
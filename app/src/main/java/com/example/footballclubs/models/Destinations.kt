package com.example.footballclubs.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {

    data object SplashDestination : Destinations(
        route = SPLASH_ROUTE,
        title = SPLASH_TITLE
    )

    data object HomeDestination : Destinations(
        route = HOME_ROUTE,
        title = HOME_TITLE,
        icon = Icons.Outlined.Home
    )

    data object FavouriteDestination : Destinations(
        route = FAVOURITE_ROUTE,
        title = FAVOURITE_TITLE,
        icon = Icons.Outlined.FavoriteBorder
    )

    data object ProfileDestination : Destinations(
        route = PROFILE_ROUTE,
        title = PROFILE_TITLE,
        icon = Icons.Outlined.Person
    )

    data object DetailsDestination : Destinations(
        route = DETAILS_ROUTE,
        title = DETAILS_TITLE
    )

    data object SignInDestination : Destinations(
        route = SIGN_IN_ROUTE,
        title = SIGN_IN_TITLE
    )

    data object SignUpDestination : Destinations(
        route = SIGN_UP_ROUTE,
        title = SIGN_UP_TITLE
    )

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    private companion object {
        const val SPLASH_ROUTE = "splash_screen"
        const val SPLASH_TITLE = "Splash"
        const val HOME_ROUTE = "home_screen"
        const val HOME_TITLE = "Home"
        const val FAVOURITE_ROUTE = "favourite_screen"
        const val FAVOURITE_TITLE = "Favorite"
        const val PROFILE_ROUTE = "profile_screen"
        const val PROFILE_TITLE = "Profile"
        const val DETAILS_ROUTE = "details_screen"
        const val DETAILS_TITLE = "Details"
        const val SIGN_IN_ROUTE = "sign_in_screen"
        const val SIGN_IN_TITLE = "Sign In"
        const val SIGN_UP_ROUTE = "sign_up_screen"
        const val SIGN_UP_TITLE = "Sign Up"
    }
}

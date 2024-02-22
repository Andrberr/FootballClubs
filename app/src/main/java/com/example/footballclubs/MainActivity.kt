package com.example.footballclubs

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.footballclubs.models.Destinations
import com.example.footballclubs.models.User
import com.example.footballclubs.ui.theme.FootballClubsTheme
import com.example.footballclubs.ui.view_model.MainViewModel
import com.example.footballclubs.utils.CurrentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private val firestore by lazy {
        Firebase.firestore
    }
    private val viewModel: MainViewModel by viewModels {
        MainViewModel.MainFactory(firestore)
    }

    private var isActivityStopped = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        setContent {
            FootballClubsTheme {
                val navController: NavHostController = rememberNavController()
                val buttonsVisible = remember { mutableStateOf(false) }
                observeSignUpState(navController)
                observeSignInState(navController)
                observeUserDeleteState(navController, buttonsVisible)
                observeLikeState()


                BottomNavigationBar(
                    viewModel = viewModel,
                    buttonsVisible = buttonsVisible,
                    navController = navController,
                    signInUser = { email, password -> signInUser(email, password) },
                    signUpUser = { signUpUser(it) },
                    logOut = {
                        logOutUser(navController = navController)
                        buttonsVisible.value = false
                    },
                    deleteUser = {
                        deleteUser()
                    }
                )
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        if (!isActivityStopped) {
            val email = auth.currentUser?.email ?: ""
            if (email.isNotEmpty()) viewModel.getUser(email, false)
        }
    }

    override fun onStop() {
        isActivityStopped = true
        super.onStop()
    }

    private fun logOutUser(navController: NavHostController) {
        CurrentUser.user = null
        auth.signOut()
        navController.popBackStack()
        navController.navigate(Destinations.SignInDestination.route)
    }

    private fun signUpUser(user: User) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    viewModel.createUser(user)
                } else {
                    showErrorToast(getString(R.string.registration_error))
                }
            }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userEmail = auth.currentUser?.email ?: ""
                    viewModel.getUser(userEmail, true)
                } else {
                    showErrorToast(getString(R.string.authorization_error))
                }
            }

    }

    private fun deleteUser() {
        val user = auth.currentUser
        user?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.email?.let { viewModel.deleteUser(it) }
                }
            }
    }

    private fun observeUserDeleteState(
        navController: NavHostController,
        buttonsVisible: MutableState<Boolean>
    ) {
        viewModel.isUserDeleted.observe(this) { isSuccess ->
            if (isSuccess) {
                logOutUser(navController = navController)
                buttonsVisible.value = false
            }
        }
    }

    private fun observeSignUpState(navController: NavHostController) {
        viewModel.isSignUpSuccess.observe(this) { isSuccess ->
            navigateHome(isSuccess, navController)
        }
    }

    private fun observeSignInState(navController: NavHostController) {
        viewModel.isSignInSuccess.observe(this) { isSuccess ->
            navigateHome(isSuccess, navController)
        }
    }

    private fun observeLikeState() {
        viewModel.isLikeSuccess.observe(this) { isSuccess ->
            if (!isSuccess) showErrorToast(getString(R.string.unexpected_error))
        }
    }

    private fun navigateHome(isSuccess: Boolean, navController: NavHostController) {
        if (isSuccess) {
            navController.navigate(Destinations.HomeDestination.route)
        } else {
            showErrorToast(getString(R.string.authentication_error))
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    buttonsVisible: MutableState<Boolean>,
    navController: NavHostController,
    viewModel: MainViewModel,
    signInUser: (String, String) -> Unit,
    signUpUser: (User) -> Unit,
    logOut: () -> Unit,
    deleteUser: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                state = buttonsVisible,
                modifier = modifier
            )
        }) { paddingValues ->
        Box(
            modifier = modifier.padding(paddingValues)
        ) {
            Navigation(
                navController = navController,
                viewModel = viewModel,
                showBottomNavBar = { buttonsVisible.value = true },
                signInUser = signInUser,
                signUpUser = signUpUser,
                logOut = logOut,
                deleteUser = deleteUser
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FootballClubsTheme {
    }
}
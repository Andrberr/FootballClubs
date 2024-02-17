package com.example.footballclubs.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.footballclubs.R
import com.example.footballclubs.extensions.noRippleClickable
import com.example.footballclubs.models.Destinations
import com.example.footballclubs.ui.theme.DarkBlue
import com.example.footballclubs.utils.InputField
import com.example.footballclubs.utils.TitleText

@Composable
fun SignInScreen(navController: NavHostController, signInUser: (String, String) -> Unit) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, top = 16.dp)
                .noRippleClickable { navController.navigate(Destinations.SignUpDestination.route) },
            text = stringResource(id = R.string.sign_up),
            color = Color.White,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Bold
        )
        Column(
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleText(text = stringResource(id = R.string.sign_in_title), color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))
            InputField(stringResource(id = R.string.enter_email), emailState)
            InputField(stringResource(id = R.string.enter_password), passwordState)
            Spacer(modifier = Modifier.height(12.dp))

            val context = LocalContext.current
            Button(modifier = Modifier.width(200.dp), onClick = {
                val email = emailState.value
                val password = passwordState.value
                if (email.isNotBlank() && password.isNotBlank()) {
                    signInUser(email, password)
                } else Toast.makeText(
                    context,
                    context.getString(R.string.empty_fields_message),
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                Text(text = stringResource(id = R.string.sign_in), textAlign = TextAlign.Center)
            }
        }
    }
}
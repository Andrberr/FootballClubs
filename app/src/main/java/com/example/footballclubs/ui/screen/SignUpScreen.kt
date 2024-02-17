package com.example.footballclubs.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.footballclubs.R
import com.example.footballclubs.models.User
import com.example.footballclubs.ui.theme.DarkBlue
import com.example.footballclubs.utils.InputField
import com.example.footballclubs.utils.TitleText

@Composable
fun SignUpScreen(signUpUser: (User) -> Unit) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val nameState = remember { mutableStateOf("") }
    val surnameState = remember { mutableStateOf("") }
    val patronymicState = remember { mutableStateOf("") }
    val birthdayState = remember { mutableStateOf("") }
    val genderState = remember { mutableStateOf("") }
    val statusState = remember { mutableStateOf("") }
    val bioState = remember { mutableStateOf("") }
    val countryState = remember { mutableStateOf("") }
    val cityState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TitleText(text = stringResource(id = R.string.sign_up_title), color = Color.White)
        InputField(stringResource(id = R.string.enter_email), emailState)
        InputField(stringResource(id = R.string.enter_password), passwordState)
        InputField(stringResource(id = R.string.enter_name), nameState)
        InputField(stringResource(id = R.string.enter_surname), surnameState)
        InputField(stringResource(id = R.string.enter_patronymic), patronymicState)
        InputField(stringResource(id = R.string.enter_birthday), birthdayState)
        InputField(stringResource(id = R.string.select_gender), genderState)
        InputField(stringResource(id = R.string.enter_status), statusState)
        InputField(stringResource(id = R.string.enter_bio), bioState)
        InputField(stringResource(id = R.string.enter_country), countryState)
        InputField(stringResource(id = R.string.enter_city), cityState)

        val context = LocalContext.current
        Button(
            modifier = Modifier.width(200.dp),
            onClick = {
                val email = emailState.value
                val password = passwordState.value
                if (email.isNotBlank() && password.isNotBlank()) {
                    signUpUser(
                        User(
                            email = email,
                            password = password,
                            name = nameState.value,
                            surname = surnameState.value,
                            patronymic = patronymicState.value,
                            birthday = birthdayState.value,
                            gender = genderState.value,
                            status = statusState.value,
                            description = bioState.value,
                            country = countryState.value,
                            city = cityState.value
                        )
                    )
                } else Toast.makeText(
                    context,
                    context.getString(R.string.empty_fields_message),
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            Text(text = stringResource(id = R.string.sign_up), textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}
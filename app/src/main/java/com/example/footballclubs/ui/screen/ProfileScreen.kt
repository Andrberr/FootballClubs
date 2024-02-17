package com.example.footballclubs.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.footballclubs.R
import com.example.footballclubs.extensions.noRippleClickable
import com.example.footballclubs.ui.theme.DarkBlue
import com.example.footballclubs.utils.CurrentUser
import com.example.footballclubs.utils.DataHolder

@Composable
fun ProfileScreen(logOut: () -> Unit, deleteUser: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            RoundImage(
                image = painterResource(id = R.drawable.profile_photo),
                modifier = Modifier
                    .size(160.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        CurrentUser.user?.run {
            UserField(value = email)
            UserField(value = name)
            UserField(value = surname)
            UserField(value = patronymic)
            UserField(value = birthday)
            UserField(value = gender)
            UserField(value = status)
            UserField(value = description)
            UserField(value = country)
            UserField(value = city)
        }
    }

    val openLogoutDialog = remember { mutableStateOf(false) }
    val openDeleteUserDialog = remember { mutableStateOf(false) }
    PopupWindowDialog(
        stringResource(id = R.string.logout_confirm_message),
        openLogoutDialog,
        logOut
    )
    PopupWindowDialog(
        stringResource(id = R.string.delete_confirm_message),
        openDeleteUserDialog,
        deleteUser
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.logout),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .offset(y = 10.dp, x = 10.dp)
                .noRippleClickable {
                    if (openDeleteUserDialog.value) openDeleteUserDialog.value = false
                    openLogoutDialog.value = true
                },
        )
        Image(
            painter = painterResource(id = R.drawable.delete_user),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .offset(y = 10.dp, x = (-10).dp)
                .noRippleClickable {
                    if (openLogoutDialog.value) openLogoutDialog.value = false
                    openDeleteUserDialog.value = true
                },
        )
    }
}

@Composable
fun RoundImage(image: Painter, modifier: Modifier = Modifier) {
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
            .border(width = 1.dp, color = Color.LightGray, shape = CircleShape)
            .clip(CircleShape)
    )
}

@Composable
fun UserField(modifier: Modifier = Modifier, value: String) {
    Text(
        modifier = modifier.padding(horizontal = 12.dp),
        text = value,
        style = TextStyle(color = Color.White, fontSize = 22.sp),
        fontWeight = FontWeight.Medium,
        fontFamily = DataHolder.fontFamily,
        color = DarkBlue
    )
    Spacer(modifier = modifier.height(10.dp))
}

@Composable
fun PopupWindowDialog(title: String, openDialog: MutableState<Boolean>, action: () -> Unit) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val popupWidth = 300.dp
        val popupHeight = 150.dp

        if (openDialog.value) {

            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties()
            ) {

                Box(
                    Modifier
                        .size(popupWidth, popupHeight)
                        .padding(top = 5.dp)
                        .background(DarkBlue, RoundedCornerShape(10.dp))
                        .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp))
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {

                        Text(
                            text = title,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 5.dp),
                            fontSize = 18.sp
                        )

                        Row {
                            Button(
                                onClick = {
                                    action()
                                    openDialog.value = false
                                },
                                modifier = Modifier.padding(horizontal = 10.dp)
                            ) {
                                Text(text = stringResource(id = R.string.yes), fontSize = 16.sp)
                            }

                            Button(
                                onClick = {
                                    openDialog.value = false
                                },
                                modifier = Modifier.padding(horizontal = 10.dp)
                            ) {
                                Text(text = stringResource(id = R.string.no), fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
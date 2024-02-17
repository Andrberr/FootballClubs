package com.example.footballclubs.utils

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.footballclubs.R

object DataHolder {
    val fontFamily = FontFamily(
        Font(R.font.playpensans_bold, FontWeight.Bold),
        Font(R.font.playpensans_extrabold, FontWeight.ExtraBold),
        Font(R.font.playpensans_extralight, FontWeight.ExtraLight),
        Font(R.font.playpensans_light, FontWeight.Light),
        Font(R.font.playpensans_medium, FontWeight.Medium),
        Font(R.font.playpensans_regular, FontWeight.Normal),
        Font(R.font.playpensans_semibold, FontWeight.SemiBold),
        Font(R.font.playpensans_thin, FontWeight.Thin)
    )
}
package com.example.footballclubs.models

data class User(
    val email: String,
    val password: String,
    val name: String = "",
    val surname: String = "",
    val patronymic: String = "",
    val birthday: String = "",
    val gender: String = "",
    val status: String = "",
    val description: String = "",
    val country: String = "",
    val city: String = "",
    val likedClubsIds: MutableList<Int> = mutableListOf()
)

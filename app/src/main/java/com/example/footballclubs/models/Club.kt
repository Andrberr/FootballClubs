package com.example.footballclubs.models

data class Club(
    val id: Int,
    val name: String,
    val url: String,
    val description: String,
    val playersImages: List<String>
) {
    companion object {
        fun empty() =
            Club(id = 0, name = "", url = "", description = "", playersImages = emptyList())
    }
}

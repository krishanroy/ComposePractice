package com.krishan.composePractice.ui.nav

// No need for kotlinx serialization
sealed class Screen {
    data object Home : Screen()
    data class Details(val user: User) : Screen()
}

data class User(val id: String, val name: String, val email: String)
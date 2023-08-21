package com.skid.kodetestappcompose.ui.navigation

const val USER_DETAILS_SCREEN_ID_ARGUMENT_KEY = "id"

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object UserDetailsScreen :
        Screen("user_details_screen/{$USER_DETAILS_SCREEN_ID_ARGUMENT_KEY}") {
        fun passUserId(id: String): String {
            return this.route.replace(
                oldValue = "{$USER_DETAILS_SCREEN_ID_ARGUMENT_KEY}",
                newValue = id
            )
        }
    }
    object ErrorScreen : Screen("error_screen")
}

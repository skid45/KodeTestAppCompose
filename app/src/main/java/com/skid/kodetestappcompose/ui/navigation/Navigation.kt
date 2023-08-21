package com.skid.kodetestappcompose.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.skid.error.ui.ErrorScreen
import com.skid.kodetestappcompose.ui.navigation.SlideAnimations.enterTransition
import com.skid.kodetestappcompose.ui.navigation.SlideAnimations.exitTransition
import com.skid.main_screen.ui.MainScreen
import com.skid.main_screen.ui.MainScreenViewModel
import com.skid.user_details.ui.UserDetailsScreen
import com.skid.user_details.ui.UserDetailsScreenViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(
            route = Screen.MainScreen.route,
            enterTransition = { enterTransition(AnimatedContentTransitionScope.SlideDirection.Left) },
            exitTransition = { exitTransition(AnimatedContentTransitionScope.SlideDirection.Left) },
            popEnterTransition = { enterTransition(AnimatedContentTransitionScope.SlideDirection.Right) },
            popExitTransition = { exitTransition(AnimatedContentTransitionScope.SlideDirection.Right) }
        ) {
            val mainScreenViewModel = hiltViewModel<MainScreenViewModel>()
            val state by mainScreenViewModel.state.collectAsState()

            MainScreen(
                state = state,
                onEvent = mainScreenViewModel::onEvent,
                onUserDetailsScreen = {
                    navController.navigate(Screen.UserDetailsScreen.passUserId(it)) {
                        launchSingleTop = true
                    }
                },
                onErrorScreen = {
                    navController.navigate(Screen.ErrorScreen.route) {
                        popUpTo(Screen.ErrorScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.UserDetailsScreen.route,
            arguments = listOf(
                navArgument(USER_DETAILS_SCREEN_ID_ARGUMENT_KEY) { type = NavType.StringType }
            ),
            enterTransition = { enterTransition(AnimatedContentTransitionScope.SlideDirection.Left) },
            exitTransition = { exitTransition(AnimatedContentTransitionScope.SlideDirection.Left) },
            popEnterTransition = { enterTransition(AnimatedContentTransitionScope.SlideDirection.Right) },
            popExitTransition = { exitTransition(AnimatedContentTransitionScope.SlideDirection.Right) }
        ) {
            val userDetailsScreenViewModel = hiltViewModel<UserDetailsScreenViewModel>()
            val userDetails by userDetailsScreenViewModel.userDetails.collectAsState()
            if (userDetails != null) {
                UserDetailsScreen(
                    userDetailsItem = userDetails!!,
                    onBackButtonClick = {
                        navController.popBackStack(Screen.MainScreen.route, false)
                    }
                )
            }
        }

        composable(
            route = Screen.ErrorScreen.route
        ) {
            ErrorScreen {
                navController.navigate(Screen.MainScreen.route) {
                    popUpTo(Screen.MainScreen.route) { inclusive = true }
                }
            }
        }
    }
}


object SlideAnimations {
    fun (AnimatedContentTransitionScope<NavBackStackEntry>).enterTransition(
        towards: AnimatedContentTransitionScope.SlideDirection,
    ): EnterTransition {
        return slideIntoContainer(
            towards = towards,
            animationSpec = tween(300)
        )
    }

    fun (AnimatedContentTransitionScope<NavBackStackEntry>).exitTransition(
        towards: AnimatedContentTransitionScope.SlideDirection,
    ): ExitTransition {
        return slideOutOfContainer(
            towards = towards,
            animationSpec = tween(300)
        )
    }
}
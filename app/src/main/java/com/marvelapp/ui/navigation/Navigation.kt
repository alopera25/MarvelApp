package com.marvelapp.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.marvelapp.ui.screens.detail.DetailScreen
import com.marvelapp.ui.screens.detail.DetailViewModel
import com.marvelapp.ui.screens.home.HomeScreen
import com.marvelapp.ui.screens.splash.SplashScreen
import com.marvelapp.ui.screens.splash.SplashViewModel

// Navigation.kt
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation() {
    SharedTransitionLayout {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Splash) {
            composable<Splash> {
                SplashScreen(navController, SplashViewModel()) {
                }
            }

            composable<Home> {
                HomeScreen(
                    onClick = { character ->
                        navController.navigate(Detail(character.id!!, character.thumbnail?.let { "${it.path}.${it.extension}" }))
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable<Detail> { backStackEntry ->
                val detail = backStackEntry.toRoute<Detail>()
                DetailScreen(
                    viewModel { DetailViewModel(detail.characterId) },
                    onBack = { navController.popBackStack() },
                    imageUrl = detail.imageUrl,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }
        }
    }
}



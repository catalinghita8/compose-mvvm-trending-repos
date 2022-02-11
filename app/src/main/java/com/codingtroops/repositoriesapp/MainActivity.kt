package com.codingtroops.repositoriesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codingtroops.repositoriesapp.presentation.details.RepositoryDetailsScreen
import com.codingtroops.repositoriesapp.presentation.details.RepositoryDetailsViewModel
import com.codingtroops.repositoriesapp.presentation.list.RepositoriesListScreen
import com.codingtroops.repositoriesapp.presentation.list.RepositoryListViewModel
import com.codingtroops.repositoriesapp.theme.RepositoriesAppTheme
import com.codingtroops.repositoriesapp.utils.NavConstants
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity is used as singular entry-point for Compose-based App.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RepositoriesAppTheme {
                RepositoriesApp()
            }
        }
    }
}

@Composable
private fun RepositoriesApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavConstants.DESTINATION_REPOS_LIST) {
        composable(route = NavConstants.DESTINATION_REPOS_LIST) {
            val viewModel: RepositoryListViewModel = hiltViewModel()
            val state = viewModel.state.value
            RepositoriesListScreen(
                state = state,
                onItemClick = { key ->
                    navController.navigate("${NavConstants.DESTINATION_REPOS_LIST}/$key")
                }, onLanguageSelected = { language ->
                    viewModel.setLanguage(language)
                }, onSortingToggled = {
                    viewModel.toggleSorting()
                })
        }
        composable(
            route = "repos/{${NavConstants.RepoDetailsKey}}",
            arguments = listOf(navArgument(NavConstants.RepoDetailsKey) {
                type = NavType.StringType
            })
        ) {
            val viewModel: RepositoryDetailsViewModel = hiltViewModel()
            val item = viewModel.state.value
            RepositoryDetailsScreen(item)
        }
    }
}
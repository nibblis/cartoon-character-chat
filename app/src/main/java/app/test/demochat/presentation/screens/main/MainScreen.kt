package app.test.demochat.presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import app.test.demochat.presentation.navigation.NavigationProvider
import app.test.demochat.presentation.navigation.Screens
import app.test.demochat.presentation.theme.DemoChatTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
@Preview
fun MainScreen() {
    DemoChatTheme {

        val navController = rememberNavController()
        val navigationProvider = koinInject<NavigationProvider>()

        LaunchedEffect(navController) {
            navigationProvider.setNavController(navController)
        }
        val viewModel: MainViewModel = koinViewModel()
        val appState by viewModel.state.collectAsState()

        Surface(Modifier.fillMaxWidth()) {
            when (appState) {
                AppState.Loading -> {
                    LoadingIndicator()
                }

                else -> {
                    NavHost(
                        navController,
                        startDestination = when (appState) {
                            AppState.Authenticated -> Screens.ChatList
                            else -> Screens.Auth
                        },
                    ) {
                        viewModel.navigatables.forEach {
                            it.display(this)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
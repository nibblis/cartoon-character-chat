package app.test.demochat.presentation.navigation.items

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.test.demochat.presentation.navigation.INavigation
import app.test.demochat.presentation.navigation.Screens
import app.test.demochat.presentation.screens.auth.AuthScreen

class AuthItem : INavigation {
    override fun display(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Screens.Auth> {
            AuthScreen()
        }
    }
}
package app.test.demochat.presentation.navigation.items

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import app.test.demochat.data.model.UserParams
import app.test.demochat.presentation.navigation.INavigation
import app.test.demochat.presentation.navigation.Screens
import app.test.demochat.presentation.navigation.serializableType
import app.test.demochat.presentation.screens.registration.RegistrationScreen
import kotlin.reflect.typeOf

class RegistrationItem : INavigation {
    override fun display(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Screens.Registration>(
            typeMap = mapOf(
                typeOf<UserParams>() to serializableType<UserParams>(),
            )
        ) {
            val data = it.toRoute<Screens.Registration>()
            RegistrationScreen(data.params)
        }
    }
}
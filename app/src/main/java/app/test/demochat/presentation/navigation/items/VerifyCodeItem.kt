package app.test.demochat.presentation.navigation.items

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import app.test.demochat.data.model.AuthParams
import app.test.demochat.presentation.navigation.INavigation
import app.test.demochat.presentation.navigation.Screens
import app.test.demochat.presentation.navigation.serializableType
import app.test.demochat.presentation.screens.auth.verify_code.VerifyCodeScreen
import kotlin.reflect.typeOf

class VerifyCodeItem : INavigation {
    override fun display(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Screens.VerifyCode>(
            typeMap = mapOf(
                typeOf<AuthParams>() to serializableType<AuthParams>(),
            )
        ) {
            val data = it.toRoute<Screens.VerifyCode>()
            VerifyCodeScreen(data.params)
        }
    }
}
package app.test.demochat.presentation.navigation

import androidx.navigation.NavHostController

class NavigationProvider {
    private var _navController: NavHostController? = null
    val navController: NavHostController get() = requireNotNull(_navController)

    fun setNavController(navController: NavHostController) {
        _navController = navController
    }
}
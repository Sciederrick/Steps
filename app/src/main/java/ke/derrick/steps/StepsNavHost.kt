package ke.derrick.steps

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ke.derrick.steps.ui.main.MainScreen

@Composable
fun StepsNavHost(
    navController: NavHostController
) {

    NavHost(navController = navController, startDestination = Main.route) {
        composable(route = Main.route) {
            MainScreen()
        }

    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

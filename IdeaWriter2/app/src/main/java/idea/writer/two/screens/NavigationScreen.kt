package idea.writer.two.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun NavigationScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.SelectionScreen.route) {
        composable(Screen.SelectionScreen.route) { MainScreen(navController) }
        composable(Screen.NewNoteScreen.route) { AdaptiveStyledTextField(navController) }
        composable(Screen.ListNoteScreen.route) { OpenScreen(navController = navController) }
        composable(Screen.OpenNoteScreen.route + "?id={id}",
            arguments = listOf(
                navArgument(
                    name = "id"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )) { AdaptiveStyledTextField(navController = navController, noteId = it.arguments?.getInt("id")) }

    //        composable(Screen.Screen1.route) { AdaptiveStyledTextField(navController) }
//        composable(Screen.Screen2.route) { Screen2(navController) }
    }
}



sealed class Screen(val route: String) {
    object SelectionScreen : Screen("selection_screen")
    object NewNoteScreen : Screen("new_note_screen")
    object ListNoteScreen : Screen("list_note_screen")
    object OpenNoteScreen : Screen("open_note_screen")
}


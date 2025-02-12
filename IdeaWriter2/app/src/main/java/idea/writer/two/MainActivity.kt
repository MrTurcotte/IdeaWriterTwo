package idea.writer.two

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@AndroidEntryPoint
class MainActivity : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HideSystemBars()
            idea.writer.two.ui.theme.IdeaWriter2 {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    idea.writer.two.screens.NavigationScreen()

                }
            }
        }
    }
}

@Composable
fun HideSystemBars() {
    val systemUiController = rememberSystemUiController()

    systemUiController.apply {
        isStatusBarVisible = false // Hide the top bar
        isNavigationBarVisible = false // Hide the bottom bar
        isSystemBarsVisible = false // Ensure both are hidden
    }
}





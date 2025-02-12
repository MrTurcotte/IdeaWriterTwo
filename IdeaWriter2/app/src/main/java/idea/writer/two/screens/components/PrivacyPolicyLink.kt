package idea.writer.two.screens.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun PrivacyPolicyLink() {
    val context = LocalContext.current
    val isDarkMode = isSystemInDarkTheme()
    val secondaryTextColor = if (isDarkMode) Color.DarkGray else Color.LightGray
    val privacyPolicyUrl = "https://yourwebsite.com/privacy-policy" // Replace with your actual URL

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Privacy Policy",
            textDecoration = TextDecoration.Underline, // Make it look like a link
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl))
                context.startActivity(intent)
            },
            color = secondaryTextColor
        )
    }
}

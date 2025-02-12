package idea.writer.two.screens

import androidx.compose.foundation.background
import idea.writer.two.R
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import idea.writer.two.HideSystemBars
import idea.writer.two.screens.components.PrivacyPolicyLink

@Composable
fun MainScreen(navigation: NavController) {
    HideSystemBars()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Image Writer 2",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center, // = Alignment.Center
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            IconButton(
                onClick = { navigation.navigate(Screen.NewNoteScreen.route) },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                modifier = Modifier.size(80.dp)
            ) {

                Box(
                    modifier = Modifier
//                    .size(80.dp)
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = CircleShape,
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        //                tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            IconButton(
                onClick = { navigation.navigate(Screen.ListNoteScreen.route) },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                modifier = Modifier.size(80.dp)
            ) {

                Box(
                    modifier = Modifier
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = CircleShape,
                        )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.file_open_48px),// ImageVector.vectorResource(R.drawable.file_open_48px,
                        contentDescription = "Open",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        //                tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row() {

            PrivacyPolicyLink()

        }
    }

}
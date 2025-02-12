package idea.writer.two.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import idea.writer.two.HideSystemBars
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OpenScreen(viewmodel: NoteViewModel = hiltViewModel(), navController: NavController) {
    val navigate = navController
    val notes = viewmodel.allNotes.collectAsState(emptyList()).value
    val isDarkMode = isSystemInDarkTheme()
    val background = MaterialTheme.colorScheme.surface
    val primaryTextColor = MaterialTheme.colorScheme.onBackground
    val secondaryTextColor = if (isDarkMode) Color.DarkGray else Color.LightGray
    val rounding = 15

    HideSystemBars()
    Box(
        modifier = Modifier.background(background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(

            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp),
//                    .fillMaxWidth(),
                    contentAlignment = Alignment.TopStart,

                    ) {
                    IconButton(
                        onClick = { navigate.navigate(Screen.SelectionScreen.route) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(36.dp),
                            tint = secondaryTextColor
                        )

                    }
                }
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    items(items = notes) { item ->
                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        navController.navigate(Screen.OpenNoteScreen.route + "?id=${item.id}")
                                    },
                                    enabled = true,

//                    onClickLabel = TODO(),
//                    role = TODO()
                                ),
                            colors = CardColors(
                                containerColor = background,
                                contentColor = primaryTextColor,
                                disabledContainerColor = secondaryTextColor,
                                disabledContentColor = background
                            ),
                            shape = RoundedCornerShape(rounding),
//                colors = TODO(),
                            elevation = CardDefaults.cardElevation(4.dp),
//                border = TODO()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    text = item.title
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    text = formatTimestamp(item.timestamp)
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd - h:mma", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
package idea.writer.two.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import idea.writer.two.HideSystemBars
import idea.writer.two.components.MarkdownVisualTransformation

@Composable
fun AdaptiveStyledTextField(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel(),
    noteId: Int? = -1
) {
    val model = viewModel
    val navigate = navController
    var textFieldValue = model.contentTextField.collectAsState().value
    val isDarkMode = isSystemInDarkTheme()
    val primaryTextColor = MaterialTheme.colorScheme.onSurface
    val secondaryTextColor = if (isDarkMode) Color.DarkGray else Color.LightGray
    var titleValue = model.titleTextField.collectAsState().value
    val titleShape = RoundedCornerShape(50)
    val titleSize = 16.sp
    val contentSize = 24.sp

    // ScrollState for handling auto-scrolling
    val scrollState = rememberScrollState()

    // Remember TextLayoutResult to use it for auto-scrolling
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val density = LocalDensity.current
    val screenHeight = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }

    var boxHeight by remember {
        mutableStateOf(1f)
    }
    val defaultBoxHeightPx = boxHeight * screenHeight
    val imeBottom = WindowInsets.ime.getBottom(density = density)
    val keyboardHeight = imeBottom.toFloat()
    val titleBoxWeight = (defaultBoxHeightPx - keyboardHeight).coerceAtLeast(0f)
    val titleBoxHeightPercent = titleBoxWeight / screenHeight

    HideSystemBars()

    // Auto-scroll cursor to the bottom of the text field
    LaunchedEffect(textFieldValue.selection.start, textLayoutResult) {
        textLayoutResult?.let { layoutResult ->
            val cursorOffset = textFieldValue.selection.start
            val cursorLine =
                layoutResult.getLineForOffset(cursorOffset) // Find the line of the cursor
            val cursorLineBottom =
                layoutResult.getLineBottom(cursorLine) // Get the bottom of that line

            // Calculate the height of the text field (50% of the screen height)
            val textFieldHeight = screenHeight * 0.5f

            // Calculate the target scroll position to keep the cursor at the bottom
            val targetScroll = (cursorLineBottom - textFieldHeight / 8 * 5).coerceAtLeast(0f)

            // Animate scroll to the target position
            scrollState.animateScrollTo(
                targetScroll.toInt(),
                animationSpec = tween(durationMillis = 300)
            )
        }
    }

    LaunchedEffect(noteId) {
        // textFieldValue.selection.start, textLayoutResult) {
        model.getNote(noteId!!)
    }

    Box (
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
    ){
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,

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
        Box(
            modifier = Modifier
                .fillMaxHeight(0.5f) // 50% height
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.BottomStart // Ensure text starts from the bottom
        ) {
            Column {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState) // Apply scrollState to scroll the content
                ) {
                    BasicTextField(
                        value = textFieldValue,
                        onValueChange = { textFieldValue ->
                            model.updateContentTextField(textFieldValue)
                        },
                        textStyle = TextStyle(
                            fontSize = contentSize,
                            color = primaryTextColor
                        ), // Text color here
                        cursorBrush = SolidColor(primaryTextColor), // Cursor color matches primary text
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent), // Transparent background for the text
                        visualTransformation = MarkdownVisualTransformation(
                            cursorPosition = textFieldValue.selection.start,
                            lastLineColor = primaryTextColor,
                            textColor = secondaryTextColor
                        ),
                        decorationBox = { innerTextField ->
                            // Align the inner text field content to the bottom of the box
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentHeight(align = Alignment.Bottom)
                                    .padding(16.dp),
                                contentAlignment = Alignment.BottomStart
                            ) {
                                if (textFieldValue.text.isEmpty()) {
                                    Text(
                                        text = "Type here...",
                                        style = TextStyle(
                                            color = secondaryTextColor,
                                            fontSize = contentSize,
                                        )
                                    )
                                }
                                innerTextField() // Display the actual text inside the box
                            }
                        },
                        onTextLayout = { result ->
                            // Store the layout result when the text is laid out
                            textLayoutResult = result
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                        )
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight(titleBoxHeightPercent)
                .fillMaxWidth()
                .zIndex(2f)
                .padding(16.dp)
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            BasicTextField(
                value = titleValue,
                onValueChange = {
                    model.updateTitleTextField(it)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                textStyle = TextStyle(
                    color = primaryTextColor,
                    fontSize = titleSize,
                    textAlign = TextAlign.Center
                ),
                cursorBrush = SolidColor(primaryTextColor), // Cursor color matches primary text
                decorationBox = { innerTextField ->

                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier
//                        .border(
//                            width = 0.dp,
//                            color = primaryTextColor,
//                            shape = titleShape
//                        )
                            .background(
                                color = Color.Transparent,
                                shape = titleShape
                            )
                    ) {
                        if (titleValue.text.isEmpty()) {

                            Text(
                                text = "Title Here",
                                style = TextStyle(
                                    color = secondaryTextColor,
                                    fontSize = titleSize,
                                )
                            )
//                            modifier = Modifier.padding(16.dp)
                        }
                        innerTextField()
                    }

                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                )


            )
        }
    }

}
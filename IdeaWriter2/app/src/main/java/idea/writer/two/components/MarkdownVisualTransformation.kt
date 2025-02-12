package idea.writer.two.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle

class MarkdownVisualTransformation(
    private val cursorPosition: Int,
    private val lastLineColor: Color,
    private val textColor: Color
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val textContent = text.text
        val lines = textContent.split("\n") // Split the text into lines

        // Find the index of the paragraph the cursor is in
        val cursorLineIndex = getCursorLineIndex(cursorPosition, lines)

        // Create transformed text with paragraph-based color formatting
        val transformedText = buildAnnotatedString {
            lines.forEachIndexed { index, line ->
                val isCurrentParagraph = index == cursorLineIndex

                // Apply color based on whether this is the current paragraph or not
                val color = if (isCurrentParagraph) lastLineColor else textColor
                withStyle(SpanStyle(color = color)) {
                    append(line)
                }
                if (index != lines.lastIndex) append("\n")
            }
        }

        // Apply markdown transformations such as bold and italic (using the SimpleFormattingVisualTransformation logic)
        val markdownTransformedText =
            SimpleFormattingVisualTransformation().filter(transformedText).text

        return TransformedText(markdownTransformedText, OffsetMapping.Identity)
    }

    private fun getCursorLineIndex(cursorPosition: Int, lines: List<String>): Int {
        var lineStart = 0
        var lineIndex = 0

        for (line in lines) {
            val lineEnd = lineStart + line.length

            // If cursor is within the line OR exactly at the end of this line
            if (cursorPosition in lineStart..lineEnd) {
                return lineIndex
            }

            lineStart = lineEnd + 1 // Move to the next line (accounting for newline)
            lineIndex++
        }

        return lines.size - 1 // Default to last line
    }

    class SimpleFormattingVisualTransformation : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            return TransformedText(
                text = buildAnnotatedString {
                    append(text)
                    styleRegex(text, BOLD_ITALIC_REGEX, BOLD_ITALIC_STYLE)
                    styleRegex(text, BOLD_REGEX, BOLD_STYLE)
                    styleRegex(text, ITALIC_REGEX, ITALIC_STYLE)
                    styleRegex(text, CODE_REGEX, CODE_STYLE)
                    // Apply markdown styles such as bold and italic
                },
                offsetMapping = OffsetMapping.Identity
            )
        }

        private fun AnnotatedString.Builder.styleRegex(
            text: AnnotatedString,
            regex: Regex,
            spanStyle: SpanStyle
        ) {
            regex.findAll(text).forEach { match ->
                addStyle(spanStyle, match.range.first, match.range.endExclusive)
            }
        }

        companion object {
            private val BOLD_ITALIC_REGEX = Regex("\\*\\*\\*.*?\\*\\*\\*")
            private val BOLD_ITALIC_STYLE =
                SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)

            private val BOLD_REGEX = Regex("\\*\\*.*?\\*\\*")
            private val BOLD_STYLE = SpanStyle(fontWeight = FontWeight.Bold)

            private val ITALIC_REGEX = Regex("\\*.*?\\*")
            private val ITALIC_STYLE = SpanStyle(fontStyle = FontStyle.Italic)

            private val CODE_REGEX: Regex = Regex("`.*`") // Match code block `code`
            private val CODE_STYLE: SpanStyle =
                SpanStyle(fontFamily = FontFamily.Monospace, background = Color.Gray)

        }
    }
}
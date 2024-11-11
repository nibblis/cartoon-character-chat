package app.test.demochat.presentation.screens.auth.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneNumberTransformation(
    private val mask: String
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {

        val numberFilter = Regex("[^0-9]")
        val filteredNumber = text.text.replace(numberFilter, "")

        val maskDigitsCount = mask.count { it == '#' }
        val maskedPart = filteredNumber.take(maskDigitsCount)
        val extraPart = filteredNumber.drop(maskDigitsCount)

        val builder = StringBuilder()
        var numberIndex = 0
        mask.forEach { char ->
            when (char) {
                '#' -> {
                    if (numberIndex < maskedPart.length) {
                        builder.append(maskedPart[numberIndex])
                        numberIndex++
                    } else {
                        builder.append('_')
                    }
                }
                else -> {
                    builder.append(char)
                }
            }
        }

        builder.append(extraPart)

        val formattedText = builder.toString()
        val annotatedString = AnnotatedString(formattedText)

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var transformedOffset = 0
                var originalOffset = 0
                var numberIndex = 0

                while (originalOffset < offset && transformedOffset < formattedText.length) {
                    if (mask.getOrNull(transformedOffset) == '#') {
                        originalOffset++
                        numberIndex++
                    }
                    transformedOffset++
                }

                return transformedOffset.coerceIn(0, formattedText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                var originalOffset = 0
                var transformedOffset = 0
                var numberIndex = 0

                while (transformedOffset < offset && numberIndex < filteredNumber.length) {
                    if (mask.getOrNull(transformedOffset) == '#') {
                        originalOffset++
                        numberIndex++
                    }
                    transformedOffset++
                }

                return originalOffset.coerceIn(0, filteredNumber.length)
            }
        }

        return TransformedText(annotatedString, offsetTranslator)
    }
}



package app.test.demochat.presentation.screens.auth.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpLength: Int = 6,
    shouldShowError: Boolean = false,
    shouldShowCursor: Boolean = true,
    focusedColor: Color = MaterialTheme.colorScheme.secondary,
    unFocusedColor: Color = MaterialTheme.colorScheme.outline,
    errorColor: Color = MaterialTheme.colorScheme.error,
    onOtpModified: (String, Boolean) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpLength) {
                onOtpModified(it.text, it.text.length == otpLength)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(otpLength) { index ->
                    CharacterContainer(
                        index = index,
                        text = otpText,
                        shouldShowError = shouldShowError,
                        shouldShowCursor = shouldShowCursor,
                        focusedColor = focusedColor,
                        unFocusedColor = unFocusedColor,
                        errorColor = errorColor,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@Composable
internal fun CharacterContainer(
    index: Int,
    text: String,
    shouldShowError: Boolean,
    shouldShowCursor: Boolean,
    focusedColor: Color,
    unFocusedColor: Color,
    errorColor: Color,
) {
    val isFocused = text.length == index
    val character = when {
        index < text.length -> text[index].toString()
        else -> ""
    }

    val cursorVisible = remember { mutableStateOf(shouldShowCursor) }

    LaunchedEffect(key1 = isFocused) {
        if (isFocused && shouldShowCursor) {
            while (true) {
                delay(800)
                cursorVisible.value = !cursorVisible.value
            }
        }
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .border(
                width = when {
                    isFocused -> 2.dp
                    else -> 1.dp
                },
                color = when {
                    shouldShowError -> errorColor
                    isFocused ->  focusedColor
                    else -> unFocusedColor
                },
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = character,
            style = MaterialTheme.typography.titleLarge,
        )

        AnimatedVisibility(visible = isFocused && cursorVisible.value) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(2.dp)
                    .height(24.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
        }
    }
}
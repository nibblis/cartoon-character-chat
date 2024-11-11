package app.test.demochat.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import app.test.demochat.R

val Roboto = FontFamily(
    Font(R.font.roboto_regular),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 96.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 60.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
)
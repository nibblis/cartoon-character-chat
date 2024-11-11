package app.test.demochat.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

private val placeholderBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888).apply {
    Canvas(this).apply {
        drawColor(Color.GRAY)
    }
}.asImageBitmap()

fun String.asImageBitmap(): ImageBitmap {
    val imageBytes = try {
        val base64Data = if (this.contains(",")) {
            this.substring(this.indexOf(",") + 1)
        } else {
            this
        }
        Base64.decode(base64Data, Base64.DEFAULT)
    } catch (e: Exception) {
        null
    }

    if (imageBytes != null) {
        try {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                .asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    return placeholderBitmap
}

fun String.formatPhoneNumber(): String {
    val cleanedNumber = this.replace(Regex("[^0-9]"), "")

    return when (cleanedNumber.length) {
        10 -> "(${cleanedNumber.substring(0, 3)}) ${
            cleanedNumber.substring(
                3,
                6
            )
        }-${cleanedNumber.substring(6)}" // США
        11 -> "+${cleanedNumber.substring(0, 1)} ${
            cleanedNumber.substring(
                1,
                4
            )
        } ${cleanedNumber.substring(4, 7)} ${cleanedNumber.substring(7)}" // Другие страны
        else -> "+$cleanedNumber"
    }
}
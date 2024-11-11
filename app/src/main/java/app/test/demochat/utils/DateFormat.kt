package app.test.demochat.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

const val DATE_INPUT_FORMAT = "yyyy-MM-dd"
const val DATE_OUTPUT_FORMAT = "d MMMM yyyy"

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat(DATE_INPUT_FORMAT, Locale.getDefault())
        val outputFormat = SimpleDateFormat(DATE_OUTPUT_FORMAT, Locale("ru"))
        val date = inputFormat.parse(dateString)
        outputFormat.format(date)
    } catch (e: Exception) {
        dateString
    }
}

fun getZodiacSign(dateString: String): String {
    try {
        val format = SimpleDateFormat(DATE_INPUT_FORMAT, Locale.getDefault())
        val date = format.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = date

        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return when {
            (month == 3 && day >= 21) || (month == 4 && day <= 19) -> "♈ Овен"
            (month == 4 && day >= 20) || (month == 5 && day <= 20) -> "♉ Телец"
            (month == 5 && day >= 21) || (month == 6 && day <= 20) -> "♊ Близнецы"
            (month == 6 && day >= 21) || (month == 7 && day <= 22) -> "♋ Рак"
            (month == 7 && day >= 23) || (month == 8 && day <= 22) -> "♌ Лев"
            (month == 8 && day >= 23) || (month == 9 && day <= 22) -> "♍ Дева"
            (month == 9 && day >= 23) || (month == 10 && day <= 22) -> "♎ Весы"
            (month == 10 && day >= 23) || (month == 11 && day <= 21) -> "♏ Скорпион"
            (month == 11 && day >= 22) || (month == 12 && day <= 21) -> "♐ Стрелец"
            (month == 12 && day >= 22) || (month == 1 && day <= 19) -> "♑ Козерог"
            (month == 1 && day >= 20) || (month == 2 && day <= 18) -> "♒ Водолей"
            else -> "♓ Рыбы"
        }
    } catch (e: Exception) {
        return ""
    }
}
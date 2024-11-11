package app.test.demochat.utils

private const val PHONE_NUMBER_MASK = '#'

fun formatByMask(mask: String, input: String): String {
    val cleanInput = input.filter { it.isDigit() }

    if (cleanInput.isEmpty()) return ""

    val result = StringBuilder()
    var inputIndex = 0
    var maskIndex = 0

    while (maskIndex < mask.length && inputIndex < cleanInput.length) {
        when (mask[maskIndex]) {
            PHONE_NUMBER_MASK -> {
                result.append(cleanInput[inputIndex])
                inputIndex++
            }

            else -> {
                result.append(mask[maskIndex])
            }
        }
        maskIndex++
    }

    if (inputIndex < cleanInput.length) {
        result.append(cleanInput.substring(inputIndex))
    }

    return result.toString()
}
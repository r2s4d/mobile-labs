package ru.gpustore.app.util

/**
 * Форматирует цену для показа: 189990 становится "189 990 ₽".
 * Разделитель тысяч это неразрывный пробел, чтобы цена не переносилась на две строки.
 */
fun formatPrice(price: Int): String {
    val digits = price.toString()
    val grouped = StringBuilder()
    for ((i, ch) in digits.withIndex()) {
        // Перед каждой тройкой цифр, считая с конца, ставим пробел
        if (i > 0 && (digits.length - i) % 3 == 0) grouped.append(' ')
        grouped.append(ch)
    }
    return "$grouped ₽"
}

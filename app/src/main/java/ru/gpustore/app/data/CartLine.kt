package ru.gpustore.app.data

/**
 * Строка корзины для показа на экране: данные товара вместе с количеством.
 * Это не таблица, а результат запроса с объединением (JOIN) двух таблиц.
 */
data class CartLine(
    val cardId: Long,
    val name: String,
    val brand: String,
    val price: Int,
    val quantity: Int
) {
    /** Стоимость строки: цена за штуку, умноженная на количество */
    val lineTotal: Int get() = price * quantity
}

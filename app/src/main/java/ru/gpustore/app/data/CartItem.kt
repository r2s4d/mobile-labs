package ru.gpustore.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Позиция корзины: какая видеокарта и сколько штук.
 * Одна видеокарта лежит в корзине только одной строкой, поэтому cardId это первичный ключ.
 * Внешний ключ связывает позицию с товаром: при удалении товара позиция удалится сама.
 */
@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = GraphicsCard::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CartItem(
    @PrimaryKey val cardId: Long,
    val quantity: Int
)

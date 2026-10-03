package ru.gpustore.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/** Запись об избранной видеокарте. Хранится только id товара. */
@Entity(
    tableName = "favorite_items",
    foreignKeys = [
        ForeignKey(
            entity = GraphicsCard::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FavoriteItem(
    @PrimaryKey val cardId: Long
)

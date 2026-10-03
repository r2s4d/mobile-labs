package ru.gpustore.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Видеокарта: строка таблицы graphics_cards.
 * Room сам создаст таблицу по этому классу (@Entity).
 */
@Entity(tableName = "graphics_cards")
data class GraphicsCard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,          // Название модели
    val brand: String,         // Производитель: NVIDIA, AMD или Intel
    val memoryGb: Int,         // Объем видеопамяти в гигабайтах
    val memoryType: String,    // Тип памяти, например GDDR6X
    val busBits: Int,          // Разрядность шины памяти
    val powerWatts: Int,       // Энергопотребление в ваттах
    val price: Int,            // Цена в рублях
    val description: String    // Описание для карточки товара
) {
    /** Короткая строка характеристик для списка: "NVIDIA, 24 ГБ GDDR6X" */
    val shortSpec: String get() = "$brand, $memoryGb ГБ $memoryType"
}

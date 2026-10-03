package ru.gpustore.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Сама база данных. Здесь перечислены все таблицы (entities) и версия схемы.
 * При изменении таблиц версию нужно увеличить и описать миграцию.
 */
@Database(
    entities = [GraphicsCard::class, CartItem::class, FavoriteItem::class],
    version = 1,
    exportSchema = false
)
abstract class StoreDatabase : RoomDatabase() {

    abstract fun storeDao(): StoreDao

    companion object {
        private const val DB_NAME = "gpu_store.db"

        /** Создает файл базы на устройстве (SQLite) и возвращает доступ к нему */
        fun create(context: Context): StoreDatabase =
            Room.databaseBuilder(context.applicationContext, StoreDatabase::class.java, DB_NAME)
                .build()
    }
}

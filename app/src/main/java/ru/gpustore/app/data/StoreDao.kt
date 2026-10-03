package ru.gpustore.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object): все SQL-запросы к базе в одном месте.
 * Методы с типом Flow сами присылают новые данные при каждом изменении таблиц,
 * поэтому экраны обновляются без ручной перезагрузки.
 */
@Dao
abstract class StoreDao {

    // ---------- Каталог ----------

    /** Поиск по названию без учета регистра. Пустая строка вернет весь каталог. */
    @Query(
        "SELECT * FROM graphics_cards " +
            "WHERE name LIKE '%' || :query || '%' " +
            "ORDER BY price DESC"
    )
    abstract fun searchCards(query: String): Flow<List<GraphicsCard>>

    @Query("SELECT * FROM graphics_cards WHERE id = :id")
    abstract fun observeCard(id: Long): Flow<GraphicsCard?>

    @Query("SELECT COUNT(*) FROM graphics_cards")
    abstract suspend fun countCards(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertCards(cards: List<GraphicsCard>)

    // ---------- Корзина ----------

    @Query(
        "SELECT c.id AS cardId, c.name AS name, c.brand AS brand, c.price AS price, i.quantity AS quantity " +
            "FROM cart_items i INNER JOIN graphics_cards c ON c.id = i.cardId " +
            "ORDER BY c.name"
    )
    abstract fun observeCart(): Flow<List<CartLine>>

    /** Сколько штук конкретной видеокарты лежит в корзине (0, если нет) */
    @Query("SELECT COALESCE((SELECT quantity FROM cart_items WHERE cardId = :cardId), 0)")
    abstract fun observeCartQuantity(cardId: Long): Flow<Int>

    /** Общее число штук в корзине, для счетчика в главном меню */
    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items")
    abstract fun observeCartCount(): Flow<Int>

    @Query("UPDATE cart_items SET quantity = quantity + 1 WHERE cardId = :cardId")
    protected abstract suspend fun incrementQuantity(cardId: Long): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertCartItem(item: CartItem): Long

    /** Минимальное количество в корзине 1: ниже единицы уменьшать нельзя, для этого есть удаление */
    @Query("UPDATE cart_items SET quantity = quantity - 1 WHERE cardId = :cardId AND quantity > 1")
    abstract suspend fun decrementQuantity(cardId: Long)

    @Query("DELETE FROM cart_items WHERE cardId = :cardId")
    abstract suspend fun removeFromCart(cardId: Long)

    @Query("DELETE FROM cart_items")
    abstract suspend fun clearCart()

    /**
     * Добавить товар в корзину: если он уже есть, увеличиваем количество,
     * иначе создаем новую строку. @Transaction выполняет обе операции как одну.
     */
    @Transaction
    open suspend fun addToCart(cardId: Long) {
        if (incrementQuantity(cardId) == 0) {
            insertCartItem(CartItem(cardId, 1))
        }
    }

    // ---------- Избранное ----------

    @Query(
        "SELECT c.* FROM graphics_cards c " +
            "INNER JOIN favorite_items f ON f.cardId = c.id " +
            "ORDER BY c.name"
    )
    abstract fun observeFavorites(): Flow<List<GraphicsCard>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_items WHERE cardId = :cardId)")
    abstract fun observeIsFavorite(cardId: Long): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_items WHERE cardId = :cardId)")
    protected abstract suspend fun isFavorite(cardId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertFavorite(item: FavoriteItem)

    @Query("DELETE FROM favorite_items WHERE cardId = :cardId")
    abstract suspend fun removeFavorite(cardId: Long)

    /** Переключить избранное: если товар уже в избранном, убираем, иначе добавляем */
    @Transaction
    open suspend fun toggleFavorite(cardId: Long) {
        if (isFavorite(cardId)) removeFavorite(cardId) else insertFavorite(FavoriteItem(cardId))
    }
}

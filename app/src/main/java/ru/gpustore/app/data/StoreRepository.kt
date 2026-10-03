package ru.gpustore.app.data

import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий: единственная точка доступа экранов к данным.
 * ViewModel не знает про Room и SQL, он общается только с этим классом.
 */
class StoreRepository(private val dao: StoreDao) {

    fun searchCards(query: String): Flow<List<GraphicsCard>> = dao.searchCards(query.trim())
    fun observeCard(id: Long): Flow<GraphicsCard?> = dao.observeCard(id)

    val cart: Flow<List<CartLine>> = dao.observeCart()
    val cartCount: Flow<Int> = dao.observeCartCount()
    val favorites: Flow<List<GraphicsCard>> = dao.observeFavorites()

    fun observeCartQuantity(cardId: Long): Flow<Int> = dao.observeCartQuantity(cardId)
    fun observeIsFavorite(cardId: Long): Flow<Boolean> = dao.observeIsFavorite(cardId)

    suspend fun addToCart(cardId: Long) = dao.addToCart(cardId)
    suspend fun decrementQuantity(cardId: Long) = dao.decrementQuantity(cardId)
    suspend fun removeFromCart(cardId: Long) = dao.removeFromCart(cardId)
    suspend fun clearCart() = dao.clearCart()

    suspend fun toggleFavorite(cardId: Long) = dao.toggleFavorite(cardId)
    suspend fun removeFavorite(cardId: Long) = dao.removeFavorite(cardId)

    /** Заполняет каталог начальными данными при первом запуске (когда таблица пустая) */
    suspend fun seedIfEmpty() {
        if (dao.countCards() == 0) {
            dao.insertCards(SeedData.cards)
        }
    }
}

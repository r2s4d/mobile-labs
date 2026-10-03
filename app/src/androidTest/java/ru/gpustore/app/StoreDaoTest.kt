package ru.gpustore.app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.gpustore.app.data.SeedData
import ru.gpustore.app.data.StoreDao
import ru.gpustore.app.data.StoreDatabase

/**
 * Тесты запросов к базе. Используется база в памяти: она создается заново для каждого теста
 * и не затрагивает данные настоящего приложения. Запускаются на эмуляторе.
 */
@RunWith(AndroidJUnit4::class)
class StoreDaoTest {

    private lateinit var db: StoreDatabase
    private lateinit var dao: StoreDao

    @Before
    fun setUp() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, StoreDatabase::class.java).build()
        dao = db.storeDao()
        dao.insertCards(SeedData.cards)
    }

    @After
    fun tearDown() {
        db.close()
    }

    /** id первой карты каталога (самой дорогой, как в сортировке списка) */
    private suspend fun firstCardId(): Long = dao.searchCards("").first().first().id

    @Test
    fun catalogContainsAllSeededCards() = runTest {
        assertEquals(SeedData.cards.size, dao.searchCards("").first().size)
    }

    @Test
    fun searchIgnoresCase() = runTest {
        val found = dao.searchCards("rtx 4090").first()
        assertEquals(1, found.size)
        assertEquals("GeForce RTX 4090", found.first().name)
    }

    @Test
    fun searchWithoutMatchesReturnsEmptyList() = runTest {
        assertTrue(dao.searchCards("такой карты нет").first().isEmpty())
    }

    @Test
    fun addingTwiceIncreasesQuantity() = runTest {
        val id = firstCardId()
        dao.addToCart(id)
        dao.addToCart(id)
        assertEquals(2, dao.observeCartQuantity(id).first())
        assertEquals(1, dao.observeCart().first().size)
    }

    @Test
    fun quantityNeverDropsBelowOne() = runTest {
        val id = firstCardId()
        dao.addToCart(id)
        dao.decrementQuantity(id)
        assertEquals(1, dao.observeCartQuantity(id).first())
    }

    @Test
    fun removeAndClearCart() = runTest {
        val ids = dao.searchCards("").first().take(2).map { it.id }
        ids.forEach { dao.addToCart(it) }
        dao.removeFromCart(ids[0])
        assertEquals(1, dao.observeCart().first().size)
        dao.clearCart()
        assertEquals(0, dao.observeCartCount().first())
    }

    @Test
    fun cartLineTotalIsPriceTimesQuantity() = runTest {
        val card = dao.searchCards("").first().first()
        dao.addToCart(card.id)
        dao.addToCart(card.id)
        assertEquals(card.price * 2, dao.observeCart().first().single().lineTotal)
    }

    @Test
    fun toggleFavoriteAddsAndRemoves() = runTest {
        val id = firstCardId()
        dao.toggleFavorite(id)
        assertTrue(dao.observeIsFavorite(id).first())
        assertEquals(1, dao.observeFavorites().first().size)
        dao.toggleFavorite(id)
        assertFalse(dao.observeIsFavorite(id).first())
    }
}

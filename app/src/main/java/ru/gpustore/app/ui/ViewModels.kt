package ru.gpustore.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.gpustore.app.data.CartLine
import ru.gpustore.app.data.GraphicsCard
import ru.gpustore.app.data.StoreRepository

/*
 * ViewModel хранит состояние экрана и переживает поворот экрана.
 * Экран только подписывается на StateFlow и рисует то, что пришло.
 * stateIn превращает холодный Flow из базы в StateFlow с последним значением.
 * WhileSubscribed(5000) держит подписку еще 5 секунд после ухода с экрана,
 * чтобы при повороте не перезапрашивать данные.
 */

/** Главное меню: показывает, сколько товаров лежит в корзине */
class MenuViewModel(repository: StoreRepository) : ViewModel() {
    val cartCount: StateFlow<Int> = repository.cartCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}

/** Каталог: список видеокарт с поиском по названию */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CatalogViewModel(repository: StoreRepository) : ViewModel() {

    private val query = MutableStateFlow("")

    /** Каждый ввод символа меняет query; debounce ждет паузу 250 мс, чтобы не дергать базу на каждую букву */
    val cards: StateFlow<List<GraphicsCard>?> = query
        .debounce(250)
        .flatMapLatest { repository.searchCards(it) }
        .map<List<GraphicsCard>, List<GraphicsCard>?> { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun onQueryChanged(text: String) {
        query.value = text
    }
}

/** Состояние экрана товара: сам товар, признак избранного и количество в корзине */
data class ItemState(
    val card: GraphicsCard? = null,
    val isFavorite: Boolean = false,
    val cartQuantity: Int = 0
)

/** Экран отдельной видеокарты */
class ItemViewModel(
    private val repository: StoreRepository,
    private val cardId: Long
) : ViewModel() {

    val state: StateFlow<ItemState> = combine(
        repository.observeCard(cardId),
        repository.observeIsFavorite(cardId),
        repository.observeCartQuantity(cardId)
    ) { card, favorite, quantity -> ItemState(card, favorite, quantity) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ItemState())

    fun addToCart() {
        viewModelScope.launch { repository.addToCart(cardId) }
    }

    fun toggleFavorite() {
        viewModelScope.launch { repository.toggleFavorite(cardId) }
    }
}

/** Корзина: строки, итоговая сумма и избранное */
class CartViewModel(private val repository: StoreRepository) : ViewModel() {

    /** Выбранная вкладка: 0 это корзина, 1 это избранное */
    val selectedTab = MutableStateFlow(0)

    val cart: StateFlow<List<CartLine>> = repository.cart
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Итог пересчитывается автоматически при любом изменении корзины */
    val total: StateFlow<Int> = repository.cart
        .map { lines -> lines.sumOf { it.lineTotal } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val favorites: StateFlow<List<GraphicsCard>> = repository.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun increase(cardId: Long) = launch { repository.addToCart(cardId) }
    fun decrease(cardId: Long) = launch { repository.decrementQuantity(cardId) }
    fun remove(cardId: Long) = launch { repository.removeFromCart(cardId) }
    fun checkout() = launch { repository.clearCart() }
    fun removeFavorite(cardId: Long) = launch { repository.removeFavorite(cardId) }

    private fun launch(block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }
}

package ru.gpustore.app.ui

import android.content.Intent
import android.os.Bundle
import ru.gpustore.app.util.enableDarkEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.gpustore.app.R
import ru.gpustore.app.data.CartLine
import ru.gpustore.app.data.GraphicsCard
import ru.gpustore.app.databinding.ActivityCartBinding
import ru.gpustore.app.util.applySystemBarsPadding
import ru.gpustore.app.util.formatPrice

/**
 * Корзина и избранное на одном экране с двумя вкладками.
 * Все данные берутся из базы, любое изменение сразу отражается в списке и в итоговой сумме.
 */
class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding

    private val viewModel: CartViewModel by viewModels {
        val repo = repository
        viewModelFactory { initializer { CartViewModel(repo) } }
    }

    /** Все, что нужно для отрисовки экрана, собрано в один объект */
    private data class CartUi(
        val tab: Int,
        val cart: List<CartLine>,
        val favorites: List<GraphicsCard>,
        val total: Int
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableDarkEdgeToEdge()
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        binding.toolbar.setNavigationOnClickListener { finish() }

        val cartAdapter = CartAdapter(
            onOpen = { openItem(it.cardId) },
            onIncrease = { viewModel.increase(it.cardId) },
            onDecrease = { viewModel.decrease(it.cardId) },
            onRemove = { viewModel.remove(it.cardId) }
        )
        val favoriteAdapter = FavoriteAdapter(
            onClick = { openItem(it.id) },
            onRemove = { viewModel.removeFavorite(it.id) }
        )
        binding.list.layoutManager = LinearLayoutManager(this)

        // Номер выбранной вкладки хранится во ViewModel, поэтому не сбрасывается при повороте
        binding.tabs.getTabAt(viewModel.selectedTab.value)?.select()
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.selectedTab.value = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) = Unit
            override fun onTabReselected(tab: TabLayout.Tab) = Unit
        })

        binding.checkoutButton.setOnClickListener { confirmCheckout() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    viewModel.selectedTab, viewModel.cart, viewModel.favorites, viewModel.total
                ) { tab, cart, favorites, total -> CartUi(tab, cart, favorites, total) }
                    .collect { ui ->
                        val showCart = ui.tab == TAB_CART
                        // Меняем адаптер только при смене вкладки
                        val adapter = if (showCart) cartAdapter else favoriteAdapter
                        if (binding.list.adapter !== adapter) binding.list.adapter = adapter

                        if (showCart) cartAdapter.submitList(ui.cart) else favoriteAdapter.submitList(ui.favorites)

                        val isEmpty = if (showCart) ui.cart.isEmpty() else ui.favorites.isEmpty()
                        binding.emptyText.isVisible = isEmpty
                        binding.emptyText.setText(
                            if (showCart) R.string.cart_empty else R.string.favorites_empty
                        )
                        // Панель с итогом нужна только во вкладке корзины и только если в ней что-то есть
                        binding.totalPanel.isVisible = showCart && !isEmpty
                        binding.totalValue.text = formatPrice(ui.total)
                    }
            }
        }
    }

    private fun openItem(cardId: Long) {
        startActivity(Intent(this, ItemActivity::class.java).putExtra(ItemActivity.EXTRA_CARD_ID, cardId))
    }

    /** Перед оформлением спрашиваем подтверждение, затем очищаем корзину */
    private fun confirmCheckout() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.checkout_title)
            .setMessage(getString(R.string.checkout_message, formatPrice(viewModel.total.value)))
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.checkout_confirm) { _, _ ->
                viewModel.checkout()
                Snackbar.make(binding.root, R.string.checkout_done, Snackbar.LENGTH_LONG).show()
            }
            .show()
    }

    private companion object {
        const val TAB_CART = 0
    }
}

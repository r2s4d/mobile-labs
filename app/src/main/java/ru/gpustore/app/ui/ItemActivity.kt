package ru.gpustore.app.ui

import android.content.Intent
import android.os.Bundle
import ru.gpustore.app.util.enableDarkEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.gpustore.app.R
import ru.gpustore.app.databinding.ActivityItemBinding
import ru.gpustore.app.util.applySystemBarsPadding
import ru.gpustore.app.util.brandArt
import ru.gpustore.app.util.formatPrice

/**
 * Карточка одной видеокарты: характеристики, цена и кнопки "В корзину" и "В избранное".
 * Какую именно карту показывать, узнаем из Intent по ключу EXTRA_CARD_ID.
 */
class ItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemBinding

    private val viewModel: ItemViewModel by viewModels {
        val repo = repository
        val cardId = intent.getLongExtra(EXTRA_CARD_ID, -1L)
        viewModelFactory { initializer { ItemViewModel(repo, cardId) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableDarkEdgeToEdge()
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.buttonCart.setOnClickListener {
            viewModel.addToCart()
            Snackbar.make(binding.root, R.string.item_added_to_cart, Snackbar.LENGTH_SHORT)
                // Уведомление показывается над кнопками, а не поверх них
                .setAnchorView(binding.actionsBar)
                .setAction(R.string.item_open_cart) {
                    startActivity(Intent(this, CartActivity::class.java))
                }
                .show()
        }
        binding.buttonFavorite.setOnClickListener { viewModel.toggleFavorite() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val card = state.card ?: return@collect
                    binding.image.setImageResource(brandArt(card.brand))
                    binding.name.text = card.name
                    binding.price.text = formatPrice(card.price)
                    binding.valueBrand.text = card.brand
                    binding.valueMemory.text = getString(R.string.item_gb, card.memoryGb)
                    binding.valueMemoryType.text = card.memoryType
                    binding.valueBus.text = getString(R.string.item_bits, card.busBits)
                    binding.valuePower.text = getString(R.string.item_watts, card.powerWatts)
                    binding.description.text = card.description

                    // Текст и значок кнопок отражают текущее состояние из базы
                    binding.buttonCart.text = if (state.cartQuantity > 0) {
                        getString(R.string.item_in_cart, state.cartQuantity)
                    } else {
                        getString(R.string.item_add_to_cart)
                    }
                    binding.buttonFavorite.setText(
                        if (state.isFavorite) R.string.item_remove_favorite else R.string.item_add_favorite
                    )
                    binding.buttonFavorite.setIconResource(
                        if (state.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart
                    )
                }
            }
        }
    }

    companion object {
        /** Ключ, по которому id товара передается между экранами */
        const val EXTRA_CARD_ID = "card_id"
    }
}

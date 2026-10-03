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
import kotlinx.coroutines.launch
import ru.gpustore.app.R
import ru.gpustore.app.databinding.ActivityMenuBinding
import ru.gpustore.app.util.applySystemBarsPadding

/**
 * Стартовый экран с главным меню: три кнопки ведут на каталог, корзину и экран "О себе".
 * Intent описывает намерение открыть другую Activity, startActivity выполняет переход.
 */
class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    private val viewModel: MenuViewModel by viewModels {
        val repo = repository
        viewModelFactory { initializer { MenuViewModel(repo) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableDarkEdgeToEdge()
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        binding.rowCatalog.setOnClickListener { open(MainActivity::class.java) }
        binding.rowCart.setOnClickListener { open(CartActivity::class.java) }
        binding.rowAbout.setOnClickListener { open(AboutActivity::class.java) }

        // Подписка на счетчик корзины. repeatOnLifecycle работает, пока экран виден,
        // и сам отписывается в фоне, чтобы не тратить ресурсы.
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartCount.collect { count ->
                    binding.rowCartSubtitle.text = if (count == 0) {
                        getString(R.string.menu_cart_empty)
                    } else {
                        getString(R.string.menu_cart_count, count)
                    }
                }
            }
        }
    }

    private fun open(target: Class<*>) {
        startActivity(Intent(this, target))
    }
}

package ru.gpustore.app.ui

import android.content.Intent
import android.os.Bundle
import ru.gpustore.app.util.enableDarkEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.gpustore.app.databinding.ActivityMainBinding
import ru.gpustore.app.util.applySystemBarsPadding

/**
 * Каталог: главная форма со списком всех видеокарт из базы данных.
 * Нажатие на строку открывает ItemActivity с подробной информацией.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: CatalogViewModel by viewModels {
        val repo = repository
        viewModelFactory { initializer { CatalogViewModel(repo) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableDarkEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        binding.toolbar.setNavigationOnClickListener { finish() }

        val adapter = CardAdapter { card ->
            // Передаем в ItemActivity только id, остальное она сама прочитает из базы
            startActivity(
                Intent(this, ItemActivity::class.java).putExtra(ItemActivity.EXTRA_CARD_ID, card.id)
            )
        }
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = adapter

        // Каждое изменение текста в поиске отправляется во ViewModel
        binding.searchInput.doAfterTextChanged { viewModel.onQueryChanged(it?.toString().orEmpty()) }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cards.collect { cards ->
                    // null означает, что данные еще не загружены: ничего не показываем
                    if (cards != null) {
                        adapter.submitList(cards)
                        binding.emptyText.isVisible = cards.isEmpty()
                    }
                }
            }
        }
    }
}
